package com.barisetech.www.workmanage.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.barisetech.www.workmanage.bean.ImageInfo;

import java.util.List;

/**
 * Created by LJH on 2018/9/21.
 */
@Dao
public interface ImageInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insetImages(List<ImageInfo> imageInfos);

    @Query("SELECT * FROM image_info WHERE signId = :signId")
    List<ImageInfo> getImageInfos(int signId);

    @Delete
    void deleteImageInfos(List<ImageInfo> imageInfos);
}
