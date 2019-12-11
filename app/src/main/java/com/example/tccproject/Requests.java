package com.example.tccproject;

public class Requests {

    public String user_id;
    public int request_type; //1 for people, 2 for vehicle

    public Requests() {

    }

    public Requests(String user_id, int request_type) {
        this.user_id = user_id;
        this.request_type = request_type;
    }
}
