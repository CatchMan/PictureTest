package com.example.picturetest.ui.mainList

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.domain.model.Picture
import com.example.picturetest.R
import com.example.picturetest.ui.base.BaseActivity
import com.example.picturetest.ui.mainList.adapters.PaginationListener
import com.example.picturetest.ui.mainList.adapters.PaginationListener.Companion.PAGE_START
import com.example.picturetest.ui.mainList.adapters.PictureAdapter
import com.example.picturetest.ui.pictureDetails.DetailActivity
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : BaseActivity(), MainContract.View, View.OnClickListener,
    SwipeRefreshLayout.OnRefreshListener{


    @Inject
    lateinit var presenter: MainContract.Presenter<MainContract.View>
    private var currentPage = PAGE_START
    private var isLoadingNow = false
    var lastFirstVisiblePosition = 0

    private var adapter = PictureAdapter(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        activityComponent.inject(this)
        presenter.bindView(this)
        initUI()
    }

    override fun onPause() {
        super.onPause()
        lastFirstVisiblePosition =
            (recyclerView.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()

    }

    override fun onResume() {
        super.onResume()
        (recyclerView.layoutManager as LinearLayoutManager).scrollToPosition(lastFirstVisiblePosition)
    }

    override fun onClick(view: View) {
        DetailActivity.start(this, adapter.getList()[recyclerView.getChildAdapterPosition(view)])
    }

    override fun getPicturesSuccess(pictureInfo: List<Picture>) {
        adapter.updateData(pictureInfo)
        swipeRefresh.isRefreshing = false
    }

    override fun getPicturesFail() {
        toast(getString(R.string.cant_show_pictures))
    }

    override fun onRefresh() {
        currentPage = PAGE_START
        presenter.loadPictures(currentPage)
    }

    private fun initUI() {
        swipeRefresh.setOnRefreshListener(this)
        presenter.loadPictures(currentPage)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        recyclerView.addOnScrollListener(object : PaginationListener(layoutManager) {

            override val isLoading: Boolean
                get() = isLoadingNow//To change initializer of created properties use File | Settings | File Templates.

            override fun loadMoreItems() {
                isLoadingNow = true
                currentPage++
                presenter.loadPictures(currentPage)
            }

        })
    }
}
