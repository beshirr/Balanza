package com.example.blanza;

public class UserManager {
    public static boolean signupProcess(String username, String email, String phoneNumber, String password, String confirmPassword) {
        if (!signupValidation(username, email, phoneNumber, password, confirmPassword)){
            return false;
        }
        else {
            return true;
        }
    }

    public static boolean loginProcess(String email, String password){
        return loginValidation(email, password);
    }

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

    private static boolean loginValidation(String email, String password){
        if (email == null || password == null){
            return false;
        }
        else if (UserDB.getUserInfoByUsername(email) == null){
            return false;
        }
        else if (UserDB.getUserInfoByUsername(email) != null){
            UserInfo userInfo = UserDB.getUserInfoByUsername(email);
            if (!userInfo.getPassword().equals(password)){
                return false;
            }
        }
        return true;
    }

    private static void createUser(String username, String email, String phoneNumber, String password) {
        UserDB.insertUserDB(username, email, phoneNumber, password);
    }

    private static void deleteUser(String username) {
        UserDB.removeUserDB(username);
    }


}
