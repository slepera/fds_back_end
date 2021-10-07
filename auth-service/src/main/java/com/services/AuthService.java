package com.services;

import com.entities.LoginRequest;
import com.entities.User;
import com.entities.value_objects.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthService {

    private final LogClient logClient;
    private final RestTemplate restTemplate;
    private final JwtUtil jwt;

    @Autowired
    public AuthService(RestTemplate restTemplate, LogClient logClient,
                       final JwtUtil jwt) {
        this.restTemplate = restTemplate;
        this.jwt = jwt;
        this.logClient = logClient;
    }

    public UserVO register(User user) {
        String salt =  BCrypt.gensalt();
        String password = BCrypt.hashpw(user.getPassword(), salt);
        user.setPassword(password);
        user.setSalt(salt);
        UserVO userVO = restTemplate.postForObject("http://user-service/register", user, UserVO.class);
        if (userVO == null){
            logClient.sendLog( "Warning", "AuthService",  "Attempt to register a new user failed.");
            return new UserVO();
        }
        logClient.sendLog("Info", "AuthService",  "New user signed up");
        return userVO;
    }

    public UserVO login(LoginRequest loginRequest) {
        UserVO userVO = restTemplate.postForObject("http://user-service/login", loginRequest, UserVO.class);
        if(userVO==null)
        {
            logClient.sendLog("Warning", "AuthService",  "User authentication failed  for " + loginRequest.getEmail());
            return new UserVO();
        }
        String password = userVO.getPassword();
        String salt = userVO.getSalt();
        if(((BCrypt.hashpw(loginRequest.getPassword(), salt)).equals(password)))
        {
            String accessToken = jwt.generate(userVO, "ACCESS");
            String refreshToken = jwt.generate(userVO, "REFRESH");
            userVO.setToken(accessToken);
            logClient.sendLog( "Info", "AuthService",  "User " + userVO.getEmail() + " logged in.");
            return userVO;
        }
        logClient.sendLog("Warning", "AuthService",  "User authentication failed  for " + userVO.getEmail());
        return new UserVO();
    }
}
