package com.vtest.it.common.tools.Compress.impl;

import com.vtest.it.common.tools.Compress.FileCompress;
import com.vtest.it.common.tools.parsedatalogtools.DatalogFileNameParser;
import org.apache.commons.io.FileUtils;
import org.zeroturnaround.zip.ZipUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.*;

public class GowFileCompressImpl implements FileCompress {

    private final SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");

    @Override
    public ConcurrentLinkedQueue<File> compress(ConcurrentLinkedQueue<File> queue, final String backupPath, DatalogFileNameParser parser) {
        final File lastFile = getFileNeedUploadList(queue);
        ExecutorService service = Executors.newFixedThreadPool(3);
        final ConcurrentLinkedQueue<File> fileNeedUploadQueue = new ConcurrentLinkedQueue<File>();
        List<Future<String>> list = new LinkedList<Future<String>>();
        while (!queue.isEmpty()) {
            final File dataLog = queue.poll();
            Future<String> future = service.submit(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    if (null != dataLog) {
                        String fileName = dataLog.getName();
                        File backupDirectory = new File(backupPath);
                        if (!backupDirectory.exists()) {
                            backupDirectory.mkdirs();
                        }
                        File backupFile = new File(backupDirectory.getPath() + "/" + fileName + ".zip");
                        System.out.println(dataLog.getName());
                        ZipUtil.packEntry(dataLog, backupFile);
                        if (ZipUtil.containsEntry(backupFile, fileName)) {
                            fileNeedUploadQueue.offer(backupFile);
                            if (lastFile != dataLog) {
                                FileUtils.forceDelete(dataLog);
                            }
                        }
                        System.out.println(backupFile.getName() + ": " + backupFile.length());
                    }
                    return dataLog.getName() + " Finished";
                }
            });
            list.add(future);
        }
        for (Future<String> future : list) {
            try {
                System.err.println(future.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        service.shutdown();
        return fileNeedUploadQueue;
    }

    public File getFileNeedUploadList(ConcurrentLinkedQueue<File> queue) {
        TreeMap<Long, File> map = new TreeMap<Long, File>();
        for (File csv : queue) {
            try {
                String fileName = csv.getName();
                String[] nameTokens = fileName.substring(0, fileName.length() - 4).split("_");
                String fileDate = nameTokens[nameTokens.length - 1];
                map.put(format.parse(fileDate).getTime(), csv);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return map.lastEntry().getValue();
    }
}
