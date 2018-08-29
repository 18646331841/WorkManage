package com.barisetech.www.workmanage.callback;

/**
 * Created by LJH on 2018/8/11.
 */
public interface ModelCallBack {
    /**
     * 获取网络请求结果
     * @param object
     */
    void netResult(Object object);

    /**
     * 网络请求失败
     * @param errorObject
     */
    void fail(Object errorObject);
}
