package com.vtest.it.common.tools.Compress;

import com.vtest.it.common.tools.parsedatalogtools.DatalogFileNameParser;

import java.io.File;
import java.util.concurrent.ConcurrentLinkedQueue;

public interface FileCompress {
    public abstract ConcurrentLinkedQueue<File> compress(ConcurrentLinkedQueue<File> queue, String backupPath, DatalogFileNameParser parser);
}
