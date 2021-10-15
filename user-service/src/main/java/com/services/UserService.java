package com.services;

import com.entities.LoginRequest;
import com.entities.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.entities.LoginRequest;
import com.entities.User;
import com.entities.Log;

import com.repos.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@Slf4j
public class UserService {



    private final UserRepository repository;
    private final LogClient logClient;
    private final RestTemplate restTemplate;
    private final JwtUtil jwt;

    @Autowired
    public UserService(RestTemplate restTemplate, LogClient logClient,
                       final JwtUtil jwt, UserRepository repository) {
        this.repository = repository;
        this.restTemplate = restTemplate;
        this.jwt = jwt;
        this.logClient = logClient;
    }

    public User register(User user) {
        String salt =  BCrypt.gensalt();
        String password = BCrypt.hashpw(user.getPassword(), salt);
        user.setPassword(password);
        user.setSalt(salt);
        if(this.repository.findByEmail(user.getEmail())==null){
            logClient.sendLog("Info",   "New user signed up");
            return this.repository.save(user);
        }
        logClient.sendLog( "Warning",   "Attempt to register a new user failed.");
        return new User();
    }

    public User getById(ObjectId id)
    {
        return this.repository.findById(id).orElse(null);
    }

    public List<User> getAll()
    {
        return this.repository.findAll();
    }

    public User login(LoginRequest user) {
        User user1 = this.repository.findByEmail(user.getEmail());
        if(user1==null)
        {
            logClient.sendLog("Warning",   "User authentication failed  for " + user.getEmail());
            return new User();
        }
        String password = user1.getPassword();
        String salt = user1.getSalt();
        if(((BCrypt.hashpw(user.getPassword(), salt)).equals(password)))
        {
            String accessToken = jwt.generate(user1, "ACCESS");
            String refreshToken = jwt.generate(user1, "REFRESH");
            user1.setToken(accessToken);
            logClient.sendLog( "Info",   "User " + user.getEmail() + " logged in.");
            return user1;
        }
        logClient.sendLog("Warning",   "User authentication failed  for " + user.getEmail());
        return new User();
    }

}





