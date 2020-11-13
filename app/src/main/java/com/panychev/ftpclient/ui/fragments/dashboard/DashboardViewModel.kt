package com.panychev.ftpclient.ui.fragments.dashboard

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.panychev.ftpclient.data.FtpRepository
import com.panychev.ftpclient.utils.lazyDeferred

class DashboardViewModel @ViewModelInject constructor(
    private val ftpRepository: FtpRepository
): ViewModel() {
    val fileList by lazyDeferred {
        ftpRepository.getListFiles()
    }
}