package ru.java;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Student {
    @Getter @Setter
    private Integer id;
    @Getter @Setter
    private String name;
    private List<Integer> grades= new ArrayList<>();

    public Student(String name) {
        this.name = name;
        this.grades = new ArrayList<>();
    }
    public Student(String name, List<Integer> grades) {
        this.name = name;
        if (grades != null) {
            this.grades = new ArrayList<>(grades);
        }
    }

    public Student(Integer id, String name) {
        this.id = id;
        this.name = name;
        this.grades = new ArrayList<>();
    }

    public List<Integer> getGrades() {
        return Collections.unmodifiableList(grades);
    }

    public void addGrade(int grade) {
        if (grade < 2 || grade > 5) {
            throw new IllegalArgumentException(grade + " is wrong grade");
        }
        grades.add(grade);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + Objects.hashCode(this.name);
        hash = 13 * hash + Objects.hashCode(this.grades);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Student other = (Student) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return Objects.equals(this.grades, other.grades);
    }

    @Override
    public String toString() {
        return "Student{" + "name=" + name + ", marks=" + grades + '}';
    }
}
