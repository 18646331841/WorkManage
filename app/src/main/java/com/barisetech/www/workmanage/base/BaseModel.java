package com.barisetech.www.workmanage.base;

import com.barisetech.www.workmanage.bean.TokenInfo;
import com.barisetech.www.workmanage.callback.ModelCallBack;

/**
 * Created by LJH on 2018/8/12.
 */
public class BaseModel {

    protected TokenInfo mTokenInfo;

//    protected BaseModel() {
//        mTokenInfo = BaseApplication.getInstance().curTokenInfo;
//    }

    protected BaseModel(ModelCallBack modelCallBack) {
        mTokenInfo = BaseApplication.getInstance().curTokenInfo;
        if (null == mTokenInfo) {
            modelCallBack.unauthorized();
        }
    }
}
