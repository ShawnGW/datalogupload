package com.vtest.it.common.tools.FileUpload.ft.impl;

import com.vtest.it.common.pojo.FtStdfInformationBean;
import com.vtest.it.common.tools.FileUpload.QueueNeedUpload;
import com.vtest.it.common.tools.ftdatalogtools.FtDatalogFileNameParser;
import com.vtest.it.common.tools.ftdatalogtools.impl.J750DatalogFileNameParser;
import com.vtest.it.common.tools.timecheck.TimeCheck;
import com.vtest.it.common.tools.timecheck.TimeCheckImpl;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class V93kQueueNeddUpload implements QueueNeedUpload {
    @Override
    public void getUploadQueue(ConcurrentLinkedQueue<File> queue, List<File> errorDataList, File[] files) {
        TimeCheck timeCheck = new TimeCheckImpl();
        Set<String> fileUploadFlagSet = new HashSet<String>();
        FtDatalogFileNameParser parser = new J750DatalogFileNameParser();
        List<File> list = new LinkedList<File>();
        Map<String, String> keyWordNameMap = new HashMap<String, String>(files.length);
        for (File file : files) {
            if (file.isFile()) {
                String fileName = file.getName();
                FtStdfInformationBean bean = parser.parse(fileName);
                if (null == bean && timeCheck.check(file.lastModified(), 60 * 60 * 24 * 2)) {
                    errorDataList.add(file);
                    continue;
                }
                String suffix = bean.getSuffix();
                String keyWord = fileName.substring(0, fileName.indexOf(suffix) - 17);
                if (suffix.equals("sum") && timeCheck.check(file.lastModified(), 60 * 30)) {
                    fileUploadFlagSet.add(keyWord);
                }
                list.add(file);
                keyWordNameMap.put(fileName, keyWord);
            }
        }
        for (File file : list) {
            String fileName = file.getName();
            String keyWord = keyWordNameMap.get(fileName);
            if (fileUploadFlagSet.contains(keyWord)) {
                queue.offer(file);
            }
        }
    }
}
