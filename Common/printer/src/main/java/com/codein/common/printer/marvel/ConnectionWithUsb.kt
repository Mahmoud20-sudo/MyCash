package com.codein.common.printer.marvel

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.view.View
import com.codein.common.printer.R

class ConnectionWithUsb {
    private var permissionUtils: PermissionUtils? = null

    private fun initPermission(context : Context) {
        permissionUtils?.requestPermissions(context.getString(R.string.permission),
            object : PermissionUtils.PermissionListener{
                override fun doAfterGrand(vararg permission: String?) {}
                override fun doAfterDenied(vararg permission: String?) {
                    for (p in permission) {
                        when (p) {
                            Manifest.permission.READ_EXTERNAL_STORAGE -> Utils.shortToast(
                                context,
                                context.getString(R.string.no_read)
                            )

                            Manifest.permission.ACCESS_FINE_LOCATION -> Utils.shortToast(
                                context,
                                context.getString(R.string.no_permission)
                            )
                        }
                    }
                }
            }, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    fun print(context: Context , view : View){
        initPermission(context)
        val device = UsbDevices(context , view)
        device.getUsbDeviceList(view)
    }
}