package com.key.password_manager.cache;

import java.util.Map;
import javax.persistence.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class CacheNameIdentifierProcessor implements ApplicationRunner {

	@Autowired
	public ApplicationContext applicationContext;

	@Autowired
	private CacheNameIdentifierRegistry cacheHashkeyRegistery;

	private final Logger LOG = LoggerFactory.getLogger(this.getClass());

	@Override
	public void run(ApplicationArguments args) throws Exception {
		registerCacheableBeans();
	}

	/**
	 * Register beans with annotation {@link CacheNameIdentifier}. This method all the beans with
	 * annotation {@link Entity} and checks if these beans are annotated with
	 * {@link CacheNameIdentifier}, if yes it extracts the {@code cacheName} param from the
	 * annotation and registers it against the class name in {@link CacheNameIdentifierRegistry}.
	 */
	public void registerCacheableBeans() {
		Map<String, Object> cacheableEntities =
				applicationContext.getBeansWithAnnotation(Entity.class);
		for (Map.Entry<String, Object> entrySet : cacheableEntities.entrySet()) {
			Class<?> entityClass = entrySet.getValue().getClass();
			if (entityClass.isAnnotationPresent(CacheNameIdentifier.class)) {
				String identifier =
						entityClass.getAnnotation(CacheNameIdentifier.class).cacheName();
				LOG.info("Class {}'s cache indentifier set to {}", entityClass.getName(),
						identifier);
				cacheHashkeyRegistery.set(entityClass, identifier);
			}
		}
	}
}
