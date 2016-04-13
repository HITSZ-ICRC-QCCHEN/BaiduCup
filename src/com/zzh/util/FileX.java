package com.zzh.util;

import java.io.*;

/**
 * Created by Jeff on 2016/4/13.
 */
public class FileX {

    public static String getText(File file) {
        String text = "";
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String s = null;
            while((s = bufferedReader.readLine()) != null) {
                text += s;
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return text;
    }
}
