package com.example.myapp.model;

import com.example.myapp.database.*;

import com.example.myapp.service.*;

public class User {
    private int age; //возраст
    private double weight; //вес
    private double height; // рост
    private String gender; // пол
    private String username; // имя
    private double active;





    public User(int age,double weight,double height,String gender,String username,double active){
        this.age = age;
        this.weight = weight;
        this.height = height;
        this.gender = gender;
        this.username = username;
        this.active = active;

    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;

    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;

    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;

    }

    public double getActive() {
        return active;
    }

    public void setActive(double active) {
        this.active = active;

    }
}
