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

    @DisplayName("6. post/student возвращает код 400, если имя не заполнено.")
    @Test
    @SneakyThrows
    public void AddingStudentNotName400() {
        Student student = new Student(12, null);
        RestAssured.defaultParser = Parser.JSON;
        given()
                .baseUri("http://localhost:8080/student/")
                .contentType(ContentType.JSON)
                .body(student)
                .log().all()
                .when().post().then()
                .statusCode(400)
                .log().all();
    }

    @DisplayName("3. post /student добавляет студента в базу, если студента с таким ID ранее не было, при этом имя заполнено, код 201")
    @Test
    @SneakyThrows
    public void CreationStudent() {
        Student student = new Student(222, "vasia1");
        RestAssured.defaultParser = Parser.JSON;
        given()
                .baseUri("http://localhost:8080/student/")
                .contentType(ContentType.JSON)
                .body(student)
                .log().all()
                .when().post().then()
                .statusCode(201)
                .body("id", notNullValue())
                .log().all();
    }// пустое тело ответа?

    @DisplayName("4. post /student обновляет студента в базе, если студент с таким ID ранее был, при этом имя заполнено, код 201")
    @Test
    @SneakyThrows
    public void updateExistingStudent() {
        Student student = new Student(22, "vasia");
        given()
                .baseUri("http://localhost:8080/student/")
                .contentType(ContentType.JSON)
                .body(student)
                .log().all()
                .when().post().then()
                .statusCode(201)
                .log().all();
    }

    @DisplayName("5. post /student добавляет студента в базу, если ID null, то возвращается назначенный ID, код 201")
    @Test
    @SneakyThrows
    public void CreationStudentIdNull() {
        Student student = new Student(null, "alex");

        given()
                .baseUri("http://localhost:8080/student/")
                .contentType(ContentType.JSON)
                .body(student)
                .log().all()
                .when().post().then()
                .statusCode(201)
                .body("id", notNullValue())
                .log().all()
                .extract().body().as(Student.class);
    }

    @DisplayName("1. get /student/{id} возвращает JSON студента с указанным ID и заполненным именем, если такой есть в базе, код 200")
    @Test
    public void GetStudent200() {
        int id = 22;
        String name = "vasia";
        given()
                .baseUri("http://localhost:8080/student/" + id)
                .contentType(ContentType.JSON)
                .log().all()
                .when().get().then()
                .statusCode(200)
                .body("id", Matchers.equalTo(id))
                .body("name", Matchers.equalTo(name))
                .log().all();
    }

    @DisplayName("2.get /student/{id} с кодом 404")
    @Test
    public void GetStudent404() {
        int id = -1;
        given()
                .baseUri("http://localhost:8080/student/" + id)
                .contentType(ContentType.JSON)
                .when().get().then()
                .statusCode(404);
    }

    @DisplayName("7. delete /student/{id} удаляет студента с указанным ID из базы, код 200.")
    @Test
    @SneakyThrows
    public void DeleteStudentId() {
        int id = 22;
        given()
                .baseUri("http://localhost:8080/delete/student/" + id)
                .contentType(ContentType.JSON)
                .log().all()
                .when().delete().then()
                .statusCode(200)
                .log().all();
    }

    @DisplayName("8. delete /student/{id} возвращает код 404, если студента с таким ID в базе нет")
    @Test
    @SneakyThrows
    public void DeleteNotExistingStudent() {
        int id = 45;
        given()
                .baseUri("http://localhost:8080/delete/student/" + id)
                .contentType(ContentType.JSON)
                .when().delete().then()
                .statusCode(404);
    }

    @DisplayName("9. get /topStudent код 200 и пустое тело, если студентов в базе нет")
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

    @DisplayName("11. get /topStudent код 200 и один студент, если у него максимальная средняя оценка, либо же среди всех студентов с максимальной средней у него их больше всего")
    @Test
    @SneakyThrows
    public void OneStudentMaxAverageMarks() {
        creatStudents();
        RestAssured.defaultParser = Parser.JSON;
        given()
                .baseUri("http://localhost:8080/topStudent/")
                .contentType(ContentType.JSON)
                .log().all()
                .when().get().then()
                .statusCode(200)
                .body("name", Matchers.equalTo("Ivan"))
                .body("grades.size()", is(5))
                .log().all();
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