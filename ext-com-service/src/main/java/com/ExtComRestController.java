package com;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/ext_com")
public class ExtComRestController {
    @Autowired
    private SFTPClient sftpClient;

    @PutMapping ("/sftp/put/{id}")
    public ResponseEntity<String> SFTPClientPut(@PathVariable("id") String id) throws IOException {
        sftpClient.PutFile(id);
        return ResponseEntity.ok("OK");
    }

    @GetMapping ("/sftp/get/{id}")
    public void SFTPClientGet(@PathVariable("id") String id) throws IOException {
        sftpClient.GetFile(id);
    }

    @GetMapping("/internal/http/get")
    public void HTTPInternalGet(@RequestParam("ext_url") String ext_url) {
        RestTemplate restTemplate = new RestTemplate();
        File file = restTemplate.execute(ext_url, HttpMethod.GET, null, clientHttpResponse -> {
            String[] tmp = ext_url.split("/");
            File ret = new File(tmp[tmp.length-1]);
            FileOutputStream fileOutputStream = new FileOutputStream(ret);
            StreamUtils.copy(clientHttpResponse.getBody(), fileOutputStream);
            fileOutputStream.close();
            return ret;
        });
    }

    @PostMapping ("/internal/http/post")
    public void HTTPInternalPost(@RequestParam("ext_url") String ext_url, @RequestParam("local_path") String local_path) throws IOException {
        System.out.println(ext_url);
        System.out.println(local_path);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new File(local_path));
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        String serverUrl = ext_url;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(serverUrl, requestEntity, String.class);
    }


    @GetMapping ("/external/http/get/{int_url}")
    public void HTTPExternalGet(@PathVariable("ext_url") String int_url) throws IOException {
    }

    @PostMapping ("/external/http/post/{int_url}")
    public void HTTPExternalPost(@PathVariable("ext_url") String int_url) throws IOException {
    }

}
