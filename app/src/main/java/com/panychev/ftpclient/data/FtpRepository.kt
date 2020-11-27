package com.panychev.ftpclient.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.panychev.ftpclient.data.ftp.FtpDataSource
import com.panychev.ftpclient.utils.Resource
import com.panychev.ftpclient.utils.Resource.Status.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.commons.net.ftp.FTPFile
import javax.inject.Inject

class FtpRepository @Inject constructor(
        private val dataSource: FtpDataSource
) {
    suspend fun getListFiles(): LiveData<Resource<List<FTPFile>>>{
        return liveData(Dispatchers.IO){
            emit(Resource.loading())
            dataSource.fetchFileList()
            emitSource(dataSource.uploadedFileList)
        }
    }
}