package com.key.password_manager.cache;

import java.time.Duration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import com.key.password_manager.utils.BaseModel;

@Configuration
@EnableCaching
public class CacheConfiguration {

	@Bean
	public LettuceConnectionFactory connectionFactory() {
		RedisStandaloneConfiguration redisStandaloneConfiguration =
				new RedisStandaloneConfiguration();
		redisStandaloneConfiguration.setHostName("localhost");
		redisStandaloneConfiguration.setPort(6379);
		return new LettuceConnectionFactory(redisStandaloneConfiguration);
	}

	@Bean
	@Primary
	public CacheManager redisCacheManager(LettuceConnectionFactory lettuceConnectionFactory) {
		Duration cacheDuration = Duration.ofHours(1L);
		RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration
				.defaultCacheConfig().disableCachingNullValues().entryTtl(cacheDuration)
				.serializeValuesWith(RedisSerializationContext.SerializationPair
						.fromSerializer(RedisSerializer.json()));
		redisCacheConfiguration.usePrefix();
		return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(connectionFactory())
				.cacheDefaults(redisCacheConfiguration).build();
	}

	@Bean
	public RedisTemplate<String, ? extends BaseModel> redisTemplate() {
		RedisTemplate<String, ? extends BaseModel> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(connectionFactory());
		redisTemplate.setEnableTransactionSupport(true);
		redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
		redisTemplate.setHashKeySerializer(new StringRedisSerializer());
		redisTemplate.setKeySerializer(new JdkSerializationRedisSerializer());
		return redisTemplate;
	}
}
