package com.barisetech.www.workmanage.base;

import com.barisetech.www.workmanage.bean.TokenInfo;

/**
 * Created by LJH on 2018/8/12.
 */
public class BaseModel {

    protected TokenInfo mTokenInfo;

    public BaseModel() {
        mTokenInfo = BaseApplication.getInstance().curTokenInfo;
    }
}
