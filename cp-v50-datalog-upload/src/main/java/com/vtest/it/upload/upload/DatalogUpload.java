package com.vtest.it.upload.upload;

import com.vtest.it.common.tools.Compress.impl.CommonFileCompress;
import com.vtest.it.common.tools.ErrorDataDeal.ErrorDataDeal;
import com.vtest.it.common.tools.ErrorDataDeal.impl.CommonErrorDataDeal;
import com.vtest.it.common.tools.FileUpload.cp.impl.V50QueueNeedUpload;
import com.vtest.it.common.tools.datalogupload.FtpUpload;
import com.vtest.it.common.tools.dealreuploadfile.DealFileNeedReUpload;
import com.vtest.it.common.tools.parsedatalogtools.impl.V50DatalogParser;
import com.vtest.it.common.tools.reupload.GenerateLog;
import com.vtest.it.upload.properties.CommonProperties;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author shawn.sun
 * @date 2020/06/08  13:21
 */
public class DatalogUpload {
    public static void main(String[] args) throws ParseException, IOException {
        if (!new File(CommonProperties.DATASOURCE).exists()) {
            return;
        }
        File[] files = new File(CommonProperties.DATASOURCE).listFiles();
        if (files.length == 0) {
            return;
        }

        ConcurrentLinkedQueue<File> queue = new ConcurrentLinkedQueue<File>();
        List<File> errorDataList = new LinkedList<File>();

        FtpUpload upload = new FtpUpload();
        V50QueueNeedUpload v50QueueNeedUpload = new V50QueueNeedUpload();
        ErrorDataDeal commonErrorDataDeal = new CommonErrorDataDeal();
        CommonFileCompress commonFileCompress = new CommonFileCompress();
        v50QueueNeedUpload.getUploadQueue(queue, errorDataList, files);
        commonErrorDataDeal.DataDeal(errorDataList, CommonProperties.ERRORDATAPATH);
        ConcurrentLinkedQueue<File> fileNeedUploadQueue = commonFileCompress.compress(queue, CommonProperties.BACKUPPATH, new V50DatalogParser());
        DealFileNeedReUpload.getFile(CommonProperties.LOGPATH, fileNeedUploadQueue);
        GenerateLog.generateLog(CommonProperties.LOGPATH, upload.upload(fileNeedUploadQueue, "/V50/"));
    }
}
