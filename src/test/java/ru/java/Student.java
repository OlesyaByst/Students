package ru.java;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class Student {

    Integer id;
    String name;
    List<Integer> marks= new ArrayList<>();


    public Student(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

}