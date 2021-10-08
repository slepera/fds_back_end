package com;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/sftp_client")
public class RestController {
    @Autowired
    private SFTPClient sftpClient;

    @PutMapping ("/put/{id}")
    public ResponseEntity<String> SFTPClientPut(@PathVariable("id") String id) throws IOException {
        sftpClient.PutFile(id);
        return ResponseEntity.ok("OK");
    }

    @GetMapping ("/get/{id}")
    public void SFTPClientGet(@PathVariable("id") String id) throws IOException {
        sftpClient.GetFile(id);
    }

}
