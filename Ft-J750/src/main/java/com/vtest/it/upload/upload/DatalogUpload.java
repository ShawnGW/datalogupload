package com.vtest.it.upload.upload;

import com.vtest.it.common.tools.Compress.FtFileCompress;
import com.vtest.it.common.tools.Compress.impl.FtCommonFileCompress;
import com.vtest.it.common.tools.ErrorDataDeal.ErrorDataDeal;
import com.vtest.it.common.tools.ErrorDataDeal.impl.CommonErrorDataDeal;
import com.vtest.it.common.tools.FileUpload.QueueNeedUpload;
import com.vtest.it.common.tools.FileUpload.ft.impl.J750QueueNeedUpload;
import com.vtest.it.common.tools.datalogupload.FtFtpUpload;
import com.vtest.it.common.tools.dealreuploadfile.DealFileNeedReUpload;
import com.vtest.it.common.tools.ftdatalogtools.impl.J750DatalogFileNameParser;
import com.vtest.it.common.tools.reupload.GenerateLog;
import com.vtest.it.upload.properties.Properties;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DatalogUpload {
    public static void main(String[] args) throws ParseException, IOException {
        File[] files = new File(Properties.DATASOURCE).listFiles();
        ConcurrentLinkedQueue<File> queueNew = new ConcurrentLinkedQueue<File>();
        List<File> errorDataList = new LinkedList<File>();

        QueueNeedUpload queueNeedUpload = new J750QueueNeedUpload();
        FtFileCompress ftFileCompress = new FtCommonFileCompress();
        ErrorDataDeal commonErrorDataDeal = new CommonErrorDataDeal();
        FtFtpUpload upload = new FtFtpUpload();
        queueNeedUpload.getUploadQueue(queueNew, errorDataList, files);

        commonErrorDataDeal.DataDeal(errorDataList, Properties.ERROR_DATA_PATH);
        ConcurrentLinkedQueue<File> fileNeedUploadQueue = ftFileCompress.compress(queueNew, Properties.BACKUP_PATH, new J750DatalogFileNameParser());
        DealFileNeedReUpload.getFile(Properties.LOG_PATH, fileNeedUploadQueue);
        GenerateLog.generateLog(Properties.LOG_PATH, upload.upload(fileNeedUploadQueue, "/J750/"));
    }
}
