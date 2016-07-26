package com.dayton.drone.utils;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/6/17.
 */
public class CheckEmailFormat {
    private static InputMethodManager imm;


    /**
     *
     * @param email
     * @return true匹配
     *          false未匹配
     */
    public static boolean checkEmail(String email) {
        String strPattern = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    public static void openInputMethod(Context context) {

        imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

    }

    public static void closeInputMethod(EditText editText){
        if(imm != null){
            imm.hideSoftInputFromWindow(editText.getWindowToken(),0);
        }
    }

    public static int dip2px(float dpValue, Context context) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
