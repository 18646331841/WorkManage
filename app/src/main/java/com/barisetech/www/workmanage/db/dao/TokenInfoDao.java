package com.barisetech.www.workmanage.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.barisetech.www.workmanage.bean.TokenInfo;

import io.reactivex.Flowable;

/**
 * Created by LJH on 2018/8/9.
 */
@Dao
public interface TokenInfoDao {
    @Query("select * from token_info where id = :id LIMIT 1")
    TokenInfo loadTokenInfoSync(int id);

    @Query("select * from token_info where id = :id LIMIT 1")
    Flowable<TokenInfo> loadTokenInfoObs(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(TokenInfo tokenInfo);

    @Update
    void update(TokenInfo tokenInfo);

    @Delete
    void delete(TokenInfo tokenInfo);

    @Query("select * from token_info where id = :id LIMIT 1")
    LiveData<TokenInfo> loadTokenInfo(int id);
}
