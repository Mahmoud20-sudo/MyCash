package com.codeIn.common.util

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

class PermissionChecker {

    fun checkPermissionCamera(activity : FragmentActivity?) : Boolean
    {
        return ( activity?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.CAMERA) } != PackageManager.PERMISSION_GRANTED)
    }

    fun checkPermissionReadStorage(activity : FragmentActivity?) : Boolean
    {
        return ( activity?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.READ_EXTERNAL_STORAGE) } != PackageManager.PERMISSION_GRANTED)
    }

    fun checkPermissionWriteStorage(activity : FragmentActivity?) : Boolean
    {
        return ( activity?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.WRITE_EXTERNAL_STORAGE) } != PackageManager.PERMISSION_GRANTED)
    }

    fun askForPermissionCamera(activity : FragmentActivity?)
    {
        activity?.let {
            ActivityCompat.requestPermissions(
                it, arrayOf(Manifest.permission.CAMERA)
                ,1)
        }
    }

}