package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;

@Service
public class MathService {
    public long calculateFastSum() {
        long n = 1_000_000;
        return n * (n + 1) / 2;
    }
}