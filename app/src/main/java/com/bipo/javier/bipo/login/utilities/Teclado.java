package com.bipo.javier.bipo.login.utilities;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Javier on 15/04/2017.
 */

public class Teclado {

    public static void ocultarTeclado(Activity activity) {
        InputMethodManager inputMetMan = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View focus = activity.getCurrentFocus();
        if (focus != null) {
            inputMetMan.hideSoftInputFromWindow(focus.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
