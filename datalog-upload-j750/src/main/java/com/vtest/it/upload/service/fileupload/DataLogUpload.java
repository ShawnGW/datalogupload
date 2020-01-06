package com.vtest.it.upload.service.fileupload;

import com.vtest.it.common.tools.Compress.impl.CommonFileCompress;
import com.vtest.it.common.tools.ErrorDataDeal.ErrorDataDeal;
import com.vtest.it.common.tools.ErrorDataDeal.impl.CommonErrorDataDeal;
import com.vtest.it.common.tools.FileUpload.cp.impl.J750QueueNeedUpload;
import com.vtest.it.common.tools.datalogupload.FtpUpload;
import com.vtest.it.common.tools.dealreuploadfile.DealFileNeedReUpload;
import com.vtest.it.common.tools.parsedatalogtools.impl.J750DatalogParser;
import com.vtest.it.common.tools.reupload.GenerateLog;
import com.vtest.it.upload.commonproperties.Properties;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DataLogUpload {
    public static void main(String[] args) throws IOException, ParseException {
        if (!new File(Properties.dataSource).exists()){
            return;
        }
        File[] files = new File(Properties.dataSource).listFiles();
        if (files.length==0){
            return;
        }
        ConcurrentLinkedQueue<File> queue = new ConcurrentLinkedQueue<File>();
        List<File> errorDataList = new LinkedList<File>();

        FtpUpload upload = new FtpUpload();
        J750QueueNeedUpload j750QueueNeedUpload = new J750QueueNeedUpload();
        ErrorDataDeal commonErrorDataDeal = new CommonErrorDataDeal();
        CommonFileCompress commonFileCompress = new CommonFileCompress();

        j750QueueNeedUpload.getUploadQueue(queue, errorDataList, files);
        commonErrorDataDeal.DataDeal(errorDataList, Properties.ERRORDATAPATH);
        ConcurrentLinkedQueue<File> fileNeedUploadQueue = commonFileCompress.compress(queue, Properties.backupPath,new J750DatalogParser());
        DealFileNeedReUpload.getFile(Properties.logPath, fileNeedUploadQueue);
        GenerateLog.generateLog(Properties.logPath, upload.upload(fileNeedUploadQueue, "/J750/"));
    }
}
