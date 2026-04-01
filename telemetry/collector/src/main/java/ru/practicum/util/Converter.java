package ru.practicum.util;

import java.time.Instant;

public class Converter {
    public static Instant timestampToInstant(com.google.protobuf.Timestamp timestamp) {
        return Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos());
    }
}