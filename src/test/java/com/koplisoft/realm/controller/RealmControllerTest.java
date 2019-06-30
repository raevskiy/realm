package com.koplisoft.realm.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_PDF;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.koplisoft.realm.controller.exceptions.ApiException;
import com.koplisoft.realm.converter.RealmConverter;
import com.koplisoft.realm.dto.ApiError;
import com.koplisoft.realm.dto.Realm;
import com.koplisoft.realm.service.RealmService;

@RunWith(MockitoJUnitRunner.class)
public class RealmControllerTest {
	@InjectMocks
	private RealmController controller;
	@Mock
	private RealmService realmService;
	@Mock
	private RealmConverter realmConverter;

	@Test
	public void createsRealm() {
		Realm responseRealmDto = new Realm();
		Realm requestRealmDto = new Realm();
		requestRealmDto.setName("Forgotten Realm");
		requestRealmDto.setDescription("Description");
		com.koplisoft.realm.model.Realm realm = new com.koplisoft.realm.model.Realm();
		when(realmService.createRealm(requestRealmDto.getName(), requestRealmDto.getDescription())).thenReturn(Optional.of(realm));
		when(realmConverter.convertEntityToDto(realm)).thenReturn(responseRealmDto);
		
		ResponseEntity<Realm> responseEntity = controller.createRealm(requestRealmDto, APPLICATION_PDF);
		
		verify(realmConverter).validateDto(requestRealmDto);
		assertThat(responseEntity.getBody(), is(responseRealmDto));
		assertThat(responseEntity.getHeaders().getContentType(), is(APPLICATION_PDF));
		assertThat(responseEntity.getStatusCode(), is(HttpStatus.CREATED));
	}
	
	@Test(expected = ApiException.class)
	public void doesNotCreateRealmWithoutNameAndPropagatesException() {
		Realm requestRealmDto = new Realm();
		doThrow(ApiException.class).when(realmConverter).validateDto(requestRealmDto);
		
		controller.createRealm(requestRealmDto, APPLICATION_PDF);
	}
	
	@Test(expected = ApiException.class)
	public void doesNotCreateRealmWithDuplicatedNameAndThrowsException() {
		Realm requestRealmDto = new Realm();
		requestRealmDto.setName("Forgotten Realm");
		requestRealmDto.setDescription("Description");
		when(realmService.createRealm(requestRealmDto.getName(), requestRealmDto.getDescription())).thenReturn(Optional.empty());
		
		controller.createRealm(requestRealmDto, APPLICATION_PDF);
	}
	
	@Test
	public void findsRealmById() {
		Realm responseRealmDto = new Realm();
		com.koplisoft.realm.model.Realm realm = new com.koplisoft.realm.model.Realm();
		when(realmConverter.convertRealmIdString("42")).thenReturn(42L);
		when(realmService.findRealmById(42L)).thenReturn(Optional.of(realm));
		when(realmConverter.convertEntityToDto(realm)).thenReturn(responseRealmDto);
		
		assertThat(controller.findRealmById("42"), is(responseRealmDto));
	}
	
	@Test(expected = ApiException.class)
	public void doesNotFindsRealmForWrongIdStringAndPropagatesException() {
		doThrow(ApiException.class).when(realmConverter).convertRealmIdString("!3");
		
		controller.findRealmById("!3");
	}
	
	@Test(expected = ApiException.class)
	public void doesNotFindsRealmForNotExistingIdAndThrowsException() {
		when(realmConverter.convertRealmIdString("42")).thenReturn(42L);
		when(realmService.findRealmById(42L)).thenReturn(Optional.empty());
		
		controller.findRealmById("42");
	}

	@Test
	public void handlesApiException() {
		ApiException exception = new ApiException("Oops", HttpStatus.I_AM_A_TEAPOT);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getContentType()).thenReturn(MediaType.APPLICATION_JSON_UTF8_VALUE);
		
		ResponseEntity<ApiError> responseEntity = controller.handleApiException(request, exception);

		ApiError apiError = responseEntity.getBody(); 
		assertThat(apiError.getCode(), is(exception.getMessage()));
		assertThat(responseEntity.getStatusCode(), is(exception.getHttpStatus()));
		assertThat(responseEntity.getHeaders().getContentType(), is(MediaType.APPLICATION_JSON_UTF8));
	}
}
