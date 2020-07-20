package com.vtest.it.common.tools.ErrorDataDeal.impl;

import com.vtest.it.common.tools.ErrorDataDeal.ErrorDataDeal;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author shawn.sun
 * @date 2020-7-17 14:55:29
 */
public class CommonErrorDataDeal implements ErrorDataDeal {
    @Override
    public void DataDeal(List<File> list, String errorDataPath)  {
        for (File file : list) {
            try {
                FileUtils.copyFile(file,new File(errorDataPath+"/"+file.getName()));
                FileUtils.forceDelete(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
