package com.koplisoft.realm.converter.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import com.koplisoft.realm.controller.exceptions.ApiException;
import com.koplisoft.realm.dto.Realm;

@RunWith(MockitoJUnitRunner.class)
public class RealmConverterImplTest {
	@InjectMocks
	private RealmConverterImpl converter;

	@Test
	public void validationPassesForDtoWithName() {
		Realm realm = new Realm();
		realm.setName("Forgotten Realm");
		
		converter.validateDto(realm);
	}
	
	@Test(expected = ApiException.class)
	public void validationFailsForDtoWithoutName() {
		Realm realm = new Realm();

		converter.validateDto(realm);
	}

	@Test
	public void convertsEntityToDto() {
		com.koplisoft.realm.model.Realm realm = new com.koplisoft.realm.model.Realm("Forgotten Realm", "The Skeleton Key");
		realm.setId(42L);
		realm.setDescription("Description");
		
		Realm realmDto = converter.convertEntityToDto(realm);
		
		assertThat(realmDto.getId(), is(realm.getId()));
		assertThat(realmDto.getName(), is(realm.getName()));
		assertThat(realmDto.getDescription(), is(realm.getDescription()));
		assertThat(realmDto.getKey(), is(realm.getEncryptionKey()));
	}

	@Test
	public void convertsRealmIdForValidString() {
		Long id = converter.convertRealmIdString("42");
		
		assertThat(id, is(42L));
	}
	
	@Test(expected = ApiException.class)
	public void convertsRealmIdForInvalidStringWithException() {
		converter.convertRealmIdString("!3");
	}

}
