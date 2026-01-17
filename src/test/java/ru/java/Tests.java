package ru.java;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import lombok.SneakyThrows;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class Tests {

    @DisplayName("post/student возвращает код 400, если имя не заполнено.")
    @Test
    @SneakyThrows
    public void AddingStudentNotName400() {
        Student student = new Student(12, null);
        RestAssured.given()
                .baseUri("http://localhost:8080/student/")
                .contentType(ContentType.JSON)
                .body(student)
                .when().post().then()
                .statusCode(400)
                .body("name", Matchers.nullValue());
    }

    @DisplayName("post /student добавляет студента в базу, если студента с таким ID ранее не было, при этом имя заполнено, код 201")
    @Test
    @SneakyThrows
    public void CreationStudent() {
        Student student = new Student(22, "vasia");
        RestAssured.given()
                .baseUri("http://localhost:8080/student/")
                .contentType(ContentType.JSON)
                .body(student)
                .when().post().then()
                .statusCode(201)
                .body("name", Matchers.equalTo("vasia"))
                .body("id", Matchers.equalTo(22));
    }

    @DisplayName("post /student обновляет студента в базе, если студент с таким ID ранее был, при этом имя заполнено, код 201")
    @Test
    @SneakyThrows
    public void updateExistingStudent() {
        Student student = new Student(22, "vasia_updated");
        RestAssured.given()
                .baseUri("http://localhost:8080/student/")
                .contentType(ContentType.JSON)
                .body(student)
                .when().post().then()
                .statusCode(201)
                .body("name", Matchers.equalTo("vasia_updated"));
    }

    @DisplayName("post /student добавляет студента в базу, если ID null, то возвращается назначенный ID, код 201")
    @Test
    @SneakyThrows
    public void CreationStudentIdNull() {
        Student student = new Student(0, "alex");
        RestAssured.given()
                .baseUri("http://localhost:8080/student/")
                .contentType(ContentType.JSON)
                .body(student)
                .when().post().then()
                .statusCode(201)
                .body("name", Matchers.equalTo("alex"))
                .body("id", Matchers.notNullValue());
    }

    @DisplayName("get /student/{id} возвращает JSON студента с указанным ID и заполненным именем, если такой есть в базе, код 200")
    @Test
    public void GetStudent200() {
        int id = 22;
        String name = "vasia";
        RestAssured.given()
                .baseUri("http://localhost:8080/student/" + id)
                .contentType(ContentType.JSON)
                .when().get().then()
                .statusCode(200)
                .body("id", Matchers.equalTo(id))
                .body("name", Matchers.equalTo(name));
    }

    @DisplayName("get /student/{id} с кодом 200,оценки есть")
    @Test
    public void GetStudent200withMarks() {
        int id = 2;
        String name = "sea";
        List<Integer> marks = Arrays.asList(5, 4);
        RestAssured.given()
                .baseUri("http://localhost:8080/student/" + id)
                .contentType(ContentType.JSON)
                .when().get().then()
                .statusCode(200)
                .body("id", Matchers.equalTo(id))
                .body("marks", Matchers.equalTo(marks))
                .body("name", Matchers.equalTo(name));
    }

    @DisplayName("get /student/{id} с кодом 404")
    @Test
    public void GetStudent404() {
        int id = -1;
        RestAssured.given()
                .baseUri("http://localhost:8080/student/" + id)
                .contentType(ContentType.JSON)
                .when().get().then()
                .statusCode(404);
    }

    @DisplayName("delete /student/{id} удаляет студента с указанным ID из базы, код 200.")
    @Test
    @SneakyThrows
    public void DeleteStudentId() {
        int id = 22;
        RestAssured.given()
                .baseUri("http://localhost:8080/delete/student/"+ id)
                .contentType(ContentType.JSON)
                .when().delete().then()
                .statusCode(200);
    }

    @DisplayName("delete /student/{id} возвращает код 404, если студента с таким ID в базе нет")
    @Test
    @SneakyThrows
    public void DeleteNotExistingStudent() {
        int id = 45;
        RestAssured.given()
                .baseUri("http://localhost:8080/delete/student/"+ id)
                .contentType(ContentType.JSON)
                .when().delete().then()
                .statusCode(404);
    }

    @DisplayName("get /topStudent код 200 и пустое тело, если студентов в базе нет")
    @Test
    @SneakyThrows
    public void DeleteStudentEmptyBodyNotStudents() {
        RestAssured.given()
                .baseUri("http://localhost:8080/topStudent/")
                .contentType(ContentType.JSON)
                .when().get().then()
                .statusCode(200)
                .body(Matchers.anyOf(Matchers.nullValue(), Matchers.equalTo("")));
    }
}