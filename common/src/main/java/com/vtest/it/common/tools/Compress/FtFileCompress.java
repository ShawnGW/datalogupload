package com.vtest.it.common.tools.Compress;

import com.vtest.it.common.tools.ftdatalogtools.FtDatalogFileNameParser;

import java.io.File;
import java.util.concurrent.ConcurrentLinkedQueue;

public interface FtFileCompress {
    ConcurrentLinkedQueue<File> compress(ConcurrentLinkedQueue<File> queue, String backupPath, FtDatalogFileNameParser parser);
}
