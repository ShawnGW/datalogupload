package com.vtest.it.common.tools.FileUpload.cp.impl;

import com.vtest.it.common.pojo.TesterDatalogInformationBean;
import com.vtest.it.common.tools.FileUpload.QueueNeedUpload;
import com.vtest.it.common.tools.parsedatalogtools.DatalogFileNameParser;
import com.vtest.it.common.tools.parsedatalogtools.impl.V50DatalogParser;

import java.io.File;
import java.text.ParseException;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author shawn.sun
 * @date 2020/06/08  13:23
 */
public class V50QueueNeedUpload implements QueueNeedUpload {
    @Override
    public void getUploadQueue(ConcurrentLinkedQueue<File> queue, List<File> errorDataList, File[] files) throws ParseException {
        DatalogFileNameParser parser = new V50DatalogParser();
        for (File file : files) {
            if (file.isFile()) {
                TesterDatalogInformationBean bean = parser.getFileInformation(file.getName());
                if (null == bean) {
                    errorDataList.add(file);
                    continue;
                }
                queue.offer(file);
            }
        }
    }
}
