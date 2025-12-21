package chufarov.projects.api_classes;

import chufarov.projects.models.request.AuthRequestDemoQa;
import chufarov.projects.models.response.AuthResponseDemoQa;
import io.restassured.http.ContentType;

import static chufarov.projects.specs.ApiSpecs.requestSpecDemoQa;
import static io.restassured.RestAssured.given;

public class AccountApiDemoQa {

    public AuthResponseDemoQa login(String userName, String password) {

        AuthRequestDemoQa authRequestDemoQa = new AuthRequestDemoQa();
        authRequestDemoQa.setUserName(userName);
        authRequestDemoQa.setPassword(password);

        return given(requestSpecDemoQa)
                .contentType(ContentType.JSON)
                .body(authRequestDemoQa)
                .when()
                .post("/Account/v1/Login")
                .then()
                .statusCode(200)
                .extract()
                .as(AuthResponseDemoQa.class);
    }
}
