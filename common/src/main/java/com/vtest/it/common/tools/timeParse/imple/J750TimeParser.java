package com.vtest.it.common.tools.timeParse.imple;

import com.vtest.it.common.tools.timeParse.TimeParser;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class J750TimeParser implements TimeParser {

    private SimpleDateFormat format=new SimpleDateFormat("yyyyMMdd_HHmmss");

    public long parse(String time) throws ParseException {
        return format.parse(time).getTime();
    }
}
