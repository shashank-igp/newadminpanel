package com.igp.config.instance;

import com.igp.config.RedisProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 * Scenario:
 * thread 1 calls getInstance(), thread 2 calls destroyJedis --
 * thread 1 gets schediled out right after createJedisPool(),
 * then thread 2 destroys it,
 * and thread 1 continues,
 * but now jedisPool is destroyed.
 * Solutions:
 * Easy solution would be to synchoronize all the public methods
 * With a synchronized block locking onto some private data. Don't just add "synchronized" to the method signature.
 * There was an uncorrectable issue that revolved around adding a synchronized keyword to a method signature,
 * as it locked onto the class itself or some such, and there were potential issues doing it.
 * A synchronized method is equivalent to synchronized (this) usually you don't want to expose the lock object
 * well if you synchronize on this and use the enum as a hashmap key you lose biased locking.
 * Biased locking occupies the same slot in the object header and the identity hashcode,
 * so if you call hashcode ones, you lose biased locking opportunity.
 * With the public lock there may have been a way for something to unintentionally "steal" it and cause a deadlock
 * which is why you shouldn't expose your lock
 * Using identityHashCode for globally ordering two locks (using synchronized on the same object) to avoid deadlocks.
 * Solution: public whatever myMethod (args) {
 * synchronized(privateFieldOnlyUsedForLocking)
 * { /* all method code goes here
 */

/**
 * } }
 * for the methods that interface with your singleton field.
 * You have to define the privateFieldOnlyUsed... thing yourself.
 * Since you can't lock on null, it needs to be a different field from the singleton one.
 */

public enum Redis {
    INSTANCE;

    private static final Logger logger = LoggerFactory.getLogger(Redis.class);

    private JedisPoolConfig jedisPoolConfig = null;
    private JedisPool jedisPool = null;
    private final Object lock = new Object();

    private JedisPool createJedisPool() throws Exception {
        try {
            if (jedisPool == null) {
                if (jedisPoolConfig == null) {
                    jedisPoolConfig = new JedisPoolConfig();
                    jedisPoolConfig.setMinIdle(100);
                    jedisPoolConfig.setMaxIdle(200);
                    jedisPoolConfig.setMaxTotal(1000);
                    jedisPoolConfig.setMaxWaitMillis(5000);
                }
                String host = RedisProperties.getRedisURI();
                Integer port = RedisProperties.getRedisPort();
                Integer timeout = 100000;
                jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout);
            }
        } catch (Exception exception) {
            logger.error("Error", exception);
        }
        return jedisPool;
    }

    public Jedis getInstance() throws Exception {
        try {
            synchronized (lock) {
                Jedis jedis = createJedisPool().getResource();
                if (!jedis.isConnected()) {
                    jedis.connect();
                }
                return jedis;
            }
        } catch (JedisConnectionException jedisConnectionException) {
            logger.error("Jedis connection error", jedisConnectionException);
            throw new RuntimeException(jedisConnectionException);
        }
    }

    public void releaseResource(Jedis jedis) {
        try {
            synchronized (lock) {
                if (jedisPool != null && jedis != null) {
                    if (jedis.isConnected()) {
                        jedis.disconnect();
                    }
                    jedis.close();
                }
            }
        } catch (Exception exception) {
            logger.error("Error", exception);
            throw new RuntimeException(exception);
        }
    }

    public void destroyJedis() {
        synchronized (lock) {
            try {
                if (jedisPool != null) {
                    jedisPool.destroy();
                }
            } catch (Exception exception) {
                logger.error("Error", exception);
                throw new RuntimeException(exception);
            } finally {
                jedisPool = null;
            }
        }
    }
}
