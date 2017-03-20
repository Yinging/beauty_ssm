package com.yingjun.ssm.cache;

import com.yingjun.ssm.util.ProtoStuffSerializerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * redis缓存
 * 
 * @author yingjun
 *
 */
@Component
public class RedisCache {
	
	
	public final static String CAHCENAME="cache";//缓存名
	public final static int CAHCETIME=60;//默认缓存时间

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	/**
	 * 将单条数据写入到redis中，不设置缓存时间
	 * @param key
	 * @param obj
	 * @param <T>
	 * @return
	 */
	public <T> boolean putCache(String key, T obj) {
		final byte[] bkey = key.getBytes();
		final byte[] bvalue = ProtoStuffSerializerUtil.serialize(obj);
		boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.setNX(bkey, bvalue);
			}
		});
		return result;
	}

	/**
	 * 将单条数据写入到redis中，设置过期时间
	 * @param key
	 * @param obj
	 * @param expireTime
	 * @param <T>
	 */
	public <T> void putCacheWithExpireTime(String key, T obj, final long expireTime) {
		final byte[] bkey = key.getBytes();
		final byte[] bvalue = ProtoStuffSerializerUtil.serialize(obj);
		redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				connection.setEx(bkey, expireTime, bvalue);
				return true;
			}
		});
	}

	/**
	 * 将多条数据写入到redis中
	 * @param key
	 * @param objList
	 * @param <T>
	 * @return
	 */
	public <T> boolean putListCache(String key, List<T> objList) {
		final byte[] bkey = key.getBytes();
		final byte[] bvalue = ProtoStuffSerializerUtil.serializeList(objList);
		boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.setNX(bkey, bvalue);
			}
		});
		return result;
	}

	/**
	 * 将多条数据写入到redis中，设置过期时间
	 * @param key
	 * @param objList
	 * @param expireTime
	 * @param <T>
	 * @return
	 */
	public <T> boolean putListCacheWithExpireTime(String key, List<T> objList, final long expireTime) {
		final byte[] bkey = key.getBytes();
		final byte[] bvalue = ProtoStuffSerializerUtil.serializeList(objList);
		boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				connection.setEx(bkey, expireTime, bvalue);
				return true;
			}
		});
		return result;
	}

	/**
	 * 通过键获取对应的redis缓存值
	 * @param key
	 * @param targetClass
	 * @param <T>
	 * @return
	 */
	public <T> T getCache(final String key, Class<T> targetClass) {
		byte[] result = redisTemplate.execute(new RedisCallback<byte[]>() {
			@Override
			public byte[] doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.get(key.getBytes());
			}
		});
		if (result == null) {
			return null;
		}
		//反序列化
		return ProtoStuffSerializerUtil.deserialize(result, targetClass);
	}

	/**
	 * 根据key获取多条redis中存在的缓存
	 * @param key
	 * @param targetClass
	 * @param <T>
	 * @return
	 */
	public <T> List<T> getListCache(final String key, Class<T> targetClass) {
		byte[] result = redisTemplate.execute(new RedisCallback<byte[]>() {
			@Override
			public byte[] doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.get(key.getBytes());
			}
		});
		if (result == null) {
			return null;
		}
		return ProtoStuffSerializerUtil.deserializeList(result, targetClass);
	}

	/**
	 * 精确删除key
	 * 
	 * @param key
	 */
	public void deleteCache(String key) {
		redisTemplate.delete(key);
	}

	/**
	 * 模糊删除key
	 * 
	 * @param pattern
	 */
	public void deleteCacheWithPattern(String pattern) {
		Set<String> keys = redisTemplate.keys(pattern);
		redisTemplate.delete(keys);
	}

	/**
	 * 清空所有缓存
	 */
	public void clearCache() {
		deleteCacheWithPattern(RedisCache.CAHCENAME+"|*");
	}
}
