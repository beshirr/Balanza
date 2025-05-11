package com.example.blanza;

/**
 * The type User manager.
 */
public class UserManager {
    /**
     * Signup process boolean.
     *
     * @param username        the username
     * @param email           the email
     * @param phoneNumber     the phone number
     * @param password        the password
     * @param confirmPassword the confirm password
     * @return the boolean
     */
    public static boolean signupProcess(String username, String email, String phoneNumber, String password, String confirmPassword) {
        if (!signupValidation(username, email, phoneNumber, password, confirmPassword)){
            return false;
        }
        else {
            String otp = OTPGenerator.generateOTP();
            AuthenticationService.sendOTP(email, otp);
            createUser(username, email, phoneNumber, password);
            UserDB.setOTP(email, otp);
            SessionManager.saveSession(UserDB.getUserInfoByUsername(username).getId());
            return true;
        }
    }

    /**
     * Login process boolean.
     *
     * @param email    the email
     * @param password the password
     * @return the boolean
     */
    public static boolean loginProcess(String email, String password){
        if (loginValidation(email, password)) {
            SessionManager.saveSession(UserDB.getUserInfoByEmail(email).getId());
            return true;
        }
        return false;
    }

    /**
     * signup validation boolean.
     *
     * @param username the username
     * @param email the email
     * @param phoneNumber the phone number
     * @param password the password
     * @param confirmPassword the confirmation password
     * @return the boolean
     */
    private static boolean signupValidation(String username, String email, String phoneNumber, String password, String confirmPassword){
        if (username == null || email == null || phoneNumber == null || password == null || confirmPassword == null){
            return false;
        }
        else if (!email.contains("@gmail.com")){
            return false;
        }
        else if (!password.equals(confirmPassword)){
            return false;
        }
        else if (UserDB.getUserInfoByUsername(username) != null){
            return false;
        }
        else if (password.contains(" ")){
            return false;
        }
        else if (password.length() < 6){
            return false;
        }
        return true;
    }

    /**
     * login validation boolean.
     *
     * @param email the email
     * @param password the password
     * @return the boolean
     */
    private static boolean loginValidation(String email, String password){
        if (email == null || password == null){
            return false;
        }
        else if (UserDB.getUserInfoByEmail(email) == null){
            return false;
        }
        else if (UserDB.getUserInfoByEmail(email) != null){
            UserInfo userInfo = UserDB.getUserInfoByEmail(email);
            if (!userInfo.getPassword().equals(password)){
                return false;
            }
            else if (userInfo.getOtp() == null){
                return false;
            }
        }
        return true;
    }

    /**
     * create user void.
     *
     * @param username the username
     * @param email the email
     * @param phoneNumber the phone number
     * @param password the password
     */
    private static void createUser(String username, String email, String phoneNumber, String password) {
        UserDB.insertUserDB(username, email, phoneNumber, password);
    }

    /**
     * delete user void.
     *
     * @param email the email
     */
    private static void deleteUser(String email) {
        UserDB.removeUserDB(email);
    }


}
