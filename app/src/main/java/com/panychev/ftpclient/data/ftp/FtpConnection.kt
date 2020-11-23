package com.panychev.ftpclient.data.ftp

import android.util.Log
import com.panychev.ftpclient.BuildConfig
import kotlinx.coroutines.*
import org.apache.commons.net.ftp.FTP
import org.apache.commons.net.ftp.FTPClient
import org.apache.commons.net.ftp.FTPFile
import org.apache.commons.net.ftp.FTPReply
import java.net.SocketException

class FtpConnection{
    private val user = BuildConfig.FTP_LOGIN
    private val password = BuildConfig.FTP_PASSWORD
    private val server = BuildConfig.FTP_ADDRESS
    private val port: Int = BuildConfig.FTP_PORT
    val ftpClient by lazy{ FTPClient().apply {
            try {
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
            } catch (e: SocketException) {
                Log.e("Socket", "Socket error", e)
            }
        }
    }
    suspend fun getFileListAsync(client: FTPClient) = coroutineScope{
        return@coroutineScope async {
            client.listFiles().toList()
        }
    }
}