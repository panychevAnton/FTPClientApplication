package com.panychev.ftpclient.data

import androidx.lifecycle.LiveData
import com.panychev.ftpclient.data.ftp.FtpDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.commons.net.ftp.FTPFile
import javax.inject.Inject

class FtpRepository @Inject constructor(
        private val dataSource: FtpDataSource
) {
    suspend fun getListFiles(): LiveData<List<FTPFile>>{
        return withContext(Dispatchers.IO){
            dataSource.fetchFileList()
            return@withContext dataSource.uploadedFileList
        }
    }
}