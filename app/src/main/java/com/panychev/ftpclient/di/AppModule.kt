package com.panychev.ftpclient.di

import com.panychev.ftpclient.data.FtpRepository
import com.panychev.ftpclient.data.ftp.FtpConnection
import com.panychev.ftpclient.data.ftp.FtpDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import org.apache.commons.net.ftp.FTPClient
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object AppModule {
    @Provides
    @Singleton
    fun provideFtpConnection(): FtpConnection = FtpConnection()

    @Provides
    @Singleton
    fun provideFtpDataSource(connect: FtpConnection): FtpDataSource = FtpDataSource(connect)

    @Provides
    @Singleton
    fun provideRepository(dataSource: FtpDataSource) = FtpRepository(dataSource)
}