package org.example.vehicles_rental.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimitService {

    private static final int MAX_REQUESTS = 5;
    private static final long TIME_WINDOW = 60_000; // 1 minute

    private final Map<String, RequestInfo> requests = new ConcurrentHashMap<>();

    public boolean isAllowed(String key) {

        long now = System.currentTimeMillis();

        RequestInfo info = requests.get(key);

        if (info == null) {
            requests.put(key, new RequestInfo(now, 1));
            return true;
        }

        // Reset after 1 minute
        if (now - info.timestamp >= TIME_WINDOW) {
            requests.put(key, new RequestInfo(now, 1));
            return true;
        }

        // Already reached 5 requests
        if (info.count >= MAX_REQUESTS) {
            return false;
        }

        info.count++;
        return true;
    }

    private static class RequestInfo {
        private final long timestamp;
        private int count;

        public RequestInfo(long timestamp, int count) {
            this.timestamp = timestamp;
            this.count = count;
        }
    }
}