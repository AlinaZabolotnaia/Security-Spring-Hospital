package org.main.services;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LoginAttemptService {

    private static final int MAX_ATTEMPTS = 5;
    private static final int LOCK_MINUTES = 15;

    private final Map<String, AttemptRecord> cache = new ConcurrentHashMap<>();

    public void success(String username) {
        cache.remove(username);
    }

    public void failure(String username) {
        cache.merge(username, new AttemptRecord(1, null), (existing, ignored) -> {
            // если старая блокировка истекла — сбрасываем счётчик
            if (existing.lockedUntil != null && LocalDateTime.now().isAfter(existing.lockedUntil)) {
                return new AttemptRecord(1, null);
            }
            // если уже заблокирован — не трогаем запись
            if (existing.lockedUntil != null) {
                return existing;
            }
            int newCount = existing.failCount + 1;
            LocalDateTime lock = newCount >= MAX_ATTEMPTS
                    ? LocalDateTime.now().plusMinutes(LOCK_MINUTES)
                    : null;
            return new AttemptRecord(newCount, lock);
        });
    }

    public boolean isBlocked(String username) {
        AttemptRecord record = cache.get(username);
        if (record == null || record.lockedUntil == null) return false;
        if (LocalDateTime.now().isAfter(record.lockedUntil)) {
            cache.remove(username);
            return false;
        }
        return true;
    }

    private static class AttemptRecord {
        final int failCount;
        final LocalDateTime lockedUntil;

        AttemptRecord(int failCount, LocalDateTime lockedUntil) {
            this.failCount = failCount;
            this.lockedUntil = lockedUntil;
        }
    }
}
