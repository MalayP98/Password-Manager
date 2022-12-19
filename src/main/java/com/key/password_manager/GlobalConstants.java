package com.key.password_manager;

public interface GlobalConstants {

	public static final String STRONG_PASSWORD_REGEX =
			"^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;_',?/*~$^+=<>]).{8,20}$";
}
