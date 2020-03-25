package com.vtest.it.common.tools.FileUpload.ft.impl;

import com.vtest.it.common.tools.FileUpload.QueueNeedUpload;
import com.vtest.it.common.tools.timecheck.TimeCheck;
import com.vtest.it.common.tools.timecheck.TimeCheckImpl;

import java.io.File;
import java.text.ParseException;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class HandlerAsetQueueNeedUpload implements QueueNeedUpload {
    @Override
    public void getUploadQueue(ConcurrentLinkedQueue<File> queue, List<File> errorDataList, File[] files) throws ParseException {
        TimeCheck timeCheck = new TimeCheckImpl();
        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".SUM") && timeCheck.check(file.lastModified(), 60)) {
                queue.add(file);
            }
        }
    }
}
