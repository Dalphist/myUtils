package com.example.demo.openoffice;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * @author DJF
 * @version 0.1.0
 * @Description
 * @create 2021-05-07 14:53
 * @since 0.1.0
 **/
public class UploadUtils {
    /**
     * 36个小写字母和数字
     */
    public static final char[] N36_CHARS = { '0', '1', '2', '3', '4', '5', '6',
            '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
            'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
            'x', 'y', 'z' };


    public static String generateFilename(String path, String ext) {
        return path + RandomStringUtils.random(8, N36_CHARS) + "." + ext;
    }

}
