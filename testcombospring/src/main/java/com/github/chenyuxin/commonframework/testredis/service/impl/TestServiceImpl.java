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

}
