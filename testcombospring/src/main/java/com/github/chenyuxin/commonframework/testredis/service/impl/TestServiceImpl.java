package com.github.chenyuxin.commonframework.testredis.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.chenyuxin.commonframework.daojpa.intf.CommonDao;
import com.github.chenyuxin.commonframework.daojpa.option.DaoOptions;
import com.github.chenyuxin.commonframework.daojpa.common.DaoConst;
import com.github.chenyuxin.commonframework.daojpa.common.TableType;
import com.github.chenyuxin.commonframework.testredis.component.AsyncCache;
import com.github.chenyuxin.commonframework.testredis.service.intf.TestService;

@Service
public class TestServiceImpl implements TestService {
	
	@Autowired CommonDao commonDao;
	
	@Autowired AsyncCache asyncCache;
	
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(TestServiceImpl.class);
	
    @Override
    public List<Map<String, Object>> queryByPhoneNo(String phoneNo) {
        log.info("start");

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> list = (List<Map<String, Object>>) asyncCache.cacheRead(phoneNo);
        if (null==list) {
            log.info("从数据库读数据");
            
            // 检查多数据源 mydata 中是否有 sys_user 表
            boolean hasTable = commonDao.isTable(TableType.of("testredis"), "mydata");
            log.info("mydata数据源中存在sys_user表: {}", hasTable);
            
            Map<String,Object> paramMap = new HashMap<String,Object>();
            paramMap.put("phoneNo", phoneNo);
            list = commonDao.selectObjMap("select * from testredis where phone_no=:phoneNo", paramMap);
            
            asyncCache.cacheData(phoneNo, list);//redis异步缓存
        }
        return list;
    }
    
    @Transactional(transactionManager = DaoConst.defaultDataSourceName + DaoConst.TransactionManager)
    @Override
    public List<Map<String, Object>> queryByPhoneNoDs1(String phoneNo) {
        return commonDao.selectObjMap("select * from testredis where phone_no=:phoneNo", 
        		DaoOptions.builder()
        		.addParam("phoneNo", phoneNo)
        		.build());
    }

    @Transactional(transactionManager = "mydata" + DaoConst.TransactionManager)
	@Override
	public List<Map<String, Object>> queryByPhoneNoDs2(String phoneNo) {
		return commonDao.selectObjMap("select * from testredis where phone_no=:phoneNo", 
        		DaoOptions.builder()
        		.dataSourceName("mydata")
        		.addParam("phoneNo", phoneNo)
        		.build());
	}

	@Override
	public void verifyCommonDao() {
		log.info("Start verifyCommonDao");
		String tableName = "testredis"; // Using existing table
		TableType tableType = TableType.of(tableName);
		
		// 1. Schema Operations
		log.info("1. Testing Schema Operations...");
		boolean isTable = commonDao.isTable(tableType);
		log.info("isTable({}): {}", tableName, isTable);
		
		// 2. Save Operations
		log.info("2. Testing Save Operations...");
		Map<String, Object> newObj = new HashMap<>();
		String testPhone = "13800138000";
		newObj.put("phone_no", testPhone);
		// Assuming other columns are nullable or have defaults. Check table structure if needed.
		// For safety, let's use unique phone number
		long timestamp = System.currentTimeMillis();
		String uniquePhone = "1" + String.valueOf(timestamp).substring(1); 
		newObj.put("phone_no", uniquePhone);
		
		log.info("Testing saveObjSingle...");
		commonDao.saveObjSingle(newObj, tableType, DaoConst.defaultDataSourceName, true);
		log.info("saveObjSingle success");
		
		// Verify save
		Map<String, Object> savedObj = commonDao.selectObjSingle("select * from " + tableName + " where phone_no = :phoneNo", 
				Map.class, DaoOptions.builder().addParam("phoneNo", uniquePhone).build());
		if(savedObj == null) log.error("saveObjSingle failed verification!");
		else log.info("Verified saved object: {}", savedObj);

		// 3. Update Operations
		log.info("3. Testing Update Operations...");
		if(savedObj != null) {
			// Update phone number
			String updatedPhone = uniquePhone + "_upd";
			savedObj.put("phone_no", updatedPhone);
			
			log.info("Testing updateObj...");
			// updateObj uses @Table annotation or TableType. 
			// Since we act on Map, we need primary key or unique key in where clause if it's generic update.
			// commonDao.updateObj usually relies on Entity class mapping. 
			// Let's use updateSingle which takes fieldNameByIds. 
			// Check if we have primary key 'id' in the map.
			// If testredis has 'id' column, it should be in savedObj.
			
			if(savedObj.containsKey("id")) {
				log.info("Table has ID, testing updateSingle with ID...");
				commonDao.updateSingle(savedObj, new String[]{"id"}, tableType, DaoConst.defaultDataSourceName, true);
				log.info("updateSingle success");
				
				// Verify update
				Map<String, Object> updatedObj = commonDao.selectObjSingle("select * from " + tableName + " where id = :id", 
						Map.class, DaoOptions.builder().addParam("id", savedObj.get("id")).build());
				if(updatedObj != null && updatedPhone.equals(updatedObj.get("phone_no"))) {
					log.info("Verified update object: {}", updatedObj);
				} else {
					log.error("updateSingle failed verification!");
				}
			} else {
				log.warn("No 'id' column found, skipping primary key based update tests.");
			}
		}
		
		// 4. Select Operations
		log.info("4. Testing Select Operations...");
		log.info("Testing getRecords...");
		Long count = commonDao.getRecords(tableType);
		log.info("getRecords count: {}", count);
		
		log.info("Testing selectBaseObj (count)...");
		Integer countInt = commonDao.selectBaseObj("select count(1) from " + tableName, Integer.class);
		log.info("selectBaseObj count: {}", countInt);
		
		// 5. Delete Operations
		log.info("5. Testing Delete Operations...");
		// Use the object we created
		Map<String, Object> objToDelete = commonDao.selectObjSingle("select * from " + tableName + " where phone_no like :phoneNo", 
				Map.class, DaoOptions.builder().addParam("phoneNo", uniquePhone + "%").build());
		
		if(objToDelete != null && objToDelete.containsKey("id")) {
			log.info("Testing deleteObjSingle...");
			commonDao.deleteObjSingle(objToDelete, tableType, DaoConst.defaultDataSourceName, true);
			log.info("deleteObjSingle success");
			
			// Verify delete
			Map<String, Object> deletedObj = commonDao.selectObjSingle("select * from " + tableName + " where id = :id", 
					Map.class, DaoOptions.builder().addParam("id", objToDelete.get("id")).build());
			if(deletedObj == null) {
				log.info("Verified delete object: success");
			} else {
				log.error("deleteObjSingle failed verification!");
			}
		}

		log.info("End verifyCommonDao");
	}
}
