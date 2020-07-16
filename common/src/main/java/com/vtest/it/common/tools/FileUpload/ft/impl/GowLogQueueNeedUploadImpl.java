package com.vtest.it.common.tools.FileUpload.ft.impl;

import com.vtest.it.common.tools.FileUpload.QueueNeedUpload;
import com.vtest.it.common.tools.timecheck.TimeCheckImpl;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author shawn.sun
 * @date 2020/04/29  14:04
 */
public class GowLogQueueNeedUploadImpl implements QueueNeedUpload {

    @Override
    public void getUploadQueue(ConcurrentLinkedQueue<File> queue, List<File> errorDataList, File[] files) throws ParseException {
        TimeCheckImpl timeCheck = new TimeCheckImpl();
        if (files.length == 0) {
            return;
        }
        for (File file : files) {
            File device = file;
            try {
                if (checkAndUpdateDirectory(device)) {
                    File[] lots = device.listFiles();
                    for (File lot : lots) {
                        if (checkAndUpdateDirectory(lot)) {
                            File[] csvs = lot.listFiles();
                            for (File csv : csvs) {
                                if (csv.isFile()) {
                                    if (timeCheck.check(csv.lastModified(), 30 * 60)) {
                                        queue.offer(csv);
                                    }
                                } else {
                                    FileUtils.deleteDirectory(csv);
                                }
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean checkAndUpdateDirectory(File file) throws IOException {
        if (file.isDirectory() && file.listFiles().length > 0) {
            return true;
        } else {
            FileUtils.forceDelete(file);
            return false;
        }
    }

}
