package com.vtest.it.common.tools.Compress;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author shawn.sun
 * @date 2020/07/17  16:03
 */
public interface FileCompressWithInformation {
    ConcurrentLinkedQueue<File> compress(ConcurrentLinkedQueue<File> queue, String backupPath, Map<File, Object> map);
}
