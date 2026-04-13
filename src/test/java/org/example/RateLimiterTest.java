package org.example;

import org.example.RateLimiter.RateLimiter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RateLimiterTest {
    private Clock fixedClock;

    @BeforeEach
    void setUp(){
        fixedClock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
    }

    @Test
    void shouldAllowRequestsUpToLimit(){

        RateLimiter rateLimiter = new RateLimiter(5000L, fixedClock);
        rateLimiter.configureLimit("Test", 3);

        assertTrue(rateLimiter.allowRequest("Test"));
        assertTrue(rateLimiter.allowRequest("Test"));
        assertTrue(rateLimiter.allowRequest("Test"));

        assertFalse(rateLimiter.allowRequest("Test"));
    }

    @Test
    void shouldResetAfterWindow() {
        Clock baseClock = Clock.fixed(Instant.now(), ZoneId.systemDefault());

        RateLimiter limiter = new RateLimiter(100, baseClock);
        limiter.configureLimit("Test", 2);

        assertTrue(limiter.allowRequest("Test"));
        assertTrue(limiter.allowRequest("Test"));
        assertFalse(limiter.allowRequest("Test"));

        // двигаем время
        Clock movedClock = Clock.offset(baseClock, Duration.ofMillis(150));
        limiter.setClock(movedClock);

        assertTrue(limiter.allowRequest("Test"));
    }

    @Test
    void shouldWorkIndependentlyForDifferentKeys() {
        RateLimiter limiter = new RateLimiter(5000L, fixedClock);

        limiter.configureLimit("A", 1);
        limiter.configureLimit("B", 1);

        assertTrue(limiter.allowRequest("A"));
        assertFalse(limiter.allowRequest("A"));

        // другой ключ не должен страдать
        assertTrue(limiter.allowRequest("B"));
    }


}
