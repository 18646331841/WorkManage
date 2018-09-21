package com.barisetech.www.workmanage.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import com.barisetech.www.workmanage.bean.ImageInfo;
import com.barisetech.www.workmanage.bean.signin.ReqSignIn;
import com.barisetech.www.workmanage.bean.signin.SignInAndImages;

import java.util.List;

import io.reactivex.Maybe;

/**
 * Created by LJH on 2018/9/21.
 */
@Dao
public abstract class SignInAndImagesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(ReqSignIn reqSignIn);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(List<ImageInfo> imageInfo);

    @Delete
    public abstract void delete(ReqSignIn reqSignIn);

    @Delete
    public abstract void delete(List<ImageInfo> imageInfos);

    @Transaction
    public void insert(SignInAndImages signInAndImages) {
        insert(signInAndImages.signInEntity);
        insert(signInAndImages.imageInfos);
    }

    @Transaction
    public void delete(SignInAndImages signInAndImages) {
        delete(signInAndImages.signInEntity);
        delete(signInAndImages.imageInfos);
    }

    @Transaction @Query("SELECT * FROM sign_in")
    public abstract List<SignInAndImages> getSignInAndImages();

    @Transaction @Query("SELECT * FROM sign_in")
    public abstract Maybe<List<SignInAndImages>> getSignInAndImagesRx();
}
