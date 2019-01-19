package com.funny.geek.ui.zhihu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.funny.geek.R;
import com.funny.geek.base.RootFragment;
import com.funny.geek.contract.zhihu.DailyContract;
import com.funny.geek.model.bean.DailyBean;
import com.funny.geek.model.net.ImageHelper;
import com.funny.geek.presenter.zhihu.DailyPresenter;
import com.funny.geek.ui.adpter.DailyAdapter;
import com.funny.geek.util.TimeUtils;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;

/**
 * Author: Funny
 * Time: 2018/10/17
 * Description: This is DailyFragment
 */
public class DailyFragment extends RootFragment<DailyPresenter> implements DailyContract.View {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.fab_btn)
    FloatingActionButton mFabBtn;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mRefreshLayout;

    //当前日期
    private String mCurrentDate;

    private List<DailyBean.StoriesBean> mDatas = new ArrayList<>();
    private DailyAdapter mAdapter;
    private Banner mBanner;
    private TextView mTvData;

    public static DailyFragment newInstance() {
        Bundle args = new Bundle();
        DailyFragment fragment = new DailyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_daily;
    }

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected void initView(View view) {
        mAdapter = new DailyAdapter(mContext, R.layout.item_daily_content_view, mDatas);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        //头部
        View headerView = LayoutInflater.from(mContext).inflate(R.layout.layout_daily_header, null);
        mAdapter.addHeaderView(headerView);
        mBanner = headerView.findViewById(R.id.banner);
        mTvData = headerView.findViewById(R.id.tv_date);
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    protected void initData() {
        mPresenter.doLoadData();
    }

    @Override
    protected void initEvent() {
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.doRefresh(mCurrentDate);
                Toast.makeText(mContext, "" + mCurrentDate, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("CheckResult")
    @Override
    public void onShowContentView(DailyBean dailyListBean) {
        if (mRefreshLayout.isRefreshing()) {
            mRefreshLayout.setRefreshing(false);
        }
        //请求成功后，设置当前日期
        mCurrentDate = TimeUtils.getCurrentDate();
        mTvData.setText(dailyListBean.date);
        //设置Banner
        List<DailyBean.TopStoriesBean> top_stories = dailyListBean.top_stories;
        if (top_stories != null && top_stories.size() != 0) {
            List<String> imageUrls = new ArrayList<>();
            List<String> bannerTitles = new ArrayList<>();
            Observable.fromIterable(top_stories)
                    .subscribe(topStoriesBean -> {
                        imageUrls.add(topStoriesBean.image);
                        bannerTitles.add(topStoriesBean.title);
                    });

            mBanner.setImageLoader(new ImageLoader() {
                @Override
                public void displayImage(Context context, Object path, ImageView imageView) {
                    ImageHelper.loadImage(context, (String) path, imageView);
                }
            });
            mBanner.setImages(imageUrls);
            mBanner.setBannerTitles(bannerTitles);
            mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
            mBanner.isAutoPlay(true);
            mBanner.start();
        } else {
            mBanner.setVisibility(View.GONE);
        }
        mDatas.clear();
        mDatas.addAll(dailyListBean.stories);
        mAdapter.notifyDataSetChanged();
    }

}
