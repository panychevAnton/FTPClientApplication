package com.panychev.ftpclient.data.ftp

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import com.panychev.ftpclient.BuildConfig
import com.panychev.ftpclient.utils.NoConnectivityException
import kotlinx.coroutines.*
import org.apache.commons.net.ftp.FTP
import org.apache.commons.net.ftp.FTPClient
import org.apache.commons.net.ftp.FTPReply
import javax.inject.Inject

class FtpConnection @Inject constructor(
        private val context: Context
){
    private val user = BuildConfig.FTP_LOGIN
    private val password = BuildConfig.FTP_PASSWORD
    private val server = BuildConfig.FTP_ADDRESS
    private val port: Int = BuildConfig.FTP_PORT
    val ftpClient by lazy{
        if (!isOnline()) throw NoConnectivityException("Нет подключения к интернету")
        FTPClient().apply {
            connect(server, port)
            if (!login(user, password)) {
                logout()
                Log.e("FTPLogin", "Login error")
            }
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                disconnect()
                Log.e("FTPConnect", "Connection error")
            }
            enterLocalPassiveMode()
            setFileType(FTP.BINARY_FILE_TYPE)
        }
    }
    suspend fun getFileListAsync(client: FTPClient) = coroutineScope{
        return@coroutineScope async {
            client.listFiles().toList()
        }
    }
    private fun isOnline(): Boolean{
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE)
            as ConnectivityManager
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            val networkInfo = connectivityManager.activeNetworkInfo
            networkInfo != null && networkInfo.isConnected
        }else {
            val network = connectivityManager.activeNetwork
            connectivityManager.getNetworkCapabilities(network)?.let {
                network != null &&
                        (it.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        it.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
            } == true
        }
    }
}