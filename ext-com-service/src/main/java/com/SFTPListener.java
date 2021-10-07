package com;


import org.apache.sshd.server.session.ServerSession;
import org.apache.sshd.sftp.server.FileHandle;
import org.apache.sshd.sftp.server.Handle;
import org.apache.sshd.sftp.server.SftpEventListener;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

public class SFTPListener implements SftpEventListener {

    static int file_size;
    static int data_len;

    @Override
    public void initialized(ServerSession session, int version) throws IOException {
        System.out.println("sftp session initialized");
    }

    @Override
    public void writing(ServerSession session, String remoteHandle, FileHandle localHandle, long offset, byte[] data, int dataOffset, int dataLen){
        String[] file = localHandle.toString().split(Pattern.quote(File.separator));
        if (SFTPListener.data_len == 0){
            System.out.println("Start receiving file: "+file[file.length-1] + " with path "+ localHandle.toString());
        }
    }

    @Override
    public void written(ServerSession session, String remoteHandle, FileHandle localHandle, long offset, byte[] data, int dataOffset, int dataLen, Throwable thrown){
        SFTPListener.data_len += dataLen;
    }

    @Override
    public void closing(ServerSession session, String remoteHandle, Handle localHandle){
        System.out.println("File size: " +  SFTPListener.data_len);
        SFTPListener.data_len = 0;
    }

    @Override
    public void reading(ServerSession session, String remoteHandle, FileHandle localHandle, long offset, byte[] data, int dataOffset, int dataLen){
        String[] file = localHandle.toString().split(Pattern.quote(File.separator));
        if (SFTPListener.data_len ==0) {
            System.out.println("Start sending file: " + file[file.length - 1] + " with path " + localHandle.toString());
        }
    }

    @Override
    public void read(ServerSession session, String remoteHandle, FileHandle localHandle, long offset, byte[] data, int dataOffset, int dataLen, int readLen, Throwable thrown){
        SFTPListener.data_len += readLen;
    }

    @Override
    public void destroying(ServerSession session){
        System.out.println("Closing sftp session.");
    }

}
