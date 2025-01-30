package com.codein.common.printer.marvel;


import android.content.Context;
import android.graphics.Bitmap;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import com.codein.common.printer.PrintUtils;
import com.codein.common.printer.R;
import com.gprinter.bean.PrinterDevices;
import com.gprinter.command.EscCommand;
import com.gprinter.utils.CallbackListener;
import com.gprinter.utils.Command;
import com.gprinter.utils.ConnMethod;
import com.gprinter.utils.LogUtils;

public class UsbDevices implements CallbackListener {
    UsbManager manager;
    Printer printer=null;
    Context context ;

    public UsbDevices(Context context , View view){
        this.context = context ;

    }
    public void  getUsbDeviceList(View view) {
        manager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        // Get the list of attached devices
        HashMap<String, UsbDevice> devices = manager.getDeviceList();
        Iterator<UsbDevice> deviceIterator = devices.values().iterator();
        int count = devices.size();
        if (count > 0) {
            while (deviceIterator.hasNext()) {
                UsbDevice device = deviceIterator.next();
                if (checkUsbDevicePidVid(device)) {
                    PrinterDevices usb= new PrinterDevices.Build()
                            .setContext(context)
                            .setConnMethod(ConnMethod.USB)
                            .setUsbDevice(device)
                            .setCommand(Command.ESC)
                            .setCallbackListener(this)
                            .build();
                    Printer.connect(usb);
                    print(view);
                }
            }
        } else {
            String noDevices = context.getResources().getText( R.string.none_usb_device).toString();
            Toast.makeText(context , noDevices , Toast.LENGTH_LONG).show();
        }
    }

    boolean checkUsbDevicePidVid(UsbDevice dev) {
        int pid = dev.getProductId();
        int vid = dev.getVendorId();
        return ((vid == 34918 && pid == 256) || (vid == 1137 && pid == 85)
                || (vid == 6790 && pid == 30084)
                || (vid == 26728 && pid == 256) || (vid == 26728 && pid == 512)
                || (vid == 26728 && pid == 256) || (vid == 26728 && pid == 768)
                || (vid == 26728 && pid == 1024) || (vid == 26728 && pid == 1280)
                || (vid == 26728 && pid == 1536));
    }

    @Override
    public void onConnecting() {

    }

    @Override
    public void onCheckCommand() {//查询打印机指令
    }

    @Override
    public void onSuccess(PrinterDevices printerDevices) {
        Toast.makeText(context,context.getString(R.string.conn_success),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onReceive(byte[] bytes) {

    }

    @Override
    public void onFailure() {//连接失败
        Toast.makeText(context,context.getString(R.string.conn_fail),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDisconnect() {
        Toast.makeText(context,context.getString(R.string.disconnect),Toast.LENGTH_SHORT).show();
    }

    Handler handler =new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 0x00:
                    String tip=(String)msg.obj;
                    Toast.makeText(context,tip,Toast.LENGTH_SHORT).show();
                    break;
                case 0x01:
                    int status=msg.arg1;
                    if (status==-1){//获取状态失败
                        return;
                    }else if (status==0){//状态正常
//                        Toast.makeText(context,getString(R.string.status_normal),Toast.LENGTH_SHORT).show();
                        return;
                    }else if (status==-2){//状态缺纸
//                        Toast.makeText(context,getString(R.string.status_out_of_paper),Toast.LENGTH_SHORT).show();
                        return;
                    }else if (status==-3){//状态开盖
//                        Toast.makeText(context,getString(R.string.status_open),Toast.LENGTH_SHORT).show();
                        return;
                    }else if (status==-4){
//                        Toast.makeText(context,getString(R.string.status_overheated),Toast.LENGTH_SHORT).show();
                        return;
                    }
                    break;
                case 0x02://关闭连接
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (printer.getPortManager()!=null){
                                printer.close();
                            }
                        }
                    }).start();
                    break;
                case 0x03:
                    break;
                case 0x08:
                    break;
            }
        }
    };

    public void print(View view) {
        ThreadPoolManager.getInstance().addTask(new Runnable() {
            @Override
            public void run() {
                try {
                    if (printer.getPortManager()==null){
                        return;
                    }
                    Command command = Printer.getPortManager().getCommand();
                    int status = printer.getPrinterState(command);
                    if (status != 0) {//打印机处于不正常状态,则不发送打印任务
                        Message msg = new Message();
                        msg.what = 0x01;
                        msg.arg1 = status;
                        handler.sendMessage(msg);
                        return;
                    }
                    EscCommand esc = new EscCommand();
                    esc.addInitializePrinter();
                    Bitmap bitmap = PrintUtils.Companion.getBitmapFromView(view);
                    esc.drawImage(bitmap ,  view.getWidth());
                    esc.addPrintAndLineFeed();
                    esc.addPrintAndLineFeed();
                    esc.addCutPaper();
                    boolean result=  printer.getPortManager().writeDataImmediately(esc.getCommand());
                } catch (IOException e) {

                }catch (Exception e){

                }
            }
        });
    }
}
