package com.koplisoft.realm.service;

import java.util.Optional;

import com.koplisoft.realm.model.Realm;

public interface RealmService {
	Optional<Realm> createRealm(String name, String description);

	Optional<Realm> findRealmById(Long id);
}
