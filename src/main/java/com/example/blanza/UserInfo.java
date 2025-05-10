package com.example.blanza;

public class UserInfo {
    int Id;
    String username;
    String email;
    String phoneNumber;
    String password;

    public UserInfo(int Id, String username, String email, String phoneNumber, String password) {
        this.Id = Id;
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public int getId() {
        return Id;
    }
    public String getUsername() {
        return username;
    }
    public String getEmail() {
        return email;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public String getPassword() {
        return password;
    }
}
