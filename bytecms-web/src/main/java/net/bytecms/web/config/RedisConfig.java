package net.bytecms.web.config;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.*;

import java.time.Duration;

@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {

	/**
	   * redis模板，存储关键字是字符串，值是Jdk序列化
	   * @Description:
	   * @param  // factory RedisConnectionFactory factory
	   * @return
	   */
	  @Bean
	  public RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory factory) {
	      RedisTemplate<String,Object> redisTemplate = new RedisTemplate<String,Object>();
	      redisTemplate.setConnectionFactory(factory);
	      RedisSerializer<String> redisSerializer = new StringRedisSerializer();
	      redisTemplate.setKeySerializer(redisSerializer);
	      redisTemplate.setHashKeySerializer(redisSerializer);
	      //JdkSerializationRedisSerializer序列化方式;
	      // JdkSerializationRedisSerializer jdkRedisSerializer=new JdkSerializationRedisSerializer();
	      //2选一
	      //Jackson 序列化方式
	      Jackson2JsonRedisSerializer<Object> jacksonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
	      ObjectMapper objectMapper = new ObjectMapper();
	      objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
	      objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
	      jacksonRedisSerializer.setObjectMapper(objectMapper);
	      redisTemplate.setValueSerializer(jacksonRedisSerializer);
	      redisTemplate.setHashValueSerializer(jacksonRedisSerializer);
	      redisTemplate.afterPropertiesSet();
	      return redisTemplate; 
	  }


	/**
	 * 缓存管理器
	 */
	@Bean
	public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
//		//初始化一个RedisCacheWriter
//		RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory);
//		//设置CacheManager的值序列化方式为json序列化
//		RedisSerializer<Object> jsonSerializer = new GenericJackson2JsonRedisSerializer();
//		RedisSerializationContext.SerializationPair<Object> pair = RedisSerializationContext.SerializationPair
//		.fromSerializer(jsonSerializer);
//		RedisCacheConfiguration defaultCacheConfig=RedisCacheConfiguration.defaultCacheConfig().serializeValuesWith(pair);
//		//设置默认超过期时间是30秒
//		defaultCacheConfig.entryTtl(Duration.ofSeconds(30));
//		//初始化RedisCacheManager
//		return new RedisCacheManager(redisCacheWriter, defaultCacheConfig);

        RedisSerializer<Object> jsonSerializer = new GenericJackson2JsonRedisSerializer();
        RedisSerializationContext.SerializationPair<Object> pair = RedisSerializationContext.SerializationPair
        .fromSerializer(jsonSerializer);
		RedisCacheConfiguration redisCacheConfiguration=RedisCacheConfiguration.defaultCacheConfig().
        entryTtl(Duration.ofHours(3)).serializeValuesWith(pair);
		return RedisCacheManager.builder(RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory))
		.cacheDefaults(redisCacheConfiguration).build();

	}


}
