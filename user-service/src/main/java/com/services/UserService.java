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

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User save(User user) {
        if(this.repository.findByEmail(user.getEmail())==null){
            return this.repository.save(user);
        }
        return null;
    }

    public User getById(ObjectId id) {
        return this.repository.findById(id).orElse(null);
    }

    public List<User> getAll(){
        return this.repository.findAll();
    }

    public User authenticate(User user) {
        User user1 = this.repository.findByEmail(user.getEmail());
        return user1;
    }
}
