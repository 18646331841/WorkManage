package com.barisetech.www.workmanage.bean.signin;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import com.barisetech.www.workmanage.bean.ImageInfo;

import java.util.List;

/**
 * Created by LJH on 2018/9/21.
 */
public class SignInAndImages {
    @Embedded
    public ReqSignIn signInEntity;

    @Relation(parentColumn = "id", entityColumn = "signId",entity = ImageInfo.class)
    public List<ImageInfo> imageInfos;
}
