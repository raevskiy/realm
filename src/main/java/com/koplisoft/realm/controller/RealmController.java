package com.koplisoft.realm.controller;

import static com.koplisoft.realm.controller.HttpHeadersFactory.createHeadersWithContentType;
import static com.koplisoft.realm.controller.exceptions.ApiException.REALM_NAME_EXISTS;
import static com.koplisoft.realm.controller.exceptions.ApiException.REALM_NOT_FOUND;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.koplisoft.realm.controller.exceptions.ApiException;
import com.koplisoft.realm.converter.RealmConverter;
import com.koplisoft.realm.dto.ApiError;
import com.koplisoft.realm.dto.Realm;
import com.koplisoft.realm.service.RealmService;

@Controller
@RequestMapping(path = "/service/user")
public class RealmController {
	@Autowired
	private RealmService realmService;
	@Autowired
	private RealmConverter realmConverter;

	@PostMapping(path = "/realm")
	public ResponseEntity<Realm> createRealm(@RequestBody Realm realm,
			@RequestHeader("Content-Type") MediaType contentType) {
		realmConverter.validateDto(realm);

		Realm createdRealm = realmService.createRealm(realm.getName(), realm.getDescription())
				.map(realmConverter::convertEntityToDto)
				.orElseThrow(() -> new ApiException(REALM_NAME_EXISTS, BAD_REQUEST));

		return new ResponseEntity<>(createdRealm, createHeadersWithContentType(contentType), CREATED);
	}

	@GetMapping(path = "/realm/{realmId}")
	public @ResponseBody Realm findRealmById(@PathVariable String realmId) {
		return realmService.findRealmById(realmConverter.convertRealmIdString(realmId))
				.map(realmConverter::convertEntityToDto)
				.orElseThrow(() -> new ApiException(REALM_NOT_FOUND, NOT_FOUND));
	}

	@ExceptionHandler(ApiException.class)
	ResponseEntity<ApiError> handleApiException(HttpServletRequest req, ApiException e) {
		ApiError apiError = new ApiError();
		apiError.setCode(e.getMessage());
		return new ResponseEntity<>(apiError, createHeadersWithContentType(req.getContentType()), e.getHttpStatus());
	}

}
