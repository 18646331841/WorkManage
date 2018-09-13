package com.barisetech.www.workmanage.utils;

import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LJH on 2018/9/13.
 */
public class MapUtil {

    /**
     * 设置线条中的纹理的方法
     * @return PolylineOptions
     * */
    public static PolylineOptions GetPolylineOptions(){
        //添加纹理图片
        List<BitmapDescriptor> textureList = new ArrayList<>();
        BitmapDescriptor mRedTexture = BitmapDescriptorFactory
                .fromAsset("map_line_red.png");
        BitmapDescriptor mBlueTexture = BitmapDescriptorFactory
                .fromAsset("map_line_blue.png");
        BitmapDescriptor mGreenTexture = BitmapDescriptorFactory
                .fromAsset("map_line_green.png");
        textureList.add(mRedTexture);
        textureList.add(mBlueTexture);
        textureList.add(mGreenTexture);
        // 添加纹理图片对应的顺序
        List<Integer> textureIndexs = new ArrayList<>();
        textureIndexs.add(0);
        textureIndexs.add(1);
        textureIndexs.add(2);
        PolylineOptions polylienOptions=new PolylineOptions();
        polylienOptions.setCustomTextureList(textureList);
        polylienOptions.setCustomTextureIndex(textureIndexs);
        polylienOptions.setUseTexture(true);
        polylienOptions.width(7.0f);
        return polylienOptions;
    }

}
