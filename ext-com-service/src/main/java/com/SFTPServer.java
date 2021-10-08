package com;


import com.services.LogClient;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.sftp.server.SftpSubsystemFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;


@Service
public class SFTPServer {
    private final LogClient logClient;
    private final ECApplicationConfiguration ecApplicationConfiguration;

    @Autowired
    public SFTPServer(LogClient logClient, ECApplicationConfiguration ecApplicationConfiguration) throws IOException {
        this.logClient = logClient;
        this.ecApplicationConfiguration = ecApplicationConfiguration;
    }

    @PostConstruct
    public void Initialize() throws IOException
    {

        SshServer sshd = SshServer.setUpDefaultServer();
        sshd.setHost(ecApplicationConfiguration.getServer_host());
        sshd.setPort(ecApplicationConfiguration.getServer_port());
        Path path = Paths.get("host.ser");
        //Path path2 = Paths.get("host.ser");

        sshd.setKeyPairProvider(new SimpleGeneratorHostKeyProvider(path));
        SftpSubsystemFactory factory = new SftpSubsystemFactory();
        factory.addSftpEventListener(new SFTPListener(logClient));
        sshd.setSubsystemFactories(Collections.singletonList(factory));
        sshd.setPasswordAuthenticator((username, password, session) -> username.equals(ecApplicationConfiguration.getServer_username()) && password.equals(ecApplicationConfiguration.getServer_password()));
        //sshd.setPublickeyAuthenticator(new AuthorizedKeysAuthenticator(path2));
        sshd.start();
    }

}
