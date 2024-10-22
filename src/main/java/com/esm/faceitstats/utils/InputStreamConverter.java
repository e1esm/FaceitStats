package com.esm.faceitstats.utils;

import java.io.IOException;
import java.io.InputStream;

public class InputStreamConverter {
    public static String convertStreamToString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();

        for(int ch; (ch = is.read()) != -1;){
            sb.append((char)ch);
        }

        return sb.toString();
    }
}
