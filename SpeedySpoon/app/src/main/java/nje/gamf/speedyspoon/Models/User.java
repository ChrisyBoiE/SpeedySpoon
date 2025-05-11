package nje.gamf.speedyspoon.Models;

import java.util.Map;

public class User {
    private String email;
    private Map<String, Boolean> orders;
    private String password;
    private String phoneNumber;
    private String username;

    // Default constructor required for calls to DataSnapshot.getValue(User.class)
    public User() {
    }

    public User(String email, Map<String, Boolean> orders, String password, String phoneNumber, String username) {
        this.email = email;
        this.orders = orders;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Map<String, Boolean> getOrders() {
        return orders;
    }

    public void setOrders(Map<String, Boolean> orders) {
        this.orders = orders;
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
}
