package com.vtest.it.common.tools.timecheck;

public class TimeCheckImpl implements TimeCheck {
    @Override
    public boolean check(long fileLastModifyTime, int seconds) {
        long currentTime = System.currentTimeMillis();
        long secondsDiff = (currentTime - fileLastModifyTime) / 1000;
        return secondsDiff > seconds;
    }
}
