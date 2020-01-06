package com.vtest.it.common.tools.ftdatalogtools.impl;

import com.vtest.it.common.pojo.FtStdfInformationBean;
import com.vtest.it.common.tools.FtDatalogRegexSamples;
import com.vtest.it.common.tools.ftdatalogtools.FtDatalogFileNameParser;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class J750DatalogFileNameParser implements FtDatalogFileNameParser {

    @Override
    public FtStdfInformationBean parse(String fileName) {
        Pattern pattern = Pattern.compile(FtDatalogRegexSamples.J750Regex);
        Matcher matcher = pattern.matcher(fileName);
        if (matcher.find()) {
            FtStdfInformationBean bean = new FtStdfInformationBean();
            String customerCode = matcher.group(1);
            String device = matcher.group(2);
            String lot = matcher.group(3);
            String vLot = matcher.group(4);
            String tester = matcher.group(5);
            String handler = matcher.group(6);
            String loadBoard = matcher.group(7);
            String ftStep = matcher.group(8);
            String rpStep = matcher.group(9);
            String operator = matcher.group(10);
            String testTime = matcher.group(11);
            String suffix = matcher.group(12);

            bean.setCustomerCode(customerCode);
            bean.setDevice(device);
            bean.setLot(lot);
            bean.setVLot(vLot);
            bean.setTester(tester);
            bean.setHandler(handler);
            bean.setFtStep(ftStep);
            bean.setLoadBoard(loadBoard);
            bean.setRpStep(rpStep);
            bean.setOperator(operator);
            bean.setStartTime(testTime);
            bean.setSuffix(suffix);
            return bean;
        }
        return null;
    }
}
