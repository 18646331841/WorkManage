package com.barisetech.www.workmanage.bean.worktask;

import com.barisetech.www.workmanage.base.BaseConstant;
import com.barisetech.www.workmanage.bean.ImageInfo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.Serializable;
import java.util.List;

/**
 * Created by LJH on 2018/9/18.
 */
public class TaskSiteBean implements Serializable {

    /**
     * Name : ttx
     * SiteId : 1
     * Longitude : 0.1
     * Latitude : 0.2
     * WorkImageList :
     * State :  状态1正常打卡2离线打卡3未打卡4打卡失败
     * SiteState :   站点状态1正常2异常
     */

    public String Name;
    public int SiteId;
    public String DateTime;
    public double Longitude;
    public double Latitude;
    public String Remark;
    public int State;
    public int SiteState;
    public double UserLongitude;
    public double UserLatitude;
    public List<ImageInfo> WorkImageList;

    /**
     * 为了传范围到签到界面，自己加的
     */
    public double range;

    public String showStatus(String role) {
        String result = BaseConstant.STATUS_TASK[2];
        if (State > 0) {
            switch (State) {
                case BaseConstant.STATUS_COMPLETED:
                    result = BaseConstant.STATUS_TASK[3];
                    break;
                case BaseConstant.STATUS_OFFLINE:
                    if (!role.equals(BaseConstant.ROLE_SUPER_ADMINS) && !role.equals(BaseConstant.ROLE_ADMINS)) {
                        result = BaseConstant.STATUS_TASK[1];
                    }
                    break;
                case BaseConstant.STATUS_UNCOMPLETED:
                    if (!role.equals(BaseConstant.ROLE_SUPER_ADMINS) && !role.equals(BaseConstant.ROLE_ADMINS)) {
                        result = BaseConstant.STATUS_TASK[0];
                    }
                    break;
                case BaseConstant.STATUS_FAIL:
                    if (!role.equals(BaseConstant.ROLE_SUPER_ADMINS) && !role.equals(BaseConstant.ROLE_ADMINS)) {
                        result = BaseConstant.STATUS_TASK[0];
                    }
                    break;
            }
        }
        return result;
    }

    public Object deepClone() throws IOException, ClassNotFoundException {
        // 将对象写到流里
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        ObjectOutputStream oo = new ObjectOutputStream(bo);
        oo.writeObject(this);
        // 从流里读出来
        ByteArrayInputStream bi = new ByteArrayInputStream(bo.toByteArray());
        ObjectInputStream oi = new ObjectInputStream(bi);
        return (oi.readObject());
    }
}
