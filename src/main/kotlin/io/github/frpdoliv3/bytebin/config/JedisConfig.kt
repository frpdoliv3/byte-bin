package io.github.frpdoliv3.bytebin.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import redis.clients.jedis.JedisPooled
import redis.clients.jedis.commands.JedisBinaryCommands

@Configuration
class JedisConfig {
    @Bean
    fun createJedisClient(): JedisBinaryCommands = JedisPooled("localhost", 6379)
}
