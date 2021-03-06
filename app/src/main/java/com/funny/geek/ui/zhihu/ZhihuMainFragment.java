package com.funny.geek.ui.zhihu;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.funny.geek.R;
import com.funny.geek.base.BaseFragment;
import com.funny.geek.ui.MainActivity;
import com.funny.geek.ui.adpter.FragmentAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Author: Funny
 * Time: 2018/10/16
 * Description: This is ZhihuMainFragment
 */
public class ZhihuMainFragment extends BaseFragment {

    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    private List<String> titleList = new ArrayList<>();
    private List<Fragment> fragmentList = new ArrayList<>();

    public static ZhihuMainFragment newInstance() {
        Bundle args = new Bundle();
        ZhihuMainFragment fragment = new ZhihuMainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mian_zhihu;
    }

    @Override
    protected void initView(View view) {
        MainActivity activity = (MainActivity) getActivity();
        activity.setStatusBarColor(R.drawable.green_gradient_color_shape);

        titleList.add(getString(R.string.daily_paper));
        titleList.add(getString(R.string.subject));
        titleList.add(getString(R.string.special));
        titleList.add(getString(R.string.hot));
        fragmentList.add(DailyFragment.newInstance());
        fragmentList.add(SubjectFragment.newInstance());
        fragmentList.add(SpecialFragment.newInstance());
        fragmentList.add(HotFragment.newInstance());

        //设置getChildFragmentManager()，在DailyFragment中getParentFragment()方法才能生效（否则获取为Null）
        FragmentAdapter adapter = new FragmentAdapter(getChildFragmentManager(), fragmentList, titleList);
        mViewPager.setAdapter(adapter);
//        mViewPager.setOffscreenPageLimit(4);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    public void setTabLayoutColor(int drawable) {
        mTabLayout.setBackgroundResource(drawable);
    }
}
