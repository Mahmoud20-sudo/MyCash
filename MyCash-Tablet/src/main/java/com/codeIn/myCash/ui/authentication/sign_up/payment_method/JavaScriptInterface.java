package com.codeIn.myCash.ui.authentication.sign_up.payment_method;

import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

public class JavaScriptInterface {
    Context context;

    JavaScriptInterface(Context context) {
        this.context = context;
    }

    @SuppressWarnings("unused")
    @JavascriptInterface
    public void sendJsMsgToWebView(String webMessage) {
        Toast.makeText(context, "Get from javascript web" + webMessage, Toast.LENGTH_SHORT).show();
        Log.d("dkkdk" , webMessage);
    }

    @SuppressWarnings("unused")
    @JavascriptInterface
    public String getMsgFromJava() {
        return "Return from native java";
    }

}