package com.wikangwiz.WikangWali.Methods;

public class ForgotPasswordRequest {
	private String username;

	public ForgotPasswordRequest() {
		super();
	}

	public ForgotPasswordRequest(String username) {
		super();
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
}
