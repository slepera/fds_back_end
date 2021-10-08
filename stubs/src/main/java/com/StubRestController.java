package com;

import com.services.ECClient;
import com.services.LogClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StubRestController {
    private final LogClient logClient;
    private final ECClient ecClient;

    @Autowired
    public StubRestController(LogClient logClient, ECClient ecClient) {
        this.logClient = logClient;
        this.ecClient = ecClient;
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
