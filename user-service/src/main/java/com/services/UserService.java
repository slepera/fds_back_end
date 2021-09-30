package com.services;

import com.entities.Log;
import com.entities.User;

import com.mongodb.BasicDBObject;
import com.repos.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class UserService {

    private final UserRepository repository;
    private final RestTemplate restTemplate;

    @Autowired
    public UserService(UserRepository repository, RestTemplate restTemplate) {
        this.repository = repository;
        this.restTemplate = restTemplate;
    }


    public User save(User user) {

        if(this.repository.findByEmail(user.getEmail())==null){
            HttpEntity<Log> request = LogService.createLog("Info", "UserService",  "New user signed up");
            this.restTemplate.postForObject("http://log-service/log", request, String.class);
            return this.repository.save(user);
        }
        HttpEntity<Log> request = LogService.createLog("Warning", "UserService",  "Attempt to register a new user failed.");
        this.restTemplate.postForObject("http://log-service/log", request, String.class);
        return null;
    }

    public String provaSftp(String path){
        final String url = "http://ext-com-service/sftp_client/put/{id}";
        Map<String, String> param = new HashMap<String, String>();
        param.put("id",path);
        this.restTemplate.put(url,null, param);
        return "ok";
    }

    public User getById(ObjectId id) {
        return this.repository.findById(id).orElse(null);
    }

    public List<User> getAll(){return this.repository.findAll();}


    public User authenticate(User user) {
        User user1 = this.repository.findByEmail(user.getEmail());
        HttpEntity<Log> request;
        if(user1!=null){
            request = LogService.createLog("Info", "UserService",  "User " + user.getEmail() + " logged in.");
        }else{
            request = LogService.createLog("Info", "UserService",  "User authentication failed  for " + user.getEmail());
        }
        this.restTemplate.postForObject("http://log-service/log", request, String.class);
        return user1;
    }
}
