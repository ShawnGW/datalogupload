package com.vtest.it.common.tools.FileUpload.cp.impl;

import com.vtest.it.common.pojo.TesterDatalogInformationBean;
import com.vtest.it.common.tools.FileUpload.QueueNeedUpload;
import com.vtest.it.common.tools.parsedatalogtools.DatalogFileNameParser;
import com.vtest.it.common.tools.parsedatalogtools.impl.J750DatalogParser;
import com.vtest.it.common.tools.timeParse.TimeParser;
import com.vtest.it.common.tools.timeParse.imple.J750TimeParser;

import java.io.File;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class J750QueueNeedUpload implements QueueNeedUpload {
    @Override
    public void getUploadQueue(ConcurrentLinkedQueue<File> queue, List<File> errorDataList, File[] files) throws ParseException {
        HashMap<String, Map<Long, File>> map = new HashMap<String, Map<Long, File>>();
        DatalogFileNameParser parser = new J750DatalogParser();
        TimeParser timeParser = new J750TimeParser();
        for (File file : files) {
            if (file.isFile()) {
                TesterDatalogInformationBean bean = parser.getFileInformation(file.getName());
                if (null == bean) {
                    errorDataList.add(file);
                    continue;
                }
                String fileSuffix = bean.getSuffix();
                if (map.containsKey(fileSuffix)) {
                    map.get(fileSuffix).put(timeParser.parse(bean.getTestTime()), file);
                } else {
                    Map<Long, File> fileMap = new TreeMap<Long, File>();
                    fileMap.put(timeParser.parse(bean.getTestTime()), file);
                    map.put(fileSuffix, fileMap);
                }
            }
        }
        Set<String> keySet = map.keySet();
        for (String keySuffix : keySet) {
            TreeMap<Long, File> fileMap = (TreeMap<Long, File>) map.get(keySuffix);
            long lastKey = fileMap.lastKey();
            for (Map.Entry<Long, File> entry : fileMap.entrySet()) {
                if (lastKey == entry.getKey()) {
                    break;
                }
                queue.offer(entry.getValue());
            }
        }
    }
}
