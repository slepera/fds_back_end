package com;

import com.services.ECClient;
import com.services.LogClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@RestController
public class StubRestController {
    private final LogClient logClient;
    private final ECClient ecClient;

    @Autowired
    public StubRestController(LogClient logClient, ECClient ecClient) {
        this.logClient = logClient;
        this.ecClient = ecClient;
    }


    @PostMapping(value = "/test/http_post_file_server")
    public ResponseEntity<String> http_post_server(@RequestParam("file") MultipartFile file) throws IOException {
        if (file == null) {
            throw new RuntimeException("You must select the a file for uploading");
        }
        InputStream inputStream = file.getInputStream();
        String originalName = file.getOriginalFilename();
        String name = file.getName();
        String contentType = file.getContentType();
        long size = file.getSize();
        FileOutputStream outputStream = new FileOutputStream(new File("/data/stub/http/"+name));
        outputStream.write(file.getInputStream().readAllBytes());
        outputStream.close();
        return new ResponseEntity<String>("originalName", HttpStatus.OK);
    }

     @GetMapping(value="/test/http_external_get/{path}")
     public void http_get_client(@PathVariable String path) throws IOException{
         RestTemplate restTemplate = new RestTemplate();
         System.out.println(path);
         String[] extension = path.split("\\.");
         File file = restTemplate.execute("http://localhost:9003/ext_com/external/http/get/"+path, HttpMethod.GET, null, clientHttpResponse -> {
             File ret;
             if(extension.length>1) {
                 ret = new File("file_sent_through_get" + "." + extension[extension.length - 1]);
             }else{
                 ret = new File("file_sent_through_get");
             }
             FileOutputStream fileOutputStream = new FileOutputStream(ret);
             StreamUtils.copy(clientHttpResponse.getBody(), fileOutputStream);
             fileOutputStream.close();
             return ret;
         });
     }

    @PutMapping(value = "/test/sftp_put")
    public ResponseEntity<String> sftp_put(@RequestParam("path") String path) {
        return ecClient.putSftp(path);
    }

    @GetMapping(value = "/test/sftp_get")
    public ResponseEntity<String> sftp_get(@RequestParam("path") String path) {
        return ecClient.getSftp(path);
    }

    @GetMapping(value = "/test/log")
    public String testLog(){
        this.logClient.sendLog( "Info", "StubService",  "TestLog");
        return "Test log ok";
    }
}
