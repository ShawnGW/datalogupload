package com.vtest.it.common.tools.timecheck;

public interface TimeCheck {
    public boolean check(long fileLastModifyTime, int seconds);
}
