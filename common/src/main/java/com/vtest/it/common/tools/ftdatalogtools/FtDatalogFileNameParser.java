package com.vtest.it.common.tools.ftdatalogtools;

import com.vtest.it.common.pojo.FtStdfInformationBean;

import java.io.File;

public interface FtDatalogFileNameParser {
    public FtStdfInformationBean parse(String fileName);
}
