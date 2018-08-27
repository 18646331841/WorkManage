package com.barisetech.www.workmanage.base;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by LJH on 2018/8/27.
 */
public class BaseViewModel extends AndroidViewModel {

    private CompositeDisposable mDisposable = new CompositeDisposable();

    public BaseViewModel(@NonNull Application application) {
        super(application);
    }

    protected void addDisposable(Disposable disposable) {
        mDisposable.add(disposable);
    }

    /**
     * 移除并结束Disposable
     * @param disposable
     */
    public void removeDisposable(Disposable disposable) {
        if (null != disposable) {
            mDisposable.remove(disposable);
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mDisposable.dispose();
        mDisposable.clear();
    }
}
