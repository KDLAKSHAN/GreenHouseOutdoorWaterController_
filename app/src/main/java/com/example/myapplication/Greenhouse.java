package com.example.myapplication;

public class Greenhouse {
    private String name;
    private String location;
    private String squareFeet;  // Change squareFeet to String
    private double temperature;  // Add temperature field
    private double humidity;     // Add humidity field

    // Default constructor for Firebase DataSnapshot
    public Greenhouse() {
    }

    // Constructor to initialize the Greenhouse object with String parameters for name, squareFeet, and location
    public Greenhouse(String name, String squareFeet, String location) {
        this.name = name;
        this.location = location;
        this.squareFeet = squareFeet;  // Store squareFeet as a String
    }

    // Getter and setter methods for the fields
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSquareFeet() {
        return squareFeet;  // Return squareFeet as String
    }

    public void setSquareFeet(String squareFeet) {
        this.squareFeet = squareFeet;
    }

    // Getter and setter methods for temperature and humidity
    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }
}
