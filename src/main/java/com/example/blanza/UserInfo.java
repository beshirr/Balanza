package com.example.blanza;

/**
 * The type User info.
 */
public class UserInfo {

    int Id;
    String username;
    String email;
    String phoneNumber;
    String password;
    String otp;

    /**
     * Instantiates a new User info.
     *
     * @param Id          the id
     * @param username    the username
     * @param email       the email
     * @param phoneNumber the phone number
     * @param password    the password
     * @param otp         the otp
     */
    public UserInfo(int Id, String username, String email, String phoneNumber, String password, String otp) {
        this.Id = Id;
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.otp = otp;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public int getId() {
        return Id;
    }

    /**
     * Gets username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets email.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Gets phone number.
     *
     * @return the phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Gets password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Gets otp.
     *
     * @return the otp
     */
    public String getOtp() { return otp; }
}
