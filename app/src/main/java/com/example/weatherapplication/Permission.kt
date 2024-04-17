package com.example.weatherapplication

import android.content.Context
import androidx.compose.runtime.collection.mutableVectorOf
import androidx.core.os.persistableBundleOf
import androidx.lifecycle.ViewModel

class Permission: ViewModel() {
    var permissionQueue = mutableVectorOf<String>()
    fun dialogBox(){
        var queueSize = permissionQueue.size
        permissionQueue.removeAt(queueSize - 1)
    }
    fun isPermissionGranted(
        isGranted: Boolean,
        permission: String
    ){
        if(!isGranted){
            permissionQueue.add(permission)
        }
        else{

        }
    }

}