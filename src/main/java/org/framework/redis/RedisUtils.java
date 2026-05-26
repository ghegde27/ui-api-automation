package org.framework.redis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.framework.utils.LogManager;
import org.slf4j.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.Map;
import java.util.Set;

public final class RedisUtils {

    private static final Logger LOG = LogManager.getLogger(RedisUtils.class);
    private static volatile JedisPool jedisPool;

    private RedisUtils() {
        // utility class
    }

    public static String get(String key) {
        try (Jedis jedis = getResource()) {
            return jedis.get(key);
        }
    }

    public static void set(String key, String value) {
        try (Jedis jedis = getResource()) {
            jedis.set(key, value);
        }
    }

    public static void set(String key, String value, long expirySeconds) {
        try (Jedis jedis = getResource()) {
            jedis.setex(key, expirySeconds, value);
        }
    }

    public static boolean exists(String key) {
        try (Jedis jedis = getResource()) {
            return jedis.exists(key);
        }
    }

    public static long delete(String... keys) {
        try (Jedis jedis = getResource()) {
            return jedis.del(keys);
        }
    }

    public static long ttl(String key) {
        try (Jedis jedis = getResource()) {
            return jedis.ttl(key);
        }
    }

    public static Set<String> keys(String pattern) {
        try (Jedis jedis = getResource()) {
            return jedis.keys(pattern);
        }
    }

    public static void hset(String key, String field, String value) {
        try (Jedis jedis = getResource()) {
            jedis.hset(key, field, value);
        }
    }

    public static String hget(String key, String field) {
        try (Jedis jedis = getResource()) {
            return jedis.hget(key, field);
        }
    }

    public static Map<String, String> hgetAll(String key) {
        try (Jedis jedis = getResource()) {
            return jedis.hgetAll(key);
        }
    }

    public static void lpush(String key, String... values) {
        try (Jedis jedis = getResource()) {
            jedis.lpush(key, values);
        }
    }

    public static List<String> lrange(String key, long start, long stop) {
        try (Jedis jedis = getResource()) {
            return jedis.lrange(key, start, stop);
        }
    }

    public static boolean isConnectionAvailable() {
        try (Jedis jedis = getResource()) {
            return "PONG".equalsIgnoreCase(jedis.ping());
        } catch (Exception e) {
            LOG.warn("Redis connection check failed", e);
            return false;
        }
    }

    public static void closePool() {
        JedisPool pool = jedisPool;
        if (pool != null) {
            pool.close();
            jedisPool = null;
        }
    }

    private static Jedis getResource() {
        return getPool().getResource();
    }

    private static JedisPool getPool() {
        if (jedisPool == null) {
            synchronized (RedisUtils.class) {
                if (jedisPool == null) {
                    jedisPool = createPool();
                }
            }
        }
        return jedisPool;
    }

    private static JedisPool createPool() {
        GenericObjectPoolConfig<Jedis> poolConfig = new GenericObjectPoolConfig<>();
        poolConfig.setMaxTotal(RedisConfig.maxTotal());
        poolConfig.setMaxIdle(RedisConfig.maxIdle());
        poolConfig.setMinIdle(RedisConfig.minIdle());

        String password = RedisConfig.password().isBlank()
                ? null
                : RedisConfig.password();

        return new JedisPool(
                poolConfig,
                RedisConfig.host(),
                RedisConfig.port(),
                RedisConfig.connectionTimeoutMillis(),
                RedisConfig.socketTimeoutMillis(),
                password,
                RedisConfig.database(),
                null
        );
    }
}
