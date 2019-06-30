package com.koplisoft.realm.controller;

import static org.springframework.http.MediaType.ALL;
import static org.springframework.http.MediaType.APPLICATION_XML;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class HttpHeadersFactory {
	public static HttpHeaders createHeadersWithContentType(String mediaTypes) {
		MediaType mediaType = MediaType.parseMediaTypes(mediaTypes).stream().findFirst().orElse(APPLICATION_XML);
		return createHeadersWithContentType(mediaType);
	}

	public static HttpHeaders createHeadersWithContentType(MediaType mediaType) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(ALL.equals(mediaType) ? APPLICATION_XML : mediaType);
		return headers;
	}
}
