package com.panychev.ftpclient.data.ftp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.panychev.ftpclient.utils.Resource
import kotlinx.coroutines.*
import org.apache.commons.net.ftp.FTPClient
import org.apache.commons.net.ftp.FTPFile
import java.io.IOException
import java.net.SocketException
import javax.inject.Inject


class FtpDataSource @Inject constructor(
        private val ftpConnection: FtpConnection
) {
    private val _uploadedFileList = MutableLiveData<Resource<List<FTPFile>>>()
    val uploadedFileList: LiveData<Resource<List<FTPFile>>>
        get() = _uploadedFileList

    suspend fun fetchFileList(){
        try {
            val fetchedFileList = with(ftpConnection) {
                getFileListAsync(ftpClient)
                        .await()
            }
            _uploadedFileList.postValue(Resource.success(fetchedFileList))
        }catch (e: IOException){
            Log.e("FileUpload", "Cannot upload files", e)
            _uploadedFileList.postValue(Resource.error(e.message ?: e.toString()))
        }
    }
}