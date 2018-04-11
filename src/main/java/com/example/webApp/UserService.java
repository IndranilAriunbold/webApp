package com.example.webApp;

import com.example.Model.User;
import org.keycloak.KeycloakSecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
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
    private User user = new User();
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final RestTemplate restTemplate;

    public UserService(RestTemplateBuilder restTemplateBuilder){
        this.restTemplate = restTemplateBuilder.build();
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
    public List<User> getAllUsersFromDB() throws Failure, IOException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
        logger.info("getAllUsersFromDB ", request);
        logger.info(String.valueOf(request));
        final Principal userPrincipal = request.getUserPrincipal();
        KeycloakSecurityContext mySecurityContext = (KeycloakSecurityContext)request.getAttribute(KeycloakSecurityContext.class.getName());
        String token = mySecurityContext.getTokenString();
        logger.info("token");
        //For same hosts ; HttpGet httpGet = new HttpGet(UriUtils.getOrigin(request.getRequestURL().toString()) + "/");
        //Request is made to remote REST Service!
        String url = "http://localhost:8080/demo/all";
        HttpHeaders headers = new HttpHeaders();
        //headers.add("Authorization", "Bearer " + token);
        //headers.add("Content-Type", "application/json");
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer "+token);
        //headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        //HttpEntity<model.User> entity = new HttpEntity<> (headers);
        HttpEntity<String> entity = new HttpEntity<> (headers);
        logger.info("entity");
        ResponseEntity<User[]> result = restTemplate.exchange(url, HttpMethod.GET, entity, User[].class);
        //ResponseEntity<User[]> result = restTemplate.getForEntity(url, User[].class);
        return Arrays.asList(result.getBody());

    }


    public List<String> getUsers(String request) {
        return Arrays.asList(request ,"Big", "Middle", "Small");
    }

    public String MakeAUserForDB() throws Exception{
    //public String MakeAUserForDB(HttpServletRequest request) throws Exception{
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
        logger.info("About to Post");
        //Get the token
        KeycloakSecurityContext mySecurityContext = (KeycloakSecurityContext)request.getAttribute(KeycloakSecurityContext.class.getName());
        String token = mySecurityContext.getTokenString();

        // turn the character to JSON, now send the character on to the Mongo Service
        String name = mySecurityContext.getIdToken().getPreferredUsername();
        String email = mySecurityContext.getIdToken().getEmail();
        user.setName(name);
        user.setEmail(email);
        System.out.println("User" + user.toString());
        //build the POST
//        CloseableHttpClient client = HttpClientBuilder.create().build();
//        try {

        String url = "http://localhost:8080/demo/add";

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
