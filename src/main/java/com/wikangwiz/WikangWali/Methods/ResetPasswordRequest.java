package com.wikangwiz.WikangWali.Methods;

public class ResetPasswordRequest {
    private String username;
    private int resetCode;
    private String newPassword;
    private String confirmPassword;
    
	public ResetPasswordRequest() {
		super();
	}

	public ResetPasswordRequest(String username, int resetCode, String newPassword, String confirmPassword) {
		super();
		this.username = username;
		this.resetCode = resetCode;
		this.newPassword = newPassword;
		this.confirmPassword = confirmPassword;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getResetCode() {
		return resetCode;
	}

	public void setResetCode(int resetCode) {
		this.resetCode = resetCode;
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
