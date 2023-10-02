package ru.job4j.grabber.utils;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

class HabrCareerDateTimeParserTest {

    @Test
    void whenParseWasCorrect() {
        HabrCareerDateTimeParser parser = new HabrCareerDateTimeParser();
        String input = "2023-09-25T12:47:08+03:00";
        LocalDateTime expectedDate = LocalDateTime.parse(input, DateTimeFormatter.ISO_DATE_TIME);
        LocalDateTime resultDate = parser.parse(input);
        assertThat(resultDate).isEqualTo(expectedDate);
    }
}