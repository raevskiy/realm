package com.koplisoft.realm.dao;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.koplisoft.realm.model.Realm;

public interface RealmDao extends CrudRepository<Realm, Long>{
	Optional<Realm> findByName(String name);
}
