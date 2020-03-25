package com.vtest.it.common.tools.datalogupload;

import com.vtest.it.common.service.FtFtpClientFactory;
import com.vtest.it.common.tools.PerfectFileName;
import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

public class FtFtpUpload {
    public ConcurrentLinkedQueue<File> upload(final ConcurrentLinkedQueue<File> queue, final String path) {
        final ConcurrentLinkedQueue<File> resultQueue = new ConcurrentLinkedQueue<File>();
        ExecutorService service = Executors.newFixedThreadPool(10);
        List<Future<String>> futures = new LinkedList<Future<String>>();
        for (int i = 0; i < 10; i++) {
            if (queue.isEmpty()) {
                break;
            }
            Future<String> future = service.submit(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    while (!queue.isEmpty()) {
                        File dataLog = queue.poll();
                        String dataLogName = PerfectFileName.remove(PerfectFileName.removeBrackets(dataLog.getName()));
                        FTPClient client = FtFtpClientFactory.ftpClient();
                        if (null != client) {
                            try {
                                client.changeWorkingDirectory(path);
                                client.storeFile(dataLogName + ".temp", new FileInputStream(dataLog));
                                client.rename(dataLogName + ".temp", dataLogName);
                                System.out.println(dataLogName + ": upload successfully!");
                                FtFtpClientFactory.backClient();
                            } catch (IOException e) {
                                resultQueue.offer(dataLog);
                            }
                        } else {
                            resultQueue.offer(dataLog);
                        }
                    }
                    return "true";
                }
            });
            futures.add(future);
        }
        for (Future<String> future : futures) {
            try {
                future.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        service.shutdown();
        return resultQueue;
    }
}
