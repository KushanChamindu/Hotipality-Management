package com.mbpackageservice.utils;

import java.util.ArrayList;
import java.util.List;

public class Tools {
    public static <T> List<List<T>> chunkArrayList(List<T> list, int chunkSize) {
        List<List<T>> chunks = new ArrayList<>();

        for (int i = 0; i < list.size(); i += chunkSize) {
            int end = Math.min(list.size(), i + chunkSize);
            List<T> chunk = new ArrayList<>(list.subList(i, end));
            chunks.add(chunk);
        }

        return chunks;
    }
}
