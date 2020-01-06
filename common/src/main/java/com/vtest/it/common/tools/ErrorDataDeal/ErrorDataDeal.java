package com.vtest.it.common.tools.ErrorDataDeal;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface ErrorDataDeal {
    public void DataDeal(List<File> list,String errorDataPath) throws IOException;
}
