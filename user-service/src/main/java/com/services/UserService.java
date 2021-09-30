package com.services;

import com.entities.Log;
import com.entities.User;

import com.repos.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@Slf4j
public class UserService {

    private final UserRepository repository;
    private final LogService logService;
    @Autowired
    public UserService(UserRepository repository, LogService logService) {
        this.repository = repository;
        this.logService = logService;
    }


    public User save(User user) {

        if(this.repository.findByEmail(user.getEmail())==null){
            this.logService.sendLog("Info", "UserService",  "New user signed up");

            return this.repository.save(user);
        }
        this.logService.sendLog("Warning", "UserService",  "Attempt to register a new user failed.");
        return null;
    }



    public User getById(ObjectId id) {
        return this.repository.findById(id).orElse(null);
    }

    public List<User> getAll(){return this.repository.findAll();}


    public User authenticate(User user) {
        User user1 = this.repository.findByEmail(user.getEmail());
        HttpEntity<Log> request;
        if(user1!=null){
            this.logService.sendLog("Info", "UserService",  "User " + user.getEmail() + " logged in.");
        }else{
            this.logService.sendLog("Info", "UserService",  "User authentication failed  for " + user.getEmail());
        }
        return user1;
    }
}
