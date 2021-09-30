package com.services;

import com.entities.Log;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LogService {

    static public HttpEntity<Log> createLog (String level, String component, String message){
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
        return request;
    }
}
