package com.bluemoon.bluemoonedtech.security.ratelimit;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * In-memory token-bucket limiter, keyed per (endpoint + client identity).
 * Single-instance only: if this app ever runs behind multiple nodes, swap the
 * backing map for Bucket4j's Redis/Hazelcast proxy manager so all nodes share state.
 */
@Service
public class RateLimiterService {

    private final ConcurrentMap<String, Bucket> buckets = new ConcurrentHashMap<>();

    public boolean tryConsume(String key, int capacity, Duration refillPeriod) {
        Bucket bucket = buckets.computeIfAbsent(key, k -> newBucket(capacity, refillPeriod));
        return bucket.tryConsume(1);
    }

    private Bucket newBucket(int capacity, Duration refillPeriod) {
        Bandwidth limit = Bandwidth.classic(capacity, Refill.greedy(capacity, refillPeriod));
        return Bucket.builder().addLimit(limit).build();
    }
}
