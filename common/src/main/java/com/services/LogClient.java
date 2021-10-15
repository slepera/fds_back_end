package com.services;

import com.entities.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class LogClient {
    @Value("${spring.application.name}")
    private String appName;
    private final RestTemplate restTemplateLogClient;

    @Autowired
    public LogClient(RestTemplate restTemplateLogClient) {
        this.restTemplateLogClient = restTemplateLogClient;
    }



    public void sendLog(String level, String message)
    {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        Date date = new Date(System.currentTimeMillis());
        Log log = new Log();
        log.level = level;
        log.date = formatter.format(date);
        log.component = appName;
        log.message = message;

        HttpEntity<Log> request =
                new HttpEntity<Log>(log, headers);
        this.restTemplateLogClient.postForObject("http://log-service/log", request, String.class);
    }
}
