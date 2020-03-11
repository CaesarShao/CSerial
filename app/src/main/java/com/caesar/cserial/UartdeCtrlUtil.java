package com.caesar.cserial;

import android.util.Log;

import androidx.annotation.IntDef;

import java.io.File;
import java.io.FileWriter;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/*
 * s5pro pt2257芯片
 * */
public class UartdeCtrlUtil {

    private static final String VOLUME_CTRL_PATH = "/sys/class/pt2257/uartde";
    private static final String TAG = "UartdeCtrlUtil";

    public static final int SOURCE_RECEIVE= 0;
    public static final int SOURCE_SEND = 1;

    @IntDef({SOURCE_RECEIVE, SOURCE_SEND})
    @Retention(RetentionPolicy.SOURCE)
    public @interface UartdeModel {
    }


    public static void switchUartde(@UartdeModel int source) {
        FileWriter fileWriter = null;
        File file = new File(VOLUME_CTRL_PATH);
        if (!file.exists()) {
            Log.e(TAG, "uartde_CTRL_PATH is not exists!");
            return;
        }
        try {
            fileWriter = new FileWriter(file);
            fileWriter.write(source + "");
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
