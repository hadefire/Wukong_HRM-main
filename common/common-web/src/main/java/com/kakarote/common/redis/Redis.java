/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.springframework.data.redis.connection.DataType
 *  org.springframework.data.redis.core.ZSetOperations$TypedTuple
 */
package com.kakarote.common.redis;

import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.ZSetOperations;

public interface Redis {
    public void del(Object ... var1);

    public Long ttl(String var1);

    public void expire(String var1, Integer var2);

    public void persist(String var1);

    public boolean exists(String var1);

    public DataType getType(String var1);

    public void set(String var1, Object var2);

    public void setex(Object var1, Integer var2, Object var3);

    public boolean setNx(String var1, Long var2, Object var3);

    public <T> T get(String var1);

    public Long incr(String var1, Long var2);

    public Long incr(String var1);

    public Long decr(String var1, Long var2);

    public Long decr(String var1);

    public List<Object> mGet(Object ... var1);

    public void mSet(Map<String, Object> var1);

    public void mSetNx(Map<String, Object> var1);

    public void appendStr(Object var1, String var2);

    public void hdel(Object var1, Object ... var2);

    public void put(Object var1, Object var2, Object var3);

    public void putAll(String var1, Map<Object, Object> var2);

    public Map<Object, Object> getRedisMap(String var1);

    public List<Object> getValues(Object var1);

    public Boolean hashMapKey(Object var1, String var2);

    public void lpush(String var1, Object var2);

    public void rpush(String var1, Object var2);

    public <T> T lPop(String var1);

    public <T> T rPop(String var1);

    public <T> T getKeyIndex(String var1, int var2);

    public Long getLength(String var1);

    public List<Object> range(String var1, int var2, int var3);

    public void addSet(String var1, Object ... var2);

    public <T> T getSet(String var1);

    public Set<Object> getSets(String var1);

    public Long getSetsNum(String var1);

    public Set<Object> members(String var1);

    public void zadd(String var1, Set<ZSetOperations.TypedTuple<Object>> var2);

    public Set<Object> reverseRange(String var1, Double var2, Double var3);
}

