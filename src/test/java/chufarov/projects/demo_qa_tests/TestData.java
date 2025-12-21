package chufarov.projects.demo_qa_tests;

import chufarov.projects.models.request.AuthRequestDemoQa;

public class TestData {

    public static final String LOGIN_USER = "Vegas";
    public static final String PASSWORD_USER = "Fifa1995new!";

    public static AuthRequestDemoQa getValidAuthRequest() {

        AuthRequestDemoQa authRequestDemoQa = new AuthRequestDemoQa();
        authRequestDemoQa.setUserName(LOGIN_USER);
        authRequestDemoQa.setPassword(PASSWORD_USER);

        return authRequestDemoQa;
    }
}
