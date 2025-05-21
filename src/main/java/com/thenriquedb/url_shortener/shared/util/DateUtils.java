package com.thenriquedb.url_shortener.shared.util;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class DateUtils {
    public static long calculateDifferenceInSeconds(LocalDateTime expireAt) {
        return ChronoUnit.SECONDS.between(
                LocalDateTime.now(),
                expireAt != null ? expireAt : LocalDateTime.now().plusDays(1)
        );
    }
}
