package com.github.chenyuxin.combo.util;

/**
 * 分布式锁的实现
 */
public class DistributedLock {
	
//	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(UseRedisServiceImpl.class);
//
//	@Autowired CommonRedisTemplate commonRedisTemplate;
//	
//	@Override
//	public Boolean verifyUseIntfTimes(ModelData modelData) {
//		long endTime = 0;//init endTime
//		
//		String lockKey = modelData.getModelId();//对应模型接口锁
//		String lockValue = CommonUtilUUID.getUUID64();//设置锁为本应用标识，作为lockValue，解铃还需系铃人。
//		long timeOut4Key = 8000;//8秒redis锁超时失效
//		long safeTime = 2000;//还剩2秒，锁超时时间快到时，作为安全缓冲，计算本应用的超时时间，避免释放锁时释放到别人刚获取到锁的极端情况。
//		try {
//			Boolean ok = null;
//			do {//3，2，1开抢！
//				try {	Thread.sleep(0);	} catch (InterruptedException e) {	}
//				ok = commonRedisTemplate.opsForValue().setIfAbsent(lockKey, lockValue, Duration.ofMillis(timeOut4Key));
//				log.debug(String.valueOf(ok));
//			} while (null == ok);//拿到锁后，停止遍历拿锁执行业务
//			long startTime = System.currentTimeMillis();//业务开始前⏲计时
//			endTime = startTime + timeOut4Key-safeTime;//redis还有safeTime秒就要失效，则默认任务超时锁失效;
//			
//			String intfName = modelData.getModelName();
//			Boolean used = commonRedisTemplate.opsForValue().setIfPresent(intfName, 1, Duration.ofHours(24));//不存在，则初始调用计为1次 
//			if (null == used) {//如果存在key则开始计数
//				int useIntfTimes = (int) commonRedisTemplate.opsForValue().get(intfName);
//				if (useIntfTimes >= modelData.getUseIntfTimes()) {
//					return false;//超过最大执行次数
//				}
//				useIntfTimes++;//调用次数加1
//				
////				if (endTime > System.currentTimeMillis()) {
////					return null;//中途任何时候都可进行超时判断
////				}
//				commonRedisTemplate.opsForValue().set(intfName, useIntfTimes);
//
//			}
//			
//			
//		} finally {//最终释放锁
//			String redisLockValue = commonRedisTemplate.opsForValue().get(lockKey).toString();
//			if (endTime < System.currentTimeMillis()) {//redis还有safeTime秒就要失效,就不执行释放锁了，让redis自动超时释放，不然就释放自己的锁
//				if (redisLockValue.equals(lockValue)) {//解铃只解自己铃
//					commonRedisTemplate.delete(lockKey);
//				}
//			}
//		}
//		
//		return true;//接口次数没有超过限制
//	}

}
