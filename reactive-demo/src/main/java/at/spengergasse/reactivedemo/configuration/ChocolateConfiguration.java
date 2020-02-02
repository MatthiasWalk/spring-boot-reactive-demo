package at.spengergasse.reactivedemo.configuration;


import at.spengergasse.reactivedemo.models.Chocolate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class ChocolateConfiguration {

    @Bean
    ReactiveRedisOperations<String, Chocolate> redisOperations(ReactiveRedisConnectionFactory factory) {
        Jackson2JsonRedisSerializer<Chocolate> serializer = new Jackson2JsonRedisSerializer<>(Chocolate.class);

        RedisSerializationContext.RedisSerializationContextBuilder<String, Chocolate> builder =
                RedisSerializationContext.newSerializationContext(new StringRedisSerializer());

        RedisSerializationContext<String, Chocolate> context = builder.value(serializer).build();

        return new ReactiveRedisTemplate<>(factory, context);
    }
}
