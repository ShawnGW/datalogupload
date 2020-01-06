package com.vtest.it.common.tools.Compress.impl;

import com.vtest.it.common.pojo.TesterDatalogInformationBean;
import com.vtest.it.common.tools.Compress.FileCompress;
import com.vtest.it.common.tools.PerfectFileName;
import com.vtest.it.common.tools.parsedatalogtools.DatalogFileNameParser;
import org.apache.commons.io.FileUtils;
import org.zeroturnaround.zip.ZipUtil;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

public class CommonFileCompress implements FileCompress {
    public ConcurrentLinkedQueue<File> compress(final ConcurrentLinkedQueue<File> queue, final String backupPath, final DatalogFileNameParser parser) {
        ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2 + 1);
        final ConcurrentLinkedQueue<File> fileNeedUploadQueue = new ConcurrentLinkedQueue<File>();
        List<Future<String>> list = new LinkedList<Future<String>>();
        while (!queue.isEmpty()) {
            Future<String> future = service.submit(new Callable<String>() {
                public String call() throws Exception {
                    File dataLog = queue.poll();
                    if (null != dataLog) {
                        String fileName = PerfectFileName.remove(PerfectFileName.removeBrackets(dataLog.getName()));
                        TesterDatalogInformationBean bean = parser.getFileInformation(fileName);
                        File backupDirectory = new File(backupPath + "/" + bean.getCustomCode() + "/" + bean.getDevice() + "/" + bean.getLot() + "/" + bean.getCpStep() + "/" + bean.getWaferId());
                        if (!backupDirectory.exists()) {
                            backupDirectory.mkdirs();
                        }
                        File backupFile = new File(backupDirectory.getPath() + "/" + fileName + ".zip");
                        ZipUtil.packEntry(dataLog, backupFile);
                        fileNeedUploadQueue.offer(backupFile);
                        FileUtils.forceDelete(dataLog);
                    }
                    return "Finished";
                }
            });
            list.add(future);
        }
        for (Future<String> future : list) {
            try {
                future.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        service.shutdown();
        return fileNeedUploadQueue;
    }
}
