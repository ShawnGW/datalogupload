package com.vtest.it.common.tools.timeParse;

import java.text.ParseException;

public interface TimeParser {
    public abstract long parse(String time) throws ParseException;
}
