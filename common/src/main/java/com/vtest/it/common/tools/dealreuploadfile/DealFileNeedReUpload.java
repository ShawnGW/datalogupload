package com.vtest.it.common.tools.dealreuploadfile;

import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DealFileNeedReUpload {
    public static void getFile(String logPath, ConcurrentLinkedQueue<File> fileNeedUploadQueue) throws IOException {
        File fileNeedReUploadLog=new File(logPath);
        if (fileNeedReUploadLog.exists()){
            FileReader in = new FileReader(fileNeedReUploadLog);
            BufferedReader reader = new BufferedReader(in);
            String content = null;
            while ((content = reader.readLine()) != null) {
                File file = new File(content);
                fileNeedUploadQueue.offer(file);
            }
            in.close();
            reader.close();
            FileUtils.forceDelete(fileNeedReUploadLog);
        }
    }
}
