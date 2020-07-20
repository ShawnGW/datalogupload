package com.vtest.it.common.tools.FileUpload;

import java.io.File;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author shawn.sun
 * @date 2020-7-17 15:39:46
 */
public interface QueueNeedUploadWithInfor {
    public Map<File, Object> getUploadQueue(ConcurrentLinkedQueue<File> queue, List<File> errorDataList, File[] files) throws ParseException;
}
