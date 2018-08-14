package com.barisetech.www.workmanage.base;

import android.text.TextUtils;

import com.barisetech.www.workmanage.callback.ModelCallBack;
import com.barisetech.www.workmanage.http.Config;
import com.barisetech.www.workmanage.utils.LogUtil;
import com.barisetech.www.workmanage.utils.SharedPreferencesUtil;

/**
 * Created by LJH on 2018/8/12.
 */
public class BaseModel {

    protected String mToken;

//    protected BaseModel() {
//        mToken = BaseApplication.getInstance().curTokenInfo;
//    }

    protected BaseModel(ModelCallBack modelCallBack) {
        mToken = SharedPreferencesUtil.getInstance().getString(BaseConstant.SP_TOKEN, "");
        LogUtil.d("BaseModel", "get Application token= " + mToken);
        if (TextUtils.isEmpty(mToken)) {
            modelCallBack.fail(Config.ERROR_UNAUTHORIZED);
        }
    }
}
