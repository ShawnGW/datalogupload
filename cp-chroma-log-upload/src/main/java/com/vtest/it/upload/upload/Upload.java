package com.vtest.it.upload.upload;

import com.vtest.it.common.tools.Compress.FileCompress;
import com.vtest.it.common.tools.Compress.impl.GowFileCompressImpl;
import com.vtest.it.common.tools.FileUpload.QueueNeedUpload;
import com.vtest.it.common.tools.FileUpload.ft.impl.GowLogQueueNeedUploadImpl;
import com.vtest.it.common.tools.datalogupload.FtpUpload;
import com.vtest.it.common.tools.dealreuploadfile.DealFileNeedReUpload;
import com.vtest.it.common.tools.reupload.GenerateLog;
import com.vtest.it.upload.properties.Properties;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author shawn.sun
 * @date 2020/04/29  10:18
 */
public class Upload {
    public static void main(String[] args) throws ParseException, IOException {
        File[] files = new File(Properties.SOURCE_PATH).listFiles();
        ConcurrentLinkedQueue<File> queueNew = new ConcurrentLinkedQueue<File>();
        List<File> errorDataList = new LinkedList<File>();

        QueueNeedUpload queueNeedUpload = new GowLogQueueNeedUploadImpl();
        FileCompress ftFileCompress = new GowFileCompressImpl();
        FtpUpload upload = new FtpUpload();
        queueNeedUpload.getUploadQueue(queueNew, errorDataList, files);
        ConcurrentLinkedQueue<File> fileNeedUploadQueue = ftFileCompress.compress(queueNew, Properties.BACKUP_PATH, null);
        DealFileNeedReUpload.getFile(Properties.LOG_PATH, fileNeedUploadQueue);
        GenerateLog.generateLog(Properties.LOG_PATH, upload.upload(fileNeedUploadQueue, "/GOW_Log/"));
    }
}
