package com.mjc.school.repository.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public final class Utils {
    private Utils() {
    }

    public static String getRandomContentByFilePath(String fileName) {
        String resultLine;
        Random r = new Random();
        int numLines = 30;
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream(fileName);
            InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(streamReader);
            int desiredLine = r.nextInt(numLines);
            int lineCtr = 0;
            while ((resultLine = reader.readLine()) != null) {
                if (lineCtr == desiredLine) {
                    break;
                }
                lineCtr++;
            }
            return resultLine;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static LocalDateTime getRandomDate() {
        Random random = new Random();
        int endDay = 30;
        LocalDate day = LocalDate.now().plusDays(random.nextInt(endDay));
        int hour = random.nextInt(24);
        int minute = random.nextInt(60);
        int second = random.nextInt(60);
        LocalTime time = LocalTime.of(hour, minute, second);
        return LocalDateTime.of(day, time);
    }
}
