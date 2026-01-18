package ru.java;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import lombok.SneakyThrows;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.is;

@WireMockTest()
public class Tests {

    @DisplayName("post/student возвращает код 400, если имя не заполнено.")
    @Test
    @SneakyThrows
    public void AddingStudentNotName400() {
        Student student = new Student(12, null);
        RestAssured.defaultParser = Parser.JSON;
        given()
                .baseUri("http://localhost:8080/student/")
                .contentType(ContentType.JSON)
                .body(student)
                .when().post().then()
                .statusCode(400);
    }

    @DisplayName("post /student добавляет студента в базу, если студента с таким ID ранее не было, при этом имя заполнено, код 201")
    @Test
    @SneakyThrows
    public void CreationStudent() {
        Student student = new Student(22, "vasia");
        RestAssured.defaultParser = Parser.JSON;
        given()
                .baseUri("http://localhost:8080/student/")
                .contentType(ContentType.JSON)
                .body(student)
                .when().post().then()
                .statusCode(201)
                .body("id", notNullValue());
    }// пустое тело ответа?

    @DisplayName("post /student обновляет студента в базе, если студент с таким ID ранее был, при этом имя заполнено, код 201")
    @Test
    @SneakyThrows
    public void updateExistingStudent() {
        Student student = new Student(22, "vasia");
        given()
                .baseUri("http://localhost:8080/student/")
                .contentType(ContentType.JSON)
                .body(student)
                .when().post().then()
                .statusCode(201);
    }

    @DisplayName("post /student добавляет студента в базу, если ID null, то возвращается назначенный ID, код 201")
    @Test
    @SneakyThrows
    public void CreationStudentIdNull() {
        Student student = new Student(null, "alex");

        given()
                .baseUri("http://localhost:8080/student/")
                .contentType(ContentType.JSON)
                .body(student)
                .when().post().then()
                .statusCode(201)
                .body("id", notNullValue())
        .extract().body().as(Student.class);;
    }

    @DisplayName("get /student/{id} возвращает JSON студента с указанным ID и заполненным именем, если такой есть в базе, код 200")
    @Test
    public void GetStudent200() {
        int id = 22;
        String name = "vasia";
        given()
                .baseUri("http://localhost:8080/student/" + id)
                .contentType(ContentType.JSON)
                .when().get().then()
                .statusCode(200)
                .body("id", Matchers.equalTo(id))
                .body("name", Matchers.equalTo(name));
    }

    @DisplayName("get /student/{id} с кодом 404")
    @Test
    public void GetStudent404() {
        int id = -1;
        given()
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
        given()
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
        given()
                .baseUri("http://localhost:8080/delete/student/"+ id)
                .contentType(ContentType.JSON)
                .when().delete().then()
                .statusCode(404);
    }

    @DisplayName("get /topStudent код 200 и пустое тело, если студентов в базе нет")
    @Test
    @SneakyThrows
    public void GetTopStudentEmptyBodyNotStudenrts() {
        stubFor(get(urlEqualTo("/topStudent/"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("")));

        given()
                .baseUri("http://localhost:8080/topStudent/")
                .contentType(ContentType.JSON)
                .when().get().then()
                .statusCode(200)
                .body(Matchers.anyOf(Matchers.nullValue(), Matchers.equalTo("")));
    }

    @DisplayName("get /topStudent код 200 и один студент, если у него максимальная средняя оценка, либо же среди всех студентов с максимальной средней у него их больше всего")
    @Test
    @SneakyThrows
    public void OneStudentMaxAverageMarks() {
        creatStudents();
        RestAssured.defaultParser = Parser.JSON;
        given()
                .baseUri("http://localhost:8080/topStudent/")
                .contentType(ContentType.JSON)
                .when().get().then()
                .statusCode(200)
                .body("name", Matchers.equalTo("Ivan"))
                .body("grades.size()", is(5));
   }

    private void creatStudents() {
        given()
                .baseUri("http://localhost:8080/student/")
                .contentType(ContentType.JSON)
                .body("{\"name\": \"Ivan\", \"grades\": [5, 5, 5]}")
                .post();

        given()
                .baseUri("http://localhost:8080/student/")
                .contentType(ContentType.JSON)
                .body("{\"name\": \"Alex\", \"grades\": [4, 4, 4]}")
                .post();
    }
}