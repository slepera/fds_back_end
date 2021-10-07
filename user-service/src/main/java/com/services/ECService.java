package com.services;

import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class ECService {

    //@PutMapping(value = "/put/{id}")
    //public String provaSftp(@PathVariable("id") String id){ return userService.provaSftp(id);}

    public String putSftp(String path){
        final String url = "http://ext-com-service/sftp_client/put/{id}";
        Map<String, String> param = new HashMap<String, String>();
        param.put("id",path);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.put(url,null, param);
        return "ok";
    }
}
