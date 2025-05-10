package com.example.blanza;

public class UserManager {
    public static boolean signupProcess(String username, String email, String phoneNumber, String password, String confirmPassword) {
        // TO DO
        return false;
    }

    public static boolean loginProcess(String email, String password){
        // TO DO
        return false;
    }

    private boolean signupValidation(String username, String email, String phoneNumber, String password, String confirmPassword){
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

    private boolean loginValidation(String email, String password){
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

    private void CreateUser(String username, String email, String phoneNumber, String password) {
        UserDB.insertUserDB(username, email, phoneNumber, password);
    }

    private void DeleteUser(String username) {
        UserDB.removeUserDB(username);
    }


}
