package com.vtest.it.common.tools.reupload;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ConcurrentLinkedQueue;

public class GenerateLog {
    public static void generateLog(String path,ConcurrentLinkedQueue<File> resultQueue) throws IOException {
        if (!resultQueue.isEmpty()){
            File file=new File(path);
            if (!file.getParentFile().exists()){
                file.getParentFile().mkdirs();
            }
            PrintWriter printWriter=new PrintWriter(file);
            for (File dataLog : resultQueue) {
                printWriter.print(dataLog.getPath()+"\r\n");
            }
            printWriter.flush();
            printWriter.close();
        }
    }
}
