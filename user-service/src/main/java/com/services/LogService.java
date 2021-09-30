package com.services;

import com.entities.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class LogService {

    private final RestTemplate restTemplate;
    @Autowired
    LogService(RestTemplate restTemplate)
    {
        this.restTemplate = restTemplate;
    }

    public void sendLog(String level, String component, String message){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        Date date = new Date(System.currentTimeMillis());
        Log log = new Log();
        log.level = level;
        log.date = formatter.format(date);
        log.component = component;
        log.message = message;

        HttpEntity<Log> request =
                new HttpEntity<Log>(log, headers);
        this.restTemplate.postForObject("http://log-service/log", request, String.class);
    }
}
