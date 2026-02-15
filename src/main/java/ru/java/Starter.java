package ru.java;

import java.util.Arrays;

public class Starter
{
    public static void main( String[] args ) {
        Student student = new Student("Michail", Arrays.asList(5, 5, 5, 5));
        System.out.println(student);
    }
}
