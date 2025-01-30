package com.codeIn.myCash.ui.authentication.sign_up.payment_method;

import android.content.Context;
import android.webkit.JavascriptInterface;

public class WebInterface {
    private Context context ;
    FawryPaymentView viewInterface ;
    public WebInterface(Context context , FawryPaymentView fawryPaymentView){
        this.context =context ;
        this.viewInterface = fawryPaymentView ;
    }

    @JavascriptInterface
    public void finished() {
        viewInterface.requestCompleted();
    }
}