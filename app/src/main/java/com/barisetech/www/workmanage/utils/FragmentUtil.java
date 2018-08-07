package com.barisetech.www.workmanage.utils;

import android.content.ContextWrapper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.barisetech.www.workmanage.R;


/**
 * Created by YZJ-PC on 2018/3/27.
 */

public class FragmentUtil {


    public static void replaceSupportFragment(AppCompatActivity activity, int containerId, Class<? extends Fragment> fragmentClass, String tag, boolean addToBackStack, boolean haveAnim) {
        if (activity == null) {
            return;
        }
        try {
            FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
            if (addToBackStack) {
                transaction.addToBackStack(null);
            }
            if (haveAnim) {
                transaction.setCustomAnimations(R.anim.backstack_push_enter, R.anim.backstack_push_exit, R.anim.backstack_pop_enter, R.anim.backstack_pop_exit);
            }
            transaction.replace(containerId, fragmentClass.newInstance(), tag);
            int i = transaction.commitAllowingStateLoss();
            Log.i("Test", i + "");
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void  replaceSupportFragment(AppCompatActivity activity, int containerId, Fragment fragment, String tag, boolean addToBackStack, boolean haveAnim) {
        if (activity == null) {
            return;
        }

        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        if (haveAnim) {
            transaction.setCustomAnimations(R.anim.backstack_push_enter, R.anim.backstack_push_exit, R.anim.backstack_pop_enter, R.anim.backstack_pop_exit);
        }
        transaction.replace(containerId, fragment);
        transaction.commitAllowingStateLoss();

    }


    public static void addSupportFragment(AppCompatActivity activity, int container, Fragment fragment, String tag, boolean addToBackStack, boolean haveAnim) {
        if (activity == null) {
            return;
        }
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        if (haveAnim) {
            transaction.setCustomAnimations(R.anim.backstack_push_enter, R.anim.backstack_push_exit, R.anim.backstack_pop_enter, R.anim.backstack_pop_exit);
        }
        transaction.add(container, fragment, tag);
        transaction.commitAllowingStateLoss();
    }

    public static void showSupportFragment(AppCompatActivity activity, Fragment fragment, boolean addToBackStack, boolean haveAnim) {
        if (activity == null) {
            return;
        }
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        if (haveAnim) {
            transaction.setCustomAnimations(R.anim.backstack_push_enter, R.anim.backstack_push_exit, R.anim.backstack_pop_enter, R.anim.backstack_pop_exit);
        }
        transaction.show(fragment);
        transaction.commitAllowingStateLoss();
    }

    public static void hideSupportFragment(AppCompatActivity activity, Fragment fragment, boolean addToBackStack, boolean haveAnim) {
        if (activity == null) {
            return;
        }
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        if (haveAnim) {
            transaction.setCustomAnimations(R.anim.backstack_push_enter, R.anim.backstack_push_exit, R.anim.backstack_pop_enter, R.anim.backstack_pop_exit);
        }
        transaction.hide(fragment);
        transaction.commitAllowingStateLoss();
    }

    /*
    * 判断当前fragment的getActivity是否可用
    * */

    public static boolean judgeGetActivityCanUse(Fragment fragment) {
        if (null != fragment) {
            FragmentActivity activity = fragment.getActivity();
            if (null != activity && !activity.isFinishing()
                    && !fragment.isDetached() && activity instanceof ContextWrapper) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
