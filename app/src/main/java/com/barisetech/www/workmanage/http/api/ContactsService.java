package com.barisetech.www.workmanage.http.api;

import com.barisetech.www.workmanage.base.BaseResponse;
import com.barisetech.www.workmanage.bean.contacts.ContactsBean;
import com.barisetech.www.workmanage.bean.contacts.ReqAllContacts;
import com.barisetech.www.workmanage.bean.contacts.ReqContactsNum;
import com.barisetech.www.workmanage.bean.workplan.PlanBean;
import com.barisetech.www.workmanage.bean.workplan.ReqAddPlan;
import com.barisetech.www.workmanage.bean.workplan.ReqAllPlan;
import com.barisetech.www.workmanage.bean.workplan.ReqDeletePlan;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by LJH on 2018/9/18.
 */
public interface ContactsService {
    /**
     * 获取全部联系人
     *
     * @param
     * @return
     */
    @POST("/api/Contacts")
    Observable<BaseResponse<List<ContactsBean>>> getAll(@Body ReqAllContacts reqAllContacts);

    /**
     * 联系人数量
     *
     * @return
     * @param reqContactsNum
     */
    @PUT("/api/Contacts")
    Observable<BaseResponse<Integer>> getNum(@Body ReqContactsNum reqContactsNum);
}
