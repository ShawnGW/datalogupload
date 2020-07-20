package com.vtest.it.upload.upload;

import com.vtest.it.common.tools.Compress.FileCompressWithInformation;
import com.vtest.it.common.tools.Compress.impl.SlaFileCompress;
import com.vtest.it.common.tools.ErrorDataDeal.ErrorDataDeal;
import com.vtest.it.common.tools.ErrorDataDeal.impl.CommonErrorDataDeal;
import com.vtest.it.common.tools.FileUpload.QueueNeedUploadWithInfor;
import com.vtest.it.common.tools.FileUpload.ft.impl.SlaDatalogQueueNeddUploadImpl;
import com.vtest.it.common.tools.datalogupload.FtFtpUpload;
import com.vtest.it.common.tools.dealreuploadfile.DealFileNeedReUpload;
import com.vtest.it.common.tools.reupload.GenerateLog;
import com.vtest.it.upload.properties.Properties;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author shawn.sun
 * @date 2020-7-17 14:52:39
 */
public class DatalogUpload {
    public static void main(String[] args) throws ParseException, IOException {
        File[] files = new File(Properties.DATASOURCE).listFiles();
        ConcurrentLinkedQueue<File> queueNew = new ConcurrentLinkedQueue();
        List<File> errorDataList = new LinkedList<File>();

        QueueNeedUploadWithInfor queueNeedUpload = new SlaDatalogQueueNeddUploadImpl();
        FileCompressWithInformation ftFileCompress = new SlaFileCompress();
        ErrorDataDeal commonErrorDataDeal = new CommonErrorDataDeal();
        FtFtpUpload upload = new FtFtpUpload();
        Map<File, Object> fileInformationMap = queueNeedUpload.getUploadQueue(queueNew, errorDataList, files);

        commonErrorDataDeal.DataDeal(errorDataList, Properties.ERROR_DATA_PATH);
        ConcurrentLinkedQueue<File> fileNeedUploadQueue = ftFileCompress.compress(queueNew, Properties.BACKUP_PATH, fileInformationMap);
        DealFileNeedReUpload.getFile(Properties.LOG_PATH, fileNeedUploadQueue);
        GenerateLog.generateLog(Properties.LOG_PATH, upload.upload(fileNeedUploadQueue, "/SLA/"));
    }
}
