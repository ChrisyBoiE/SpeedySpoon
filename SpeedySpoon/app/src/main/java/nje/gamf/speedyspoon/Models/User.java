package nje.gamf.speedyspoon.Models;

import java.util.Map;

public class User {
    private String email;
    private String password;
    private String phoneNumber;
    private String username;
    private Map<String, Order> orders;
    private String address;
    private String city;
    private String userId;

    // Kötelező üres konstruktor Firebaseserializáláshoz
    public User() {
    }

    // Frissített konstruktor paraméterekkel
    public User(String email, String password, String phoneNumber, String username, Map<String, Order> orders) {
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.username = username;
        this.orders = orders;
    }

    // Getterek és setterek
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Map<String, Order> getOrders() {
        return orders;
    }

    public void setOrders(Map<String, Order> orders) {
        this.orders = orders;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
