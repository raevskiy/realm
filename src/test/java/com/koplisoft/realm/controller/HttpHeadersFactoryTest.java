package com.koplisoft.realm.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class HttpHeadersFactoryTest {
	@Test
	public void createsHeadersWithSameContentType() {
		HttpHeaders headers = HttpHeadersFactory.createHeadersWithContentType(MediaType.APPLICATION_JSON_UTF8);
		
		assertThat(headers.getContentType(), is(MediaType.APPLICATION_JSON_UTF8));
	}
	
	@Test
	public void createsHeadersWithXmlContentTypeAsFallback() {
		HttpHeaders headers = HttpHeadersFactory.createHeadersWithContentType(MediaType.ALL);
		
		assertThat(headers.getContentType(), is(MediaType.APPLICATION_XML));
	}
	
	@Test
	public void createsHeadersWithContentTypeForPostRequest() {
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getMethod()).thenReturn("POST");
		when(request.getContentType()).thenReturn(MediaType.APPLICATION_JSON_UTF8_VALUE);
		
		HttpHeaders headers = HttpHeadersFactory.createHeadersWithContentType(request);
		
		assertThat(headers.getContentType(), is(MediaType.APPLICATION_JSON_UTF8));
	}
	
	@Test
	public void createsHeadersWithAcceptForGetRequest() {
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getMethod()).thenReturn("GET");
		when(request.getHeader("Accept")).thenReturn(MediaType.APPLICATION_JSON_UTF8_VALUE);
		
		HttpHeaders headers = HttpHeadersFactory.createHeadersWithContentType(request);
		
		assertThat(headers.getContentType(), is(MediaType.APPLICATION_JSON_UTF8));
	}
	
	@Test
	public void createsHeadersWithXmlContentTypeAsFallbackForNullString() {
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getMethod()).thenReturn("POST");
		
		HttpHeaders headers = HttpHeadersFactory.createHeadersWithContentType(request);
		
		assertThat(headers.getContentType(), is(MediaType.APPLICATION_XML));
	}

}
