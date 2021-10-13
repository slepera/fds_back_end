package com.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
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



    public ResponseEntity<String> putSftp(String path){
        final String url = "http://ext-com-service/ext_com/sftp/put?";
        //restTemplateECClient.put(url+"path="+path,null);
        ResponseEntity<String> response = restTemplateECClient.exchange(url+"path="+path, HttpMethod.PUT, null, String.class);
        return response;
    }

    public ResponseEntity<String> getSftp(String path){
        final String url = "http://ext-com-service/ext_com/sftp/get?";
        ResponseEntity<String> response = restTemplateECClient.exchange(url+"path="+path, HttpMethod.GET, null, String.class);
        return response;
    }
}
