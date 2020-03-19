package com.vtest.it.upload.upload;

import com.vtest.it.common.tools.Compress.impl.CommonFileCompress;
import com.vtest.it.common.tools.ErrorDataDeal.ErrorDataDeal;
import com.vtest.it.common.tools.ErrorDataDeal.impl.CommonErrorDataDeal;
import com.vtest.it.common.tools.FileUpload.cp.impl.S200QueueNeedUpload;
import com.vtest.it.common.tools.datalogupload.FtpUpload;
import com.vtest.it.common.tools.dealreuploadfile.DealFileNeedReUpload;
import com.vtest.it.common.tools.parsedatalogtools.impl.J750DatalogParser;
import com.vtest.it.common.tools.reupload.GenerateLog;
import com.vtest.it.upload.properties.Properties;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DataLogUpload {
    public static void main(String[] args) throws IOException, ParseException {
        File[] files = new File(Properties.DATASOURCE).listFiles();
        ConcurrentLinkedQueue<File> queue = new ConcurrentLinkedQueue<File>();
        List<File> errorDataList = new LinkedList<File>();

        FtpUpload upload = new FtpUpload();
        ErrorDataDeal commonErrorDataDeal = new CommonErrorDataDeal();
        CommonFileCompress commonFileCompress = new CommonFileCompress();
        S200QueueNeedUpload s200QueueNeedUpload = new S200QueueNeedUpload();

        s200QueueNeedUpload.getUploadQueue(queue, errorDataList, files);
        commonErrorDataDeal.DataDeal(errorDataList, Properties.ERROR_DATA_PATH);
        System.err.println("init size: " + queue.size());
        ConcurrentLinkedQueue<File> fileNeedUploadQueue = commonFileCompress.compress(queue, Properties.BACKUP_PATH, new J750DatalogParser());
        System.err.println("complete size: " + fileNeedUploadQueue.size());
        DealFileNeedReUpload.getFile(Properties.LOG_PATH, fileNeedUploadQueue);
        GenerateLog.generateLog(Properties.LOG_PATH, upload.upload(fileNeedUploadQueue, "/S200/"));
    }
}
