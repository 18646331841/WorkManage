package com.barisetech.www.workmanage.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.barisetech.www.workmanage.bean.TokenInfo;

/**
 * Created by LJH on 2018/8/8.
 */
@Dao
public interface TokenInfoDao {

    @Query("select * from token_info where id = :id LIMIT 1")
    TokenInfo loadTokenInfoById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTokenInfo(TokenInfo tokenInfo);

    @Delete
    void delete(TokenInfo tokenInfo);
}
