package com.services;

import com.entities.JWTToken;
import com.entities.LoginRequest;
import com.entities.User;
import com.entities.value_objects.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthService {

    private final RestTemplate restTemplate;
    private final JwtUtil jwt;

    @Autowired
    public AuthService(RestTemplate restTemplate,
                       final JwtUtil jwt) {
        this.restTemplate = restTemplate;
        this.jwt = jwt;
    }

    public JWTToken register(User user) {
        //do validation if user already exists
        String salt =  BCrypt.gensalt();
        String password = BCrypt.hashpw(user.getPassword(), salt);
        user.setPassword(password);
        user.setSalt(salt);
        UserVO userVO = restTemplate.postForObject("http://user-service/register", user, UserVO.class);
        Assert.notNull(userVO, "Failed to register user. Please try again later");

        String accessToken = jwt.generate(userVO, "ACCESS");
        String refreshToken = jwt.generate(userVO, "REFRESH");

        return new JWTToken(accessToken, refreshToken);

    }

    public UserVO login(LoginRequest loginRequest) {
        //do validation if user already exists
        UserVO userVO = restTemplate.postForObject("http://user-service/login", loginRequest, UserVO.class);
        if(userVO==null)
        {
            return new UserVO();
        }
        String password = userVO.getPassword();
        String salt = userVO.getSalt();
        if(((BCrypt.hashpw(loginRequest.getPassword(), salt)).equals(password)))
        {
            String accessToken = jwt.generate(userVO, "ACCESS");
            String refreshToken = jwt.generate(userVO, "REFRESH");
            userVO.setToken(accessToken);
            return userVO;
        }
        return new UserVO();
    }
}
