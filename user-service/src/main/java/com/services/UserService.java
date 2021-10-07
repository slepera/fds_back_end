package com.services;


import com.entities.User;
import com.entities.Log;
import com.repos.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserService {

    private final UserRepository repository;
    private final LogService logService;
    @Autowired
    public UserService(LogService logService, UserRepository repository) {
        this.repository = repository;
        this.logService = logService;
    }


    public User save(User user) {

        if(this.repository.findByEmail(user.getEmail())==null){
            this.logService.sendLog("Info", "UserService",  "New user signed up");

            return this.repository.save(user);
        }
        this.logService.sendLog( "Warning", "UserService",  "Attempt to register a new user failed.");
        return null;
    }



    public User getById(ObjectId id) {
        return this.repository.findById(id).orElse(null);
    }

    public List<User> getAll(){
        this.logService.sendLog( "Info", "UserService",  "TestLog");
        return this.repository.findAll();
    }


    public User authenticate(User user) {
        User user1 = this.repository.findByEmail(user.getEmail());
        HttpEntity<Log> request;
        if(user1!=null && user1.getPassword().equals(BCrypt.hashpw(user.getPassword(), user1.getSalt()))){
            this.logService.sendLog( "Info", "UserService",  "User " + user.getEmail() + " logged in.");
        }else{
            this.logService.sendLog("Warning", "UserService",  "User authentication failed  for " + user.getEmail());
        }
        return user1;
    }
}
