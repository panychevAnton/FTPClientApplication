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
    companion object {
        operator fun invoke(): FTPClient{
            val ftpClient = FTPClient()
            val user = BuildConfig.FTP_LOGIN
            val password = BuildConfig.FTP_PASSWORD
            val server = BuildConfig.FTP_ADDRESS
            val port = BuildConfig.FTP_PORT
            return ftpClient.apply {
                try {
                    connect(server, port)
                    if (!login(user, password)) {
                        logout()
                        Log.e("FTPLogin", "Login error")
                    }
                    if (!FTPReply.isPositiveCompletion(reply)) {
                        disconnect()
                        Log.e("FTPConnect", "Connection error")
                    }
                    enterLocalPassiveMode()
                    setFileType(FTP.BINARY_FILE_TYPE)
                }catch (e: SocketException){
                    Log.e("Socket", "Socket error", e)
                }
            }
        }
    }
}