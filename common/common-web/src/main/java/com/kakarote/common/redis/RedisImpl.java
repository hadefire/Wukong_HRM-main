/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Resource
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.data.redis.connection.DataType
 *  org.springframework.data.redis.core.RedisTemplate
 *  org.springframework.data.redis.core.ZSetOperations$TypedTuple
 *  org.springframework.stereotype.Service
 */
package com.kakarote.common.redis;

import com.kakarote.common.redis.Redis;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

@Service("commonRedisImpl")
public class RedisImpl
implements Redis {
    private static final Logger log = LoggerFactory.getLogger(RedisImpl.class);
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    private String appendKeyPrefix(Object key) {
        if (key instanceof String) {
            return (String)key;
        }
        return key.toString();
    }

    @Override
    public void del(Object ... keys) {
        if (keys == null || keys.length == 0) {
            return;
        }
        ArrayList<String> keysList = new ArrayList<String>();
        for (Object key : keys) {
            keysList.add(this.appendKeyPrefix(key));
        }
        this.redisTemplate.delete(keysList);
    }

    @Override
    public Long ttl(String key) {
        return this.redisTemplate.getExpire(this.appendKeyPrefix(key));
    }

    @Override
    public void expire(String key, Integer timeout) {
        this.redisTemplate.expire(this.appendKeyPrefix(key), (long)timeout.intValue(), TimeUnit.SECONDS);
    }

    @Override
    public void persist(String key) {
        this.redisTemplate.persist(this.appendKeyPrefix(key));
    }

    @Override
    public boolean exists(String key) {
        Boolean exists = this.redisTemplate.hasKey(this.appendKeyPrefix(key));
        return exists != null ? exists : false;
    }

    @Override
    public DataType getType(String key) {
        return this.redisTemplate.type(this.appendKeyPrefix(key));
    }

    @Override
    public void set(String key, Object value) {
        this.redisTemplate.opsForValue().set(this.appendKeyPrefix(key), value);
    }

    @Override
    public void setex(Object key, Integer second, Object value) {
        this.redisTemplate.opsForValue().set(this.appendKeyPrefix(key), value, (long)second.intValue(), TimeUnit.SECONDS);
    }

    @Override
    public boolean setNx(String key, Long timeout, Object value) {
        Boolean ifAbsent = this.redisTemplate.opsForValue().setIfAbsent(this.appendKeyPrefix(key), value, timeout.longValue(), TimeUnit.SECONDS);
        if (ifAbsent == null) {
            return false;
        }
        return ifAbsent;
    }

    @Override
    public <T> T get(String key) {
        return (T)this.redisTemplate.opsForValue().get(this.appendKeyPrefix(key));
    }

    @Override
    public Long incr(String key, Long value) {
        return this.redisTemplate.opsForValue().increment(this.appendKeyPrefix(key), value.longValue());
    }

    @Override
    public Long incr(String key) {
        return this.incr(key, 1L);
    }

    @Override
    public Long decr(String key, Long value) {
        return this.redisTemplate.opsForValue().decrement(this.appendKeyPrefix(key), value.longValue());
    }

    @Override
    public Long decr(String key) {
        return this.decr(key, 1L);
    }

    @Override
    public List<Object> mGet(Object ... keys) {
        ArrayList<String> keysList = new ArrayList<String>();
        for (Object key : keys) {
            keysList.add(this.appendKeyPrefix(key));
        }
        return this.redisTemplate.opsForValue().multiGet(keysList);
    }

    @Override
    public void mSet(Map<String, Object> map) {
        for (String key : map.keySet()) {
            Object value = map.remove(key);
            map.put(this.appendKeyPrefix(key), value);
        }
        this.redisTemplate.opsForValue().multiSet(map);
    }

    @Override
    public void mSetNx(Map<String, Object> map) {
        for (String key : map.keySet()) {
            Object value = map.remove(key);
            map.put(this.appendKeyPrefix(key), value);
        }
        this.redisTemplate.opsForValue().multiSetIfAbsent(map);
    }

    @Override
    public void appendStr(Object key, String value) {
        this.redisTemplate.opsForValue().append(this.appendKeyPrefix(key), value);
    }

    @Override
    public void hdel(Object key, Object ... hashKeys) {
        this.redisTemplate.opsForHash().delete(this.appendKeyPrefix(key), hashKeys);
    }

    @Override
    public void put(Object key, Object hashKey, Object value) {
        this.redisTemplate.opsForHash().put(this.appendKeyPrefix(key), hashKey, value);
    }

    @Override
    public void putAll(String key, Map<Object, Object> map) {
        this.redisTemplate.opsForHash().putAll(this.appendKeyPrefix(key), map);
    }

    @Override
    public Map<Object, Object> getRedisMap(String key) {
        return this.redisTemplate.opsForHash().entries(this.appendKeyPrefix(key));
    }

    @Override
    public List<Object> getValues(Object key) {
        return this.redisTemplate.opsForHash().values(this.appendKeyPrefix(key));
    }

    @Override
    public Boolean hashMapKey(Object key, String hashKey) {
        return this.redisTemplate.opsForHash().hasKey(this.appendKeyPrefix(key), (Object)hashKey);
    }

    @Override
    public void lpush(String key, Object value) {
        this.redisTemplate.opsForList().leftPush(this.appendKeyPrefix(key), value);
    }

    @Override
    public void rpush(String key, Object value) {
        this.redisTemplate.opsForList().rightPush(this.appendKeyPrefix(key), value);
    }

    @Override
    public <T> T lPop(String key) {
        return (T)this.redisTemplate.opsForList().leftPop(this.appendKeyPrefix(key));
    }

    @Override
    public <T> T rPop(String key) {
        return (T)this.redisTemplate.opsForList().rightPop(this.appendKeyPrefix(key));
    }

    @Override
    public <T> T getKeyIndex(String key, int index) {
        return (T)this.redisTemplate.opsForList().index(this.appendKeyPrefix(key), (long)index);
    }

    @Override
    public Long getLength(String key) {
        return this.redisTemplate.opsForList().size(this.appendKeyPrefix(key));
    }

    @Override
    public List<Object> range(String key, int start, int end) {
        return this.redisTemplate.opsForList().range(this.appendKeyPrefix(key), (long)start, (long)end);
    }

    @Override
    public void addSet(String key, Object ... values) {
        this.redisTemplate.opsForSet().add(this.appendKeyPrefix(key), values);
    }

    @Override
    public <T> T getSet(String key) {
        return (T)this.redisTemplate.opsForSet().pop(this.appendKeyPrefix(key));
    }

    @Override
    public Set<Object> getSets(String key) {
        return this.redisTemplate.opsForSet().members(this.appendKeyPrefix(key));
    }

    @Override
    public Long getSetsNum(String key) {
        return this.redisTemplate.opsForSet().size(this.appendKeyPrefix(key));
    }

    @Override
    public Set<Object> members(String key) {
        return this.redisTemplate.opsForSet().members(this.appendKeyPrefix(key));
    }

    @Override
    public void zadd(String key, Set<ZSetOperations.TypedTuple<Object>> tuples) {
        this.redisTemplate.opsForZSet().add(this.appendKeyPrefix(key), tuples);
    }

    @Override
    public Set<Object> reverseRange(String key, Double min, Double max) {
        return this.redisTemplate.opsForZSet().reverseRangeByScore(this.appendKeyPrefix(key), min.doubleValue(), max.doubleValue());
    }
}

