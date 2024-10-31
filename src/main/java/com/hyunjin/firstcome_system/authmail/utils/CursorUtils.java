package com.hyunjin.firstcome_system.authmail.utils;

import java.util.List;
import java.util.function.ToIntFunction;

public final class CursorUtils {
    private static final int INITIAL_CURSOR = Integer.MAX_VALUE;

    private CursorUtils() {
    }

    public static int determineCursor(String cursor) {
        if (cursor == null) {
            return INITIAL_CURSOR;
        }
        return Integer.parseInt(cursor);
    }

    public static <T> String getNextCursor(int requestedSize, List<T> items, ToIntFunction<T> idExtractor) {
        if (items.size() != requestedSize) {
            return null;
        }

        return String.valueOf(idExtractor.applyAsInt(items.getLast()));
    }
}