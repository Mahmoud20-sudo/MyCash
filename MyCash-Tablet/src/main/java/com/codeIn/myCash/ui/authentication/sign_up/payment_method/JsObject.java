package com.codeIn.myCash.ui.authentication.sign_up.payment_method;

import android.util.Log;
import android.webkit.JavascriptInterface;

class JsObject {
    @JavascriptInterface
    public boolean postMessage(String json, String transferList) {
        Log.d("DKKDKD" , "HEHEHEHEHEH");
        return false; // here we return true if we handled the post.
    }
}

