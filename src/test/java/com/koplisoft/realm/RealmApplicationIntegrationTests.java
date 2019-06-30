package com.koplisoft.realm;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.koplisoft.realm.dto.ApiError;
import com.koplisoft.realm.dto.Realm;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RealmApplicationIntegrationTests {
	
	@LocalServerPort
    private int port;
    private TestRestTemplate restTemplate = new TestRestTemplate();
    private HttpHeaders headers = new HttpHeaders();
    
    @Test
    public void createsAndFindsRealm() throws Exception {
    	Realm createdRealm = createsRealm();
    	findsRealm(createdRealm);
    }
    
    private Realm createsRealm() {
    	Realm realm = new Realm();
    	realm.setName("Forgotten Realm");
    	realm.setDescription("Description");
        HttpEntity<Realm> entity = new HttpEntity<>(realm, headers);
        
        ResponseEntity<Realm> response = restTemplate.exchange(
          createURLWithPort("/service/user/realm"), HttpMethod.POST, entity, Realm.class);
        
        Realm createdRealm = response.getBody();
        assertThat(createdRealm.getName(), is(realm.getName()));
        assertThat(createdRealm.getDescription(), is(realm.getDescription()));
        assertThat(createdRealm.getKey().length(), is(32));
        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
        return createdRealm;
    }
    
    private void findsRealm(Realm createdRealm) {
        ResponseEntity<Realm> response = restTemplate.exchange(
          createURLWithPort("/service/user/realm/" + createdRealm.getId().toString()), HttpMethod.GET, null, Realm.class);
        
        Realm foundRealm = response.getBody();
        assertThat(foundRealm.getId(), is(createdRealm.getId()));
        assertThat(foundRealm.getName(), is(createdRealm.getName()));
        assertThat(foundRealm.getDescription(), is(createdRealm.getDescription()));
        assertThat(foundRealm.getKey(), is(createdRealm.getKey()));
    }
    
    @Test
    public void doesNotCreateRealmWithoutName() {
    	Realm realm = new Realm();
    	realm.setDescription("Description");
        HttpEntity<Realm> entity = new HttpEntity<>(realm, headers);
        
        ResponseEntity<ApiError> response = restTemplate.exchange(
          createURLWithPort("/service/user/realm"), HttpMethod.POST, entity, ApiError.class);
        
        ApiError error = response.getBody();
        assertThat(error.getCode(), is("InvalidRealmName"));
        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    }

    @Test
    public void doesNotCreateRealmWithDuplicatedName() {
    	Realm realm = new Realm();
    	realm.setName("Realm");
    	realm.setDescription("Description");
        HttpEntity<Realm> entity = new HttpEntity<>(realm, headers);

        restTemplate.exchange(createURLWithPort("/service/user/realm"), HttpMethod.POST, entity, Realm.class);
        ResponseEntity<ApiError> response = restTemplate.exchange(
          createURLWithPort("/service/user/realm"), HttpMethod.POST, entity, ApiError.class);
        
        ApiError error = response.getBody();
        assertThat(error.getCode(), is("DuplicateRealmName"));
        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    }
    
    @Test
    public void doesNotFindRealmForWrongIdString() {
        ResponseEntity<ApiError> response = restTemplate.exchange(
          createURLWithPort("/service/user/realm/!3"), HttpMethod.GET, null, ApiError.class);
        
        ApiError error = response.getBody();
        assertThat(error.getCode(), is("InvalidArgument"));
        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    }
    
    @Test
    public void doesNotFindRealmForNonExistingId() {
        ResponseEntity<ApiError> response = restTemplate.exchange(
          createURLWithPort("/service/user/realm/-1"), HttpMethod.GET, null, ApiError.class);
        
        ApiError error = response.getBody();
        assertThat(error.getCode(), is("RealmNotFound"));
        assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }
    
    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

}
