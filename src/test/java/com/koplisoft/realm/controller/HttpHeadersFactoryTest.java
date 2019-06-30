package com.koplisoft.realm.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

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
	public void createsHeadersWithSameContentTypeForString() {
		HttpHeaders headers = HttpHeadersFactory.createHeadersWithContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
		
		assertThat(headers.getContentType(), is(MediaType.APPLICATION_JSON_UTF8));
	}
	
	@Test
	public void createsHeadersWithXmlContentTypeAsFallbackForNullString() {
		String mediaType = null;
		HttpHeaders headers = HttpHeadersFactory.createHeadersWithContentType(mediaType);
		
		assertThat(headers.getContentType(), is(MediaType.APPLICATION_XML));
	}

}
