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
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
        Log logTable = new Log();
        logTable.component = "UserService";
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        Date date = new Date(System.currentTimeMillis());
        logTable.date = formatter.format(date);
        logTable.level = "Trace";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        if(this.repository.findByEmail(user.getEmail())==null){
            logTable.message = "New user signed up";
            HttpEntity<Log> request =
                    new HttpEntity<Log>(logTable, headers);
            this.restTemplate.postForObject("http://log-service/log", request, String.class);
            return this.repository.save(user);
        }
        logTable.message = "Attempt to register a new user failed.";
        HttpEntity<Log> request =
                new HttpEntity<Log>(logTable, headers);
        this.restTemplate.postForObject("http://log-service/log", request, String.class);
        return null;
    }

    public User getById(ObjectId id) {
        return this.repository.findById(id).orElse(null);
    }

    public List<User> getAll(){return this.repository.findAll();}


    public User authenticate(User user) {
        User user1 = this.repository.findByEmail(user.getEmail());
        return user1;
    }
}
