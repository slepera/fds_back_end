package com.services;

import com.entities.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LogService {

    static public Log createLog (String level, String component, String message){
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        Date date = new Date(System.currentTimeMillis());
        Log log = new Log();
        log.level = level;
        log.date = formatter.format(date);
        log.component = component;
        log.message = message;
        return log;
    }
}
