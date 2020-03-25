package com.vtest.it.common.tools.Compress.impl;

import com.vtest.it.common.pojo.FtStdfInformationBean;
import com.vtest.it.common.tools.Compress.FtFileCompress;
import com.vtest.it.common.tools.PerfectFileName;
import com.vtest.it.common.tools.ftdatalogtools.FtDatalogFileNameParser;
import org.apache.commons.io.FileUtils;
import org.zeroturnaround.zip.ZipUtil;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

public class FtCommonFileCompress implements FtFileCompress {
    @Override
    public ConcurrentLinkedQueue<File> compress(final ConcurrentLinkedQueue<File> queue, final String backupPath, final FtDatalogFileNameParser parser) {
        final ConcurrentLinkedQueue<File> fileNeedUploadQueue = new ConcurrentLinkedQueue<File>();
        ExecutorService service = Executors.newFixedThreadPool(3);
        List<Future<String>> list=new LinkedList<Future<String>>();
        while (!queue.isEmpty()) {
            Future<String> future = service.submit(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    File dataLog = queue.poll();
                    if (null != dataLog) {
                        String fileName = PerfectFileName.remove(PerfectFileName.removeBrackets(dataLog.getName()));
                        FtStdfInformationBean bean = parser.parse(fileName);
                        File backupDirectory = new File(backupPath + "/" + bean.getCustomerCode() + "/" + bean.getDevice() + "/" + bean.getLot() + "/" + bean.getFtStep() + "/" + bean.getVLot());
                        if (!backupDirectory.exists()) {
                            backupDirectory.mkdirs();
                        }
                        File backupFile = new File(backupDirectory.getPath() + "/" + fileName + ".zip");
                        System.out.println(dataLog.getName());
                        ZipUtil.packEntry(dataLog, backupFile);
                        if (ZipUtil.containsEntry(backupFile, fileName)) {
                            fileNeedUploadQueue.offer(backupFile);
                            FileUtils.forceDelete(dataLog);
                        }
                        System.out.println(backupFile.getName() + ": " + backupFile.length());
                    }
                    return "finished";
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
