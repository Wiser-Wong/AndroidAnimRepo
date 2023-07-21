package com.example.secondfloor

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class MianVm(application: Application) : AndroidViewModel(application) {
    var data = MutableLiveData<Float?>()
}