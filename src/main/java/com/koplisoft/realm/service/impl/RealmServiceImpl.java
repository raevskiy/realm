package com.koplisoft.realm.service.impl;

import java.util.Optional;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.koplisoft.realm.dao.RealmDao;
import com.koplisoft.realm.model.Realm;
import com.koplisoft.realm.service.RealmService;

@Service
public class RealmServiceImpl implements RealmService {
	@Autowired
	private RealmDao realmDao;

	@Override
	public Optional<Realm> createRealm(String name, String description) {
		if (realmDao.findByName(name).isPresent()) {
			return Optional.empty();
		}
		
		String key = RandomStringUtils.random(32, true, true);
		Realm realm = new Realm(name, key);
		realm.setDescription(description);
		realmDao.save(realm);
		return Optional.of(realm);
	}

	@Override
	public Optional<Realm> findRealmById(Long id) {
		return realmDao.findById(id);
	}

}
