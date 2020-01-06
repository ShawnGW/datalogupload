package com.vtest.it.common.tools.parsedatalogtools;


import com.vtest.it.common.pojo.TesterDatalogInformationBean;

import java.text.ParseException;

public interface DatalogFileNameParser {
    public abstract TesterDatalogInformationBean getFileInformation(String fileName) throws ParseException;
}
