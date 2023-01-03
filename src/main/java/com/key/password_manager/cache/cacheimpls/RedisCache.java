package com.key.password_manager.cache.cacheimpls;

import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Component;
import com.key.password_manager.cache.CacheNameIdentifierRegistry;
import com.key.password_manager.utils.BaseModel;

@Component("redis")
public class RedisCache<T extends BaseModel> implements Cache<T> {

	@Autowired
	private CacheNameIdentifierRegistry cacheHashkeyRegistry;

	@Resource(name = "redisTemplate")
	private HashOperations<Long, String, T> hashOperations;

	@Override
	public T save(T entity) {
		hashOperations.put(entity.getId(), cacheHashkeyRegistry.hashKey(entity.getClass()), entity);
		return entity;
	}

	@Override
	public T find(Long id, Class<T> clazz) {
		return hashOperations.get(id, cacheHashkeyRegistry.hashKey(clazz));
	}

	@Override
	public boolean delete(T entity) {
		return delete(entity.getId(), entity.getClass());
	}

	public boolean delete(Long id, Class<?> clazz) {
		try {
			hashOperations.delete(id, cacheHashkeyRegistry.hashKey(clazz));
			return true;
		} catch (Exception e) {
			// LOG
		}
		return false;
	}
}
