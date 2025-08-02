package com.lab.basic.conditional

import org.springframework.boot.autoconfigure.condition.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component

@Configuration
class BeanConditionalConfig {

    @Bean
    @ConditionalOnMissingBean(DatabaseService::class)
    fun defaultDatabaseService(): DatabaseService {
        return H2DatabaseService()
    }

    @Bean
    @ConditionalOnBean(DatabaseService::class)
    fun dataRepository(databaseService: DatabaseService): DataRepository {
        return DataRepositoryImpl(databaseService)
    }

    @Bean
    @ConditionalOnMissingBean(name = ["customUserService"])
    fun defaultUserService(): UserService {
        return DefaultUserService()
    }

    @Bean("customUserService")
    @ConditionalOnProperty(name = ["user.service.custom"], havingValue = "true")
    fun customUserService(): UserService {
        return CustomUserService()
    }

    @Bean
    @ConditionalOnBean(UserService::class)
    fun userController(userService: UserService): UserController {
        return UserController(userService)
    }

    @Bean
    @ConditionalOnClass(name = ["org.springframework.data.redis.core.RedisTemplate"])
    fun redisBasedService(): RedisBasedService? {
        return try {
            RedisBasedServiceImpl()
        } catch (e: Exception) {
            null
        }
    }

    @Bean
    @ConditionalOnMissingClass("org.springframework.data.redis.core.RedisTemplate")
    fun inMemoryBasedService(): InMemoryBasedService {
        return InMemoryBasedServiceImpl()
    }

    @Bean
    @ConditionalOnBean(RedisBasedService::class)
    fun distributedCacheService(redisBasedService: RedisBasedService): CacheService {
        return DistributedCacheService(redisBasedService)
    }

    @Bean
    @ConditionalOnMissingBean(CacheService::class)
    fun localCacheService(): CacheService {
        return LocalCacheService()
    }
}

@Component
@ConditionalOnProperty(name = ["database.type"], havingValue = "mysql")
class MySQLDatabaseService : DatabaseService {
    override fun connect(): String = "Connected to MySQL"
    override fun query(sql: String): List<String> = listOf("MySQL result for: $sql")
}

interface DatabaseService {
    fun connect(): String
    fun query(sql: String): List<String>
}

class H2DatabaseService : DatabaseService {
    override fun connect(): String = "Connected to H2"
    override fun query(sql: String): List<String> = listOf("H2 result for: $sql")
}

interface DataRepository {
    fun findAll(): List<String>
    fun save(data: String)
}

class DataRepositoryImpl(private val databaseService: DatabaseService) : DataRepository {
    override fun findAll(): List<String> {
        databaseService.connect()
        return databaseService.query("SELECT * FROM data")
    }

    override fun save(data: String) {
        databaseService.connect()
        println("Saving data: $data")
    }
}

interface UserService {
    fun findUser(id: String): User
    fun createUser(user: User): User
}

data class User(val id: String, val name: String, val email: String)

class DefaultUserService : UserService {
    override fun findUser(id: String): User {
        return User(id, "Default User", "default@example.com")
    }

    override fun createUser(user: User): User {
        println("Creating user with default service: ${user.name}")
        return user
    }
}

class CustomUserService : UserService {
    override fun findUser(id: String): User {
        return User(id, "Custom User", "custom@example.com")
    }

    override fun createUser(user: User): User {
        println("Creating user with custom service: ${user.name}")
        return user.copy(email = "${user.name}@custom.com")
    }
}

class UserController(private val userService: UserService) {
    fun getUser(id: String): User = userService.findUser(id)
    fun createUser(user: User): User = userService.createUser(user)
}

interface RedisBasedService {
    fun set(key: String, value: String)
    fun get(key: String): String?
}

class RedisBasedServiceImpl : RedisBasedService {
    override fun set(key: String, value: String) {
        println("Redis SET: $key = $value")
    }

    override fun get(key: String): String? {
        println("Redis GET: $key")
        return "redis-value-$key"
    }
}

interface InMemoryBasedService {
    fun store(key: String, value: String)
    fun retrieve(key: String): String?
}

class InMemoryBasedServiceImpl : InMemoryBasedService {
    private val storage = mutableMapOf<String, String>()

    override fun store(key: String, value: String) {
        storage[key] = value
        println("InMemory STORE: $key = $value")
    }

    override fun retrieve(key: String): String? {
        val value = storage[key]
        println("InMemory RETRIEVE: $key = $value")
        return value
    }
}

interface CacheService {
    fun cache(key: String, value: String)
    fun getCached(key: String): String?
}

class DistributedCacheService(private val redisBasedService: RedisBasedService) : CacheService {
    override fun cache(key: String, value: String) {
        redisBasedService.set(key, value)
    }

    override fun getCached(key: String): String? {
        return redisBasedService.get(key)
    }
}

class LocalCacheService : CacheService {
    private val cache = mutableMapOf<String, String>()

    override fun cache(key: String, value: String) {
        cache[key] = value
        println("Local cache: $key = $value")
    }

    override fun getCached(key: String): String? {
        return cache[key]
    }
}