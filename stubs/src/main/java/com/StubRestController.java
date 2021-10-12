package com;

import com.services.ECClient;
import com.services.LogClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
        FileOutputStream outputStream = new FileOutputStream(new File("file_received_through_post"));
        outputStream.write(file.getInputStream().readAllBytes());
        outputStream.close();
        return new ResponseEntity<String>("originalName", HttpStatus.OK);
    }

    @PutMapping(value = "/test/sftp_put/{path}")
    public String sftp_put(@PathVariable String path) {
        return ecClient.putSftp(path);
    }

    @GetMapping(value = "/test/sftp_get/{path}")
    public String sftp_get(@PathVariable String path) {
        return ecClient.getSftp(path);
    }

    @GetMapping(value = "/test/log")
    public String testLog(){
        this.logClient.sendLog( "Info", "StubService",  "TestLog");
        return "Test log ok";
    }
}
