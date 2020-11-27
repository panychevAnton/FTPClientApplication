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
import com.google.android.material.snackbar.Snackbar
import com.panychev.ftpclient.R
import com.panychev.ftpclient.utils.Resource.Status.*
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
        filesLiveData.observe(viewLifecycleOwner, Observer{
            when(it.status){
                LOADING -> group_loading.visibility = View.VISIBLE
                ERROR -> {
                    group_loading.visibility = View.GONE
                    recycler_files.visibility = View.GONE
                    txt_error.visibility = View.VISIBLE
                    txt_error.text = it.message
                }
                SUCCESS -> {
                    if (it.data.isNullOrEmpty()) return@Observer
                    txt_error.visibility = View.GONE
                    group_loading.visibility = View.GONE
                    recycler_files.visibility = View.VISIBLE
                    initRecyclerView(it.data.toFileItem())
                }
            }
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