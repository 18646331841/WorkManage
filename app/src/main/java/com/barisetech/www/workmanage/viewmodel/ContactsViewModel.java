package com.barisetech.www.workmanage.viewmodel;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.barisetech.www.workmanage.base.BaseConstant;
import com.barisetech.www.workmanage.base.BaseViewModel;
import com.barisetech.www.workmanage.bean.EventBusMessage;
import com.barisetech.www.workmanage.bean.FailResponse;
import com.barisetech.www.workmanage.bean.TypeResponse;
import com.barisetech.www.workmanage.bean.contacts.ContactsBean;
import com.barisetech.www.workmanage.bean.contacts.ReqAllContacts;
import com.barisetech.www.workmanage.bean.contacts.ReqContactsNum;
import com.barisetech.www.workmanage.callback.ModelCallBack;
import com.barisetech.www.workmanage.http.Config;
import com.barisetech.www.workmanage.model.ContactsModel;
import com.barisetech.www.workmanage.view.LoginActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * Created by LJH on 2018/8/10.
 */
public class ContactsViewModel extends BaseViewModel implements ModelCallBack {
    private static final String TAG = "ContactsViewModel";

    private Handler mDelivery;

    private ContactsModel contactsModel;
    private MutableLiveData<List<ContactsBean>> mObservableAll;
    private MutableLiveData<Integer> mObservableNum;

    public ContactsViewModel(@NonNull Application application) {
        super(application);
        mDelivery = new Handler(Looper.getMainLooper());

        contactsModel = new ContactsModel(this);

        mObservableAll = new MutableLiveData<>();
        mObservableAll.setValue(null);

        mObservableNum = new MutableLiveData<>();
        mObservableNum.setValue(null);
    }

    /**
     * 获取联系人数量
     *
     * @return
     */
    public Disposable reqNum(ReqContactsNum reqContactsNum) {
        Disposable disposable = contactsModel.reqNum(reqContactsNum);
        addDisposable(disposable);
        return disposable;
    }

    /**
     * 获取所有联系人
     *
     * @param reqAllContacts
     * @return
     */
    public Disposable reqAll(ReqAllContacts reqAllContacts) {
        Disposable disposable = contactsModel.reqAll(reqAllContacts);
        addDisposable(disposable);
        return disposable;
    }

    @Override
    public void netResult(Object object) {
        EventBus.getDefault().post(new EventBusMessage(BaseConstant.PROGRESS_CLOSE));
        if (object instanceof TypeResponse) {
            TypeResponse typeResponse = (TypeResponse) object;
            mDelivery.post(() -> {
                switch (typeResponse.type) {
                    case ContactsModel.TYPE_ALL:
                        mObservableAll.setValue((List<ContactsBean>) typeResponse.data);
                        break;
                    case ContactsModel.TYPE_NUM:
                        mObservableNum.setValue((Integer) typeResponse.data);
                        break;
                }
            });
        }
    }

    @Override
    public void fail(Object object) {
        EventBus.getDefault().post(new EventBusMessage(BaseConstant.PROGRESS_CLOSE));
        if (object instanceof FailResponse) {
            FailResponse failResponse = (FailResponse) object;
            if (failResponse.code == Config.ERROR_UNAUTHORIZED) {
                EventBus.getDefault().post(new EventBusMessage(LoginActivity.TAG));
            }

            mDelivery.post(() -> {
                switch (failResponse.type) {
                    case ContactsModel.TYPE_ALL:
                        mObservableAll.setValue(null);
                        break;
                    case ContactsModel.TYPE_NUM:
                        mObservableNum.setValue(0);
                        break;
                }
            });
        }
    }

    public MutableLiveData<List<ContactsBean>> getObservableAll() {
        return mObservableAll;
    }

    public MutableLiveData<Integer> getObservableNum() {
        return mObservableNum;
    }
}
