package com.vtest.it.common.service;

import org.apache.commons.net.ftp.FTPClient;

import java.io.IOException;

public class FtFtpClientFactory {
    private static ThreadLocal<FTPClient> threadLocal = new ThreadLocal<FTPClient>();
    public static FTPClient ftpClient() {
        if (threadLocal.get() == null) {
            FTPClient client = new FTPClient();
            try {
                client.connect("192.168.10.194", 21);
                client.login("root", "admin123#");
                client.enterLocalPassiveMode();
                client.setConnectTimeout(1000000);
                client.setBufferSize(1024 * 1024);
                client.setControlEncoding("GBK");
                client.setFileType(FTPClient.BINARY_FILE_TYPE);
            } catch (IOException e) {
                client = null;
            }
            threadLocal.set(client);
        }
        return threadLocal.get();
    }
    public static void backClient() {
        FTPClient client = threadLocal.get();
        try {
            client.disconnect();
            threadLocal.set(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
