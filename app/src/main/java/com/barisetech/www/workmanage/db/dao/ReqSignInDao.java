package com.barisetech.www.workmanage.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.barisetech.www.workmanage.bean.signin.ReqSignIn;
import java.util.List;

/**
 * Created by LJH on 2018/9/21.
 */
@Dao
public interface ReqSignInDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insetSign(ReqSignIn reqSignIn);

    @Query("SELECT * FROM sign_in WHERE id = :id LIMIT 1")
    ReqSignIn getSign(int id);

    @Query("SELECT * FROM sign_in")
    List<ReqSignIn> getSigns();

    @Delete
    void deleteSigns(List<ReqSignIn> reqSignIns);

    @Delete
    void deleteSign(ReqSignIn reqSignIn);
}
