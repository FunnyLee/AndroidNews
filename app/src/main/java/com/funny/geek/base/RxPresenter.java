package com.funny.geek.base;

import com.funny.geek.model.net.DataHelper;
import com.trello.rxlifecycle2.LifecycleProvider;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Author: Funny
 * Time: 2018/10/19
 * Description: This is RxPresenter
 */
public class RxPresenter<V extends IBaseView> implements IBasePresenter<V> {

    protected V mView;

    protected DataHelper mDataHelper;

    protected LifecycleProvider mLifecycleProvider;

    @Inject
    public RxPresenter(DataHelper dataHelper, LifecycleProvider lifecycleProvider) {
        mDataHelper = dataHelper;
        mLifecycleProvider = lifecycleProvider;
    }

    /**
     * 处理Rxjava线程切换
     * 使用RxLifeCycle对Rxjava生命周期进行管理
     *
     * @param observable
     * @param <T>
     * @return
     */
    public <T> Observable<T> add(Observable<T> observable) {
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(mLifecycleProvider.bindToLifecycle());
    }

    @Override
    public void attachView(V view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }
}
