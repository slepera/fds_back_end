package com;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
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
        body.add("file", new FileSystemResource(local_path));
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        String serverUrl = ext_url;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(serverUrl, requestEntity, String.class);
    }


    @GetMapping ("/external/http/get/{int_url}")
    public void HTTPExternalGet(@PathVariable("int_url") String int_url, HttpServletResponse response) throws IOException {
        InputStream inputStream  = new FileInputStream(int_url);
        IOUtils.copy(inputStream, response.getOutputStream());
    }

    @PostMapping ("/external/http/post")
    public ResponseEntity<String> HTTPExternalPost(@RequestParam("file") MultipartFile file) throws IOException {
        if (file == null) {
            throw new RuntimeException("You must select the a file for uploading");
        }
        InputStream inputStream = file.getInputStream();
        String originalName = file.getOriginalFilename();
        String name = file.getName();
        String contentType = file.getContentType();
        long size = file.getSize();
        FileOutputStream outputStream = new FileOutputStream(new File("./data/http/"+originalName));
        outputStream.write(file.getInputStream().readAllBytes());
        outputStream.close();
        return new ResponseEntity<String>(originalName, HttpStatus.OK);
    }

}
