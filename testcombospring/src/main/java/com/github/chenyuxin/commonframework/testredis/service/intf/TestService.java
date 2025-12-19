package com.github.chenyuxin.commonframework.testredis.service.intf;

import java.util.List;
import java.util.Map;

public interface TestService {
	
    List<Map<String,Object>> queryByPhoneNo(String phoneNo);

    /**
     * 查询默认数据库
     * @param phoneNo
     * @return
     */
	List<Map<String, Object>> queryByPhoneNoDs1(String phoneNo);
	
	/**
	 * 查询数据库2
	 * @param phoneNo
	 * @return
	 */
	List<Map<String, Object>> queryByPhoneNoDs2(String phoneNo);

	/**
	 * 验证CommonDao所有方法
	 */
	void verifyCommonDao();

}
