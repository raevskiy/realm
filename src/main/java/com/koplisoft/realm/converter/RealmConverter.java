package com.koplisoft.realm.converter;

import com.koplisoft.realm.dto.Realm;

public interface RealmConverter {
	void validateDto(Realm realm);
	Realm convertEntityToDto(com.koplisoft.realm.model.Realm realm);
	Long convertRealmIdString(String realmIdString);
}
