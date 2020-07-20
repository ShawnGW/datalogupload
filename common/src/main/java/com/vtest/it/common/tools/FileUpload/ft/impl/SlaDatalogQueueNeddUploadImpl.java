package com.vtest.it.common.tools.FileUpload.ft.impl;

import com.vtest.it.common.pojo.SlaBean;
import com.vtest.it.common.tools.FileUpload.QueueNeedUploadWithInfor;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author shawn.sun
 * @date 2020/07/17  14:59
 */
public class SlaDatalogQueueNeddUploadImpl implements QueueNeedUploadWithInfor {
    @Override
    public Map<File, Object> getUploadQueue(ConcurrentLinkedQueue<File> queue, List<File> errorDataList, File[] files) throws ParseException {
        Map<File, Object> map = new HashMap<File, Object>();
        for (File file : files) {
            if (checkDirectory(file)) {
                String deviceName = file.getName();
                File[] vtLots = file.listFiles();
                for (File vtLot : vtLots) {
                    if (checkDirectory(vtLot)) {
                        String vtlotName = vtLot.getName();
                        File[] customerLots = vtLot.listFiles();
                        for (File customerLot : customerLots) {
                            if (checkDirectory(customerLot)) {
                                String customerLotName = customerLot.getName();
                                File[] dateCodes = customerLot.listFiles();
                                for (File dateCode : dateCodes) {
                                    if (checkDirectory(dateCode)) {
                                        String dateCodeName = dateCode.getName();
                                        File[] datalogs = dateCode.listFiles();
                                        if (checkDatalog(datalogs)) {
                                            for (File datalog : datalogs) {
                                                SlaBean slaBean = new SlaBean();
                                                slaBean.setDevice(deviceName);
                                                slaBean.setCustomerCode(customerLotName);
                                                slaBean.setvLot(vtlotName);
                                                slaBean.setDataCode(dateCodeName);
                                                map.put(datalog, slaBean);
                                                queue.add(datalog);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return map;
    }

    private boolean checkDirectory(File file) {
        try {
            if (file.isFile()) {
                FileUtils.forceDelete(file);
                return false;
            }
            if (file.listFiles().length == 0) {
                FileUtils.forceDelete(file);
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    private boolean checkDatalog(File[] files) {
        boolean blogFlag = false;
        boolean sumBlog = false;
        if (files.length == 2) {
            for (File file : files) {
                if (file.getName().endsWith(".blog") && fileDateCheck(file, 60 * 10)) {
                    blogFlag = true;
                    continue;
                }
                if (file.getName().endsWith("SUM.txt") && fileDateCheck(file, 60 * 10)) {
                    sumBlog = true;
                }
            }
            if (blogFlag && sumBlog) {
                return true;
            }
        }
        return false;
    }

    private boolean fileDateCheck(File file, long time) {
        long currentTimeMillis = System.currentTimeMillis();
        long fileTime = file.lastModified();
        if (((currentTimeMillis - fileTime) / 1000) > time) {
            return true;
        }
        return false;
    }
}
