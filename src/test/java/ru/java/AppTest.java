package ru.java;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;


public class AppTest  {
    @DisplayName("Геттер должен возвращать неизменяемый список!")
    @Test
    void testEncapsulationGrades() {
        Student student = new Student("vasia", Arrays.asList(3,4));
       List<Integer> grades = student.getGrades();
        assertThrows(UnsupportedOperationException.class, () -> {
            grades.add(3);
        });
    }

    @DisplayName("Изменение списка не влияет на оценки внутри студента! Оценки не изменились")
    @Test
    void testEncapsulationGradesCopy() {
        List<Integer> grades =  new ArrayList<>();
        grades.add(3);
        grades.add(4);
        Student student = new Student("Иван", grades);
        grades.add(2);
        assertEquals(2, student.getGrades().size(),
                "Изменение внешнего списка не влияют на оценки внутри студента!");
                assertEquals(Arrays.asList(3, 4), student.getGrades(),
                        "Оценки не изменились");
    }

    @DisplayName("Добавление валидных оценок")
    @ParameterizedTest(name="добавление валидных оценок")
    @MethodSource("getValidGrades")
    public void parametrizedNumberTest(Integer expected, Integer actual) {
        Student student=new Student("vasia");
        student.addGrade(actual);
        Integer result = student.getGrades().get(0);
        assertEquals(expected, result, "Ожидаемый результат равен фактическому");
    }

    public static Stream<Arguments> getValidGrades(){
        return Stream.of(
                Arguments.of(2, 2),
                Arguments.of(4,4),
        Arguments.of(5,5));
    }

    @DisplayName("Добавление НЕвалидных оценок")
    @ParameterizedTest(name="добавление НЕвалидных оценок")
    @MethodSource("getInvalidGrades")
    public void parametrizedNumberTestObject(Integer invalidGrade) {
        Student student=new Student("vasia");
        assertThrows(IllegalArgumentException.class, () -> {
            student.addGrade(invalidGrade);
        }, "Метод должен выбрасывать Exception");
    }

    public static Stream<Integer> getInvalidGrades(){
        return Stream.of(-1,0,1,6);
    }

    @Test
    @DisplayName("Проверка установки и получения имени")
    void testSetNameAndGetName() {
        Student student = new Student("vasia");
        student.setName("Ivan");
        assertEquals("Ivan", student.getName(), "Имя Ivan");
    }

    @Test
    @DisplayName("Проверка формата toString")
    public void testToStringFormat() {
        Student student = new Student("vasia", Arrays.asList(3,4));
        String result = "Student{name=vasia, marks=[3, 4]}";
        assertEquals(result, student.toString());
    }

    @Test
    @DisplayName("Сравнение студента с самим собой")
    public void testEqualStudent() {
        Student student = new Student("vasia", Arrays.asList(3,4));
        assertEquals(student, student);
    }

    @Test
    @DisplayName("Сравнение студента с null")
    public void testEqualStudentNull() {
        Student student = new Student("vasia", Arrays.asList(3,4));
        assertNotEquals(null, student);
    }

    @Test
    @DisplayName("Сравнение студента с объектом другого класса")
    public void testEqualStudentObject() {
        Student student = new Student("vasia", Arrays.asList(3,4));
        String result = "Student{name=vasia, marks=[3, 4]}";
        assertNotEquals(result, student);
    }
    @Test
    @DisplayName("Сравнение поля name студента ")
    public void testEqualStudentName() {
        Student student = new Student("vasia");
        Student student1 = new Student("ivan");
        assertNotEquals(student, student1);
    }

    @Test
    @DisplayName("Сравнение оценок студента c одним именем ")
    public void testEqualDifferentGrades() {
        Student student = new Student("vasia",Arrays.asList(3,4) );
        Student student1 = new Student("vasia", Arrays.asList(2,4));
        assertNotEquals(student, student1);
    }
    @Test
    @DisplayName("Сравнение одинаковых оценок студента ")
    public void testEqualSameGrades() {
        Student student = new Student("vasia",Arrays.asList(3,4));
        Student student1 = new Student("vasia", Arrays.asList(3,4));
        assertEquals(student, student1);
    }

    @Test
    @DisplayName("Сравнение hashCode")
    void testHashCodeEquality() {
        Student student1 = new Student("vasia",Arrays.asList(3,4));
        Student student2 = new Student("vasia",Arrays.asList(3,4));
        assertEquals(student1, student2);
        assertEquals(student1.hashCode(), student2.hashCode());
    }
    }