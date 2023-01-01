package com.key.password_manager.cache;

import javax.annotation.Resource;
import org.bouncycastle.jcajce.provider.symmetric.util.BaseMac;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Component;
import com.key.password_manager.utils.BaseModel;

@Component
public class RedisCache<T extends BaseModel> {

	@Resource(name = "redisTemplate")
	private HashOperations<Long, String, T> hashOperations;

	public T save(T entity, String hashkey) {
		hashOperations.put(entity.getId(), hashkey, entity);
		return entity;
	}

	@SuppressWarnings("unchecked")
	public T findEntityById(Long id, String hashkey) {
		return hashOperations.get(id, hashkey);
	}
}
