package com.example.iscg7424assignment2exercises.exercisea.model;

public class car {
    private String carId;
    private String brand;
    private String model;
    private int year;
    private double price;
    private String imageUrl;
    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }
    public String getBrand() {
        return brand;
    }
    public void setBrand(String brand) {
        this.brand = brand;
    }
    public String getModel() {
        return model;
    }
    public void setModel(String model) {
        this.model = model;
    }
    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public car(String carId, String brand, String model, int year, double price, String imageUrl) {
        this.carId = carId;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public car() {
        // Required for Firebase
    }
}
