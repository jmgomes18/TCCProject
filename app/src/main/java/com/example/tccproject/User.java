package com.example.tccproject;

import com.google.firebase.database.IgnoreExtraProperties;

public class User {

    public String name;
    public String email;
    public String password;
    public String cargo;

    public User() {

    }

    public User(String name, String email, String password, String cargo) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.cargo = cargo;
    }

}
