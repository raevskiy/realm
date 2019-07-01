package com.koplisoft.realm.controller;

import static org.springframework.http.MediaType.ALL;
import static org.springframework.http.MediaType.APPLICATION_XML;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

public class HttpHeadersFactory {
	public static HttpHeaders createHeadersWithContentType(HttpServletRequest request) {
		String contentType = HttpMethod.GET.name().equals(request.getMethod()) ? request.getHeader("Accept") : request.getContentType();
		MediaType mediaType = MediaType.parseMediaTypes(contentType).stream().findFirst().orElse(APPLICATION_XML);
		return createHeadersWithContentType(mediaType);
	}

	public static HttpHeaders createHeadersWithContentType(MediaType mediaType) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(ALL.equals(mediaType) ? APPLICATION_XML : mediaType);
		return headers;
	}
}
