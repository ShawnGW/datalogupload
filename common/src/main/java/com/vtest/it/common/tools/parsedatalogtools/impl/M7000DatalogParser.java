package com.vtest.it.common.tools.parsedatalogtools.impl;


import com.vtest.it.common.pojo.TesterDatalogInformationBean;
import com.vtest.it.common.tools.DatalogRegexSamples;
import com.vtest.it.common.tools.parsedatalogtools.DatalogFileNameParser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class M7000DatalogParser implements DatalogFileNameParser {
    private Pattern pattern = Pattern.compile(DatalogRegexSamples.M7000REGEX);
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    private SimpleDateFormat dateFormatAfterModify = new SimpleDateFormat("yyyyMMdd_HHmmss");

    public TesterDatalogInformationBean getFileInformation(String fileName) throws ParseException {
        Matcher matcher = pattern.matcher(fileName);
        if (matcher.find()) {
            TesterDatalogInformationBean bean = new TesterDatalogInformationBean();
            String customerCode = matcher.group(1);
            String device = matcher.group(2);
            String lot = matcher.group(3);
            String waferId = matcher.group(4);
            String tester = matcher.group(5);
            String prober = matcher.group(6);
            String proberCard = matcher.group(7);
            String cpProcess = matcher.group(8);
            String reTestTime = matcher.group(9);
            String operator = matcher.group(10);
            String testTime = matcher.group(11);
            String suffix = matcher.group(15);

            testTime = dateFormatAfterModify.format(dateFormat.parse(testTime).getTime());

            bean.setCustomCode(customerCode);
            bean.setDevice(device);
            bean.setLot(lot);
            bean.setWaferId(waferId);
            bean.setTester(tester);
            bean.setProber(prober);
            bean.setProberCard(proberCard);
            bean.setCpStep(cpProcess);
            bean.setTestStep(reTestTime);
            bean.setOperator(operator);
            bean.setTestTime(testTime);
            bean.setSuffix(suffix);
            return bean;
        }
        return null;
    }
}
