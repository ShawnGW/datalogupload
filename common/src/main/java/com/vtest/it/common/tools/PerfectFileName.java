package com.vtest.it.common.tools;

public class PerfectFileName {
    public static String remove(String fileName){
        return fileName.contains("__")?remove(fileName.replaceAll("__","_")):fileName;
    }
    public static String removeBrackets(String fileName) {
        return fileName.replaceAll("\\(", "").replaceAll("\\)", "");
    }
}
