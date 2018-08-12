package com.barisetech.www.workmanage.callback;

/**
 * Created by LJH on 2018/8/11.
 */
public interface ModelCallBack {
    /**
     * token过期回调
     */
    void unauthorized();

    /**
     * 获取网络请求结果
     * @param object
     */
    void netResult(Object object);
}
