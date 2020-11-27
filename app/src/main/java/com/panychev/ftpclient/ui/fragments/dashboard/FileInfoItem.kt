package com.panychev.ftpclient.ui.fragments.dashboard

import com.panychev.ftpclient.R
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_file_info.*
import org.apache.commons.net.ftp.FTPFile

class FileInfoItem(
        private val file: FTPFile
): Item() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.apply {
            txt_file_name.text = file.name
        }
    }

    override fun getLayout() = R.layout.item_file_info
}