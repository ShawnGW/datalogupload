package com.vtest.it.common.tools.FileUpload.cp.impl;

import com.vtest.it.common.pojo.TesterDatalogInformationBean;
import com.vtest.it.common.tools.FileUpload.QueueNeedUpload;
import com.vtest.it.common.tools.parsedatalogtools.DatalogFileNameParser;
import com.vtest.it.common.tools.parsedatalogtools.impl.J750DatalogParser;

import java.io.File;
import java.text.ParseException;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class S200QueueNeedUpload implements QueueNeedUpload {
    @Override
    public void getUploadQueue(ConcurrentLinkedQueue<File> queue, List<File> errorDataList, File[] files) throws ParseException {
        DatalogFileNameParser parser = new J750DatalogParser();
        for (File file : files) {
            TesterDatalogInformationBean bean = parser.getFileInformation(file.getName());
            if (null == bean) {
                errorDataList.add(file);
                continue;
            }
            queue.offer(file);
        }
    }
}
