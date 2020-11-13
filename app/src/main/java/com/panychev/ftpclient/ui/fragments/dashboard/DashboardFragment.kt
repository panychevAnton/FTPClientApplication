package com.panychev.ftpclient.ui.fragments.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.panychev.ftpclient.R
import com.panychev.ftpclient.utils.ScopedFragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.apache.commons.net.ftp.FTPFile

@AndroidEntryPoint
class DashboardFragment : ScopedFragment() {

    private val viewModel: DashboardViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        bindUi()
    }

    private fun bindUi() = launch {
        val filesLiveData = viewModel.fileList.await()
        filesLiveData.observe(viewLifecycleOwner, Observer{listFiles ->
            if (listFiles == null) return@Observer
            group_loading.visibility = View.GONE
            initRecyclerView(listFiles.toFileItem())
        })
    }

    private fun List<FTPFile>.toFileItem() =
        map {
            FileInfoItem(it)
        }


    private fun initRecyclerView(items: List<FileInfoItem>) {
        val groupAdapter = GroupAdapter<GroupieViewHolder>().apply {
            addAll(items)
        }
        recycler_files.apply {
            layoutManager = LinearLayoutManager(this@DashboardFragment.context)
            adapter = groupAdapter
        }
    }


}