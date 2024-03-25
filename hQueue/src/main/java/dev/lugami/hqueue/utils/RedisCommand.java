package dev.lugami.hqueue.utils;

import redis.clients.jedis.Jedis;

public interface RedisCommand<T> {
    public T execute(Jedis var1);
}

