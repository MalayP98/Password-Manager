package com.key.password_manager.utils;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class GenericReponse {

	private Boolean status;

	private String message;

	private String error;

	private String jwt;

	public Boolean getStatus() {
		return status;
	}

	public GenericReponse setStatus(Boolean status) {
		this.status = status;
		return this;
	}

	public String getMessage() {
		return message;
	}

	public GenericReponse setMessage(String message) {
		this.message = message;
		return this;
	}

	public String getError() {
		return error;
	}

	public GenericReponse setError(String error) {
		this.error = error;
		return this;
	}

	public String getJwt() {
		return jwt;
	}

	public GenericReponse setJwt(String jwt) {
		this.jwt = jwt;
		return this;
	}
}
