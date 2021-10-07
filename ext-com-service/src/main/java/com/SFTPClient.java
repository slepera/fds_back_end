package com;


import com.services.LogClient;
import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.sftp.client.SftpClient;
import org.apache.sshd.sftp.client.impl.DefaultSftpClientFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class SFTPClient {
    private final LogClient logClient;

    @Autowired
    public SFTPClient(LogClient logClient) {
        this.logClient = logClient;
    }

    @Autowired
    private ECApplicationConfiguration ECApplicationConfiguration;
    public void PutFile(String path) throws IOException {
        SshClient client = SshClient.setUpDefaultClient();
        client.start();
        try (ClientSession session = client.connect(ECApplicationConfiguration.getClient_username(), ECApplicationConfiguration.getClient_host(), ECApplicationConfiguration.getClient_port()).verify(1000).getSession()) {
            session.addPasswordIdentity(ECApplicationConfiguration.getClient_password());
            session.auth().verify();
            try (SftpClient sftp = DefaultSftpClientFactory.INSTANCE.createSftpClient(session)) {
                OutputStream outputStream = sftp.write("data/ftp_server/"+path);
                FileInputStream fileInputStream = new FileInputStream(new File(path));
                int i;
                while((i = fileInputStream.read())!=-1)
                {
                    outputStream.write(i);
                }
                logClient.sendLog("Info", "AuthService",  "New user signed up");
                outputStream.close();
                fileInputStream.close();
                logClient.sendLog("Info", "External Communication Service", "File "+path+" successfully transferred");
            } catch (Exception e){
                logClient.sendLog("Error", "External Communication Service", "Error in file receiving file.");
            }
        }
    }


    public void GetFile(String path) throws IOException {
        SshClient client = SshClient.setUpDefaultClient();
        client.start();
        try (ClientSession session = client.connect(ECApplicationConfiguration.getClient_username(), ECApplicationConfiguration.getClient_host(), ECApplicationConfiguration.getClient_port()).verify(1000).getSession()) {
            session.addPasswordIdentity(ECApplicationConfiguration.getClient_password());
            session.auth().verify();
            try (SftpClient sftp = DefaultSftpClientFactory.INSTANCE.createSftpClient(session)) {
                InputStream inputStream = sftp.read(path);
                FileOutputStream fileOutputStream = new FileOutputStream(new File("data/ftp_client/"+path));
                int i;
                while((i = inputStream.read())!=-1)
                {
                    fileOutputStream.write(i);
                }
                fileOutputStream.close();
                inputStream.close();
                logClient.sendLog("Info", "External Communication Service", "File "+path+" successfully transferred");
            }catch (Exception e){
                logClient.sendLog("Error", "External Communication Service", "Error in file sending file.");
            }
        }
    }





}
