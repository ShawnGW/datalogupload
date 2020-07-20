package com.vtest.it.common.tools.Compress.impl;

import com.vtest.it.common.pojo.SlaBean;
import com.vtest.it.common.tools.Compress.FileCompressWithInformation;
import com.vtest.it.common.tools.PerfectFileName;
import org.apache.commons.io.FileUtils;
import org.zeroturnaround.zip.ZipUtil;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author shawn.sun
 * @date 2020/07/17  16:04
 */
public class SlaFileCompress implements FileCompressWithInformation {
    @Override
    public ConcurrentLinkedQueue<File> compress(ConcurrentLinkedQueue<File> queue, final String backupPath, final Map<File, Object> map) {
        int processors = Runtime.getRuntime().availableProcessors();
        final ConcurrentLinkedQueue<File> res = new ConcurrentLinkedQueue<File>();
        ExecutorService service = new ThreadPoolExecutor(processors, processors, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(), new ThreadPoolExecutor.CallerRunsPolicy());
        List<Future<String>> list = new LinkedList<Future<String>>();
        while (!queue.isEmpty()) {
            final File dataLog = queue.poll();
            Future<String> future = service.submit(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    if (null != dataLog) {
                        String fileName = PerfectFileName.remove(PerfectFileName.removeBrackets(dataLog.getName()));
                        SlaBean bean = ((SlaBean) map.get(dataLog));
                        File backupDirectory = new File(backupPath + "/" + bean.getDevice() + "/" + bean.getvLot() + "/" + bean.getCustomerCode() + "/" + bean.getDataCode());
                        if (!backupDirectory.exists()) {
                            backupDirectory.mkdirs();
                        }
                        File backupFile = new File(backupDirectory.getPath() + "/" + fileName + ".zip");
                        System.out.println(dataLog.getName());
                        ZipUtil.packEntry(dataLog, backupFile);
                        if (ZipUtil.containsEntry(backupFile, dataLog.getName())) {
                            res.offer(backupFile);
                            FileUtils.forceDelete(dataLog);
                        }
                        System.out.println(backupFile.getName() + ": " + backupFile.length());
                    }
                    return dataLog.getName() + " Finished";
                }
            });
            list.add(future);
        }
        service.shutdown();
        for (Future<String> future : list) {
            try {
                future.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return res;
    }
}
