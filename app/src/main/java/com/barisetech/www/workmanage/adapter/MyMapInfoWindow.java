package com.barisetech.www.workmanage.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.Marker;
import com.barisetech.www.workmanage.R;

/**
 * Created by LJH on 2018/9/14.
 */
public class MyMapInfoWindow implements AMap.InfoWindowAdapter{

    private Activity activity;
    private OnClickToHere onClickToHere;

    public MyMapInfoWindow(Activity activity, OnClickToHere onClickToHere) {
        this.onClickToHere = onClickToHere;
        this.activity = activity;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        View infoWindow = activity.getLayoutInflater().inflate(R.layout.map_my_infowindow, null);

        render(infoWindow, marker);
        return infoWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View infoContent = activity.getLayoutInflater().inflate(R.layout.map_my_infowindow, null);

        render(infoContent, marker);
        return infoContent;
    }

    private void render(View view, Marker marker) {
        String title = marker.getTitle();

        TextView titleTV = view.findViewById(R.id.map_marker_title);
        Button toHereB = view.findViewById(R.id.map_to_here);

        titleTV.setText(title);
        toHereB.setOnClickListener(view1 -> {
            onClickToHere.toHere(marker);
        });
    }

    public interface OnClickToHere {
        void toHere(Marker marker);
    }
}
