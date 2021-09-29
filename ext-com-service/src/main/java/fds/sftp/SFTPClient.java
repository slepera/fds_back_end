package fds.sftp;


import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.sftp.client.SftpClient;
import org.apache.sshd.sftp.client.impl.DefaultSftpClient;
import org.apache.sshd.sftp.client.impl.DefaultSftpClientFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

@Service
public class SFTPClient {
    @Autowired
    private SftpApplicationConfiguration sftpApplicationConfiguration;
    public void PutFile(String path) throws IOException {
        SshClient client = SshClient.setUpDefaultClient();
        client.start();
        try (ClientSession session = client.connect(sftpApplicationConfiguration.getClient_username(), sftpApplicationConfiguration.getClient_host(), sftpApplicationConfiguration.getClient_port()).verify(1000).getSession()) {
            session.addPasswordIdentity(sftpApplicationConfiguration.getClient_password());
            session.auth().verify();
            try (SftpClient sftp = DefaultSftpClientFactory.INSTANCE.createSftpClient(session)) {
                OutputStream outputStream = sftp.write("./data/ftp_server/"+path);
                FileInputStream fileInputStream = new FileInputStream(new File(path));
                int i;
                while((i = fileInputStream.read())!=-1)
                {
                    outputStream.write(i);
                }
                outputStream.close();
                fileInputStream.close();
            }
        }
    }


    public void GetFile(String path) throws IOException {
        SshClient client = SshClient.setUpDefaultClient();
        client.start();
        try (ClientSession session = client.connect(sftpApplicationConfiguration.getClient_username(), sftpApplicationConfiguration.getClient_host(), sftpApplicationConfiguration.getClient_port()).verify(1000).getSession()) {
            session.addPasswordIdentity(sftpApplicationConfiguration.getClient_password());
            session.auth().verify();
            try (SftpClient sftp = DefaultSftpClientFactory.INSTANCE.createSftpClient(session)) {
                InputStream inputStream = sftp.read(path);
                FileOutputStream fileOutputStream = new FileOutputStream(new File("./data/ftp_client/"+path));
                int i;
                while((i = inputStream.read())!=-1)
                {
                    fileOutputStream.write(i);
                }
                fileOutputStream.close();
                inputStream.close();
            }
        }
    }





}
