package com.key.password_manager.cache;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * Stores the name of the cache identifier for any class. The registry picks the name from the
 * annotation {@link CacheNameIdentifier}.
 */
@Component
public class CacheNameIdentifierRegistry {

	private Map<Class<?>, String> cacheNameIdentifierMap = new HashMap<Class<?>, String>();

	public void set(Class<?> clazz, String hashkey) {
		cacheNameIdentifierMap.put(clazz, hashkey);
	}

	public String hashKey(Class<?> clazz) {
		return cacheNameIdentifierMap.get(clazz);
	}
}
