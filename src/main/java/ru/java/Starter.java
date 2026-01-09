package ru.java;

public class Starter
{
    public static void main( String[] args ) {
        Student student=new Student("Les");
        student.addGrade(33);
        System.out.println(student);
    }
}
