package com.koplisoft.realm.converter.impl;

import static com.koplisoft.realm.controller.exceptions.ApiException.ID_STRING_IS_NOT_AN_INTEGER;
import static com.koplisoft.realm.controller.exceptions.ApiException.REALM_NAME_MISSING;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.koplisoft.realm.controller.exceptions.ApiException;
import com.koplisoft.realm.converter.RealmConverter;
import com.koplisoft.realm.dto.Realm;

@Component
public class RealmConverterImpl implements RealmConverter {

	@Override
	public void validateDto(Realm realm) {
		if (StringUtils.isEmpty(realm.getName())) {
			throw new ApiException(REALM_NAME_MISSING, BAD_REQUEST);
		}
	}

	@Override
	public Realm convertEntityToDto(com.koplisoft.realm.model.Realm realm) {
		Realm realmDto = new Realm();
		realmDto.setId(realm.getId());
		realmDto.setName(realm.getName());
		realmDto.setDescription(realm.getDescription());
		realmDto.setKey(realm.getEncryptionKey());
		return realmDto;
	}

	@Override
	public Long convertRealmIdString(String realmIdString) {
		try {
			return Long.parseLong(realmIdString);
		} catch (NumberFormatException e) {
			throw new ApiException(ID_STRING_IS_NOT_AN_INTEGER, BAD_REQUEST);
		}
	}

}
