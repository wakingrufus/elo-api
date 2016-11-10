package com.github.wakingrufus.elo.auth;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Objects;

public class LoginRequest {

    private final String password;
    private final String email;

    @JsonCreator
    public LoginRequest(String password, String email) {
        this.password = password;
        this.email = email;
    }


    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "LoginRequest{" +
                "password='" + password + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoginRequest that = (LoginRequest) o;
        return Objects.equals(getPassword(), that.getPassword()) &&
                Objects.equals(getEmail(), that.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPassword(), getEmail());
    }
}
