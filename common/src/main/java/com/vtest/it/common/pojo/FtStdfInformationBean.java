package com.vtest.it.common.pojo;

import lombok.Data;

@Data
public class FtStdfInformationBean {
    private String customerCode;
    private String device;
    private String lot;
    private String vLot;
    private String tester;
    private String handler;
    private String loadBoard;
    private String ftStep;
    private String rpStep;
    private String operator;
    private String startTime;
    private String suffix;
}
