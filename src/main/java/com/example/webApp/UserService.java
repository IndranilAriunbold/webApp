package com.example.webApp;

import com.example.Model.User;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class UserService {
    
    @Autowired
    private KeycloakRestTemplate restTemplate;
    //private RestTemplate restTemplate;

    private User user = new User();
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public AccessToken getAccessToken() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .currentRequestAttributes())
                .getRequest();
        AccessToken accessToken = ((KeycloakPrincipal) ((KeycloakAuthenticationToken) request.getUserPrincipal()).getPrincipal())
                .getKeycloakSecurityContext().getToken();
        return accessToken;
    }
    public HttpServletRequest getRequest(){
        return ((ServletRequestAttributes) RequestContextHolder
                .currentRequestAttributes())
                .getRequest();
    }
    static class TypedList extends ArrayList<String> {}
    public static class Failure extends Exception {
        private int status;

        public Failure(int status) {
            this.status = status;
        }

        public int getStatus() {
            return status;
        }
    }

    //public List<User> getAllUsersFromDB(HttpServletRequest request) throws Failure, IOException {
    public List<User> getAllUsersFromDB(Principal principal) throws Failure, IOException {
        //current Request
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        //principal.getName();
        //KeycloakSecurityContext mySecurityContext = (KeycloakSecurityContext)request.getAttribute(KeycloakSecurityContext.class.getName());
        //String token = mySecurityContext.getTokenString();
        //AccessToken token = ((KeycloakPrincipal) ((KeycloakAuthenticationToken) request.getUserPrincipal()).getPrincipal()).getKeycloakSecurityContext().getToken();
        logger.info("token");


        //For same hosts ; HttpGet httpGet = new HttpGet(UriUtils.getOrigin(request.getRequestURL().toString()) + "/");
        //Request is made to remote REST Service!
        String url = "http://127.0.0.1:8080/demo/all";
        //HttpHeaders headers = new HttpHeaders();
        //headers.setContentType(MediaType.APPLICATION_JSON);
        //String token = principal.toString();
        //headers.set("Authorization", "Bearer "+token);
        //headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        //HttpEntity<String> entity = new HttpEntity<> (headers);
        logger.info("entity");
        //ResponseEntity<User[]> result = restTemplate.exchange(url, HttpMethod.GET, entity, User[].class);
        ResponseEntity<User[]> result = restTemplate.getForEntity(url, User[].class);
        return Arrays.asList(result.getBody());

    }


    public List<String> getUsers(String request) {
        return Arrays.asList(request ,"Big", "Middle", "Small");
    }

    //TODO change for KeycloakResttemplate
    public String MakeAUserForDB() throws Exception{
    //public String MakeAUserForDB(HttpServletRequest request) throws Exception{
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
        logger.info("About to Post");
        //Get the token
        //KeycloakSecurityContext mySecurityContext = (KeycloakSecurityContext)request.getAttribute(KeycloakSecurityContext.class.getName());
        //String token = mySecurityContext.getTokenString();
        AccessToken token = ((KeycloakPrincipal) ((KeycloakAuthenticationToken) request.getUserPrincipal()).getPrincipal())
                .getKeycloakSecurityContext().getToken();
        // turn the character to JSON, now send the character on to the Mongo Service
        String name = token.getPreferredUsername();
        String email = token.getEmail();
        user.setName(name);
        user.setEmail(email);
        System.out.println("User" + user.toString());
        //build the POST
//        CloseableHttpClient client = HttpClientBuilder.create().build();
//        try {

        String url = "http://172.17.0.3:8080/demo/add";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        headers.add("Content-Type", "application/json");
        HttpEntity<String> entity = new HttpEntity<String> (headers);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("name", name)
                .queryParam("email", email);

        ResponseEntity<String> result = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, String.class);
        return result.getBody();
        //ResponseEntity<User> result =   restTemplate.exchange(builder.toUriString(),HttpMethod.GET,entity, User.class);
        //return Arrays.asList(result.getBody());

    }

}
