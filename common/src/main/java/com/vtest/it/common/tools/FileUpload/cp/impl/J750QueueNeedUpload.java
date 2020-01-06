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
        Set<String> keySet = map.keySet();
        for (String keySuffix : keySet) {
            boolean flag=false;
            if (keySuffix.equals("stdf")){
                flag=true;
            }
            TreeMap<Long, File> fileMap = (TreeMap<Long, File>) map.get(keySuffix);
            long lastKey = fileMap.lastKey();
            for (Map.Entry<Long, File> entry : fileMap.entrySet()) {
                if (flag&&lastKey==entry.getKey()){
                    if (!map.containsKey("txt")){
                        break;
                    }
                    File summary=((TreeMap<Long, File>)map.get("txt")).lastEntry().getValue();
                    if (null==summary){
                        break;
                    }
                    File stdf=entry.getValue();
                    if (stdf.getName().substring(0,stdf.getName().indexOf(".")).equals(summary.getName().substring(0,summary.getName().indexOf("."))))
                    {
                        queue.offer(entry.getValue());
                    }
                    break;
                }
                queue.offer(entry.getValue());
            }
        }
    }
}
