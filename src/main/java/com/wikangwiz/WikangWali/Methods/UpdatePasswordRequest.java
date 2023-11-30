package com.wikangwiz.WikangWali.Methods;

public class UpdatePasswordRequest {

    private String oldPassword;
    private String newPassword;
    private String confirmPassword;

    // Constructors, getters, and setters

    // Default constructor
    public UpdatePasswordRequest() {
    }

    // Parameterized constructor
    public UpdatePasswordRequest(String oldPassword, String newPassword, String confirmPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
    }

    // Getters and setters

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}

