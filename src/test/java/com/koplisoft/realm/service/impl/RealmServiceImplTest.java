package com.koplisoft.realm.service.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.koplisoft.realm.dao.RealmDao;
import com.koplisoft.realm.model.Realm;

@RunWith(MockitoJUnitRunner.class)
public class RealmServiceImplTest {
	@InjectMocks
	private RealmServiceImpl service;
	@Mock
	private RealmDao realmDao;

	@Test
	public void createsRealm() {
		Optional<Realm> realm = service.createRealm("name", "description");
		
		ArgumentCaptor<Realm> realmCaptor = ArgumentCaptor.forClass(Realm.class); 
		verify(realmDao).save(realmCaptor.capture());
		Realm createdRealm = realmCaptor.getValue();
		assertThat(createdRealm.getName(), is("name"));
		assertThat(createdRealm.getDescription(), is("description"));
		assertThat(realm.get(), is(createdRealm));
	}
	
	@Test
	public void doesNotCreateRealmWithDuplicatedName() {
		when(realmDao.findByName("name")).thenReturn(Optional.of(new Realm()));
		
		Optional<Realm> realm = service.createRealm("name", "description");

		verify(realmDao, never()).save(any(Realm.class));
		assertFalse(realm.isPresent());
	}
	
	@Test
	public void findsRealmById() {
		Realm realm = new Realm();
		when(realmDao.findById(42L)).thenReturn(Optional.of(realm));
		
		Optional<Realm> realmFound = service.findRealmById(42L);
		
		assertThat(realmFound.get(), is(realm));
	}
	
	@Test
	public void findsNoRealmById() {
		Optional<Realm> realmFound = service.findRealmById(42L);
		
		assertFalse(realmFound.isPresent());
	}

}
