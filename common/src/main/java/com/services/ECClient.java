package com.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class ECClient {

    private final RestTemplate restTemplateECClient;

    @Autowired
    public ECClient(RestTemplate restTemplateECClient) {
        this.restTemplateECClient = restTemplateECClient;
    }



    public String putSftp(String path){
        final String url = "http://ext-com-service/sftp_client/put/{id}";
        Map<String, String> param = new HashMap<String, String>();
        param.put("id",path);
        restTemplateECClient.put(url,null, param);
        return "ok";
    }

    public String getSftp(String path){
        final String url = "http://ext-com-service/sftp_client/get/{id}";
        Map<String, String> param = new HashMap<String, String>();
        param.put("id",path);
        restTemplateECClient.getForObject(url, String.class, param);
        return "ok";
    }
}
