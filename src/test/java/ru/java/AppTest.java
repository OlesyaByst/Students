package ru.java;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

@WireMockTest(httpPort = 5352)
public class AppTest {

    private Student student;

    @BeforeEach
    void setUp() {
        student = new Student("vasia");
    }

    @DisplayName("Проверка НЕвалидной оценки")
    @Test
    void testAddInCorrectGrade() {
        stubFor(get(urlEqualTo("/checkGrade?grade=6"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("false")));

        assertThrows(IllegalArgumentException.class, () -> {
            student.addGrade(6);
        });

        assertTrue(student.getGrades().isEmpty());
    }

    @DisplayName("Проверка валидной оценки")
    @Test
    void testAddCorrectGrade() {
        stubFor(get(urlEqualTo("/checkGrade?grade=5"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("true")));

        student.addGrade(5);
        assertEquals(1, student.getGrades().size());
        assertEquals(5, student.getGrades().get(0));
    }
}