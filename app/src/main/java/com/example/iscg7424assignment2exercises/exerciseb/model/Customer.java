package com.example.iscg7424assignment2exercises.exerciseb.model;

public class Customer {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String image;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getEmail() {
        return email;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }

    public String getImage() {
        return image;
    }

    public Customer(int id, String firstName, String lastName, String image, String phone, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.image = image;
        this.phone = phone;
        this.email = email;
    }
}
