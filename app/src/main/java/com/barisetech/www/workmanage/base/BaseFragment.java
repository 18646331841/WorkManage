package com.barisetech.www.workmanage.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by LJH on 2018/7/10.
 */
public abstract class BaseFragment extends Fragment {



    public void setToolBarHeight(View view){
        ViewGroup.LayoutParams linearParams = view.getLayoutParams();
        linearParams.height = linearParams.height+BaseApplication.getInstance().getheight(getActivity());
        view.setLayoutParams(linearParams);
        view.setPadding(view.getPaddingLeft(),BaseApplication.getInstance().getheight(getActivity()),view.getPaddingRight(),view.getPaddingBottom());
    }

}
