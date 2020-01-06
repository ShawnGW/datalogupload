package com.vtest.it.common.tools.FileUpload;

import java.io.File;
import java.text.ParseException;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public interface QueueNeedUpload {
    public void getUploadQueue(ConcurrentLinkedQueue<File> queue, List<File> errorDataList, File[] files) throws ParseException;
}
