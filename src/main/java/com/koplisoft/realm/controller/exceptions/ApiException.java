package com.koplisoft.realm.controller.exceptions;

import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {
	public static final String REALM_NAME_MISSING = "InvalidRealmName";
	public static final String ID_STRING_IS_NOT_AN_INTEGER = "InvalidArgument";
	public static final String REALM_NAME_EXISTS = "DuplicateRealmName";
	public static final String REALM_NOT_FOUND = "RealmNotFound";

	private static final long serialVersionUID = 1L;

	private HttpStatus httpStatus;

	public ApiException(String message, HttpStatus httpStatus) {
		super(message);
		this.httpStatus = httpStatus;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

}
