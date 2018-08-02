package com.barisetech.www.workmanage.view.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.autonavi.ae.route.model.RouteConstant;
import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.bean.LineStation;
import com.barisetech.www.workmanage.bean.MapPosition;
import com.barisetech.www.workmanage.bean.MarkerStation;
import com.barisetech.www.workmanage.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment {

    public static final String TAG = "MapFragment";
    private MapView mMapView;
    private AMap mAMap;
    private MyLocationStyle myLocationStyle;

    public MapFragment() {
        // Required empty public constructor
    }

    public static MapFragment newInstance() {
        MapFragment fragment = new MapFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_map, container, false);

        initView(root, savedInstanceState);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mMapView.onDestroy();
    }

    private void initView(View root, Bundle savedInstanceState) {
        mMapView = (MapView) root.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        initMap();
    }

    private void initMap() {
        if (null == mAMap) {
            mAMap = mMapView.getMap();
        }
        mAMap.setOnMarkerClickListener(markerClickListener);
        //初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);
        // 连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（
        // 1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle = new MyLocationStyle();
        //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.interval(2000);
        //设置定位蓝点的Style
        mAMap.setMyLocationStyle(myLocationStyle);
        //aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
        mAMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。

        MarkerStation markerStation = new MarkerStation();
        markerStation.title = "test";
        MapPosition position = new MapPosition(39.906901f, 116.397972f);
        markerStation.position = position;
        addStationMarker(markerStation);

    }

    AMap.OnMarkerClickListener markerClickListener = marker -> {
        LogUtil.d("marker", marker.getTitle());
        return false;
    };

    private void addStationMarker(@NonNull MarkerStation markerStation) {
        MarkerOptions markerOptions = new MarkerOptions();
        LatLng latLng = new LatLng(markerStation.position.latitude, markerStation.position.longitude);
        markerOptions.position(latLng);
        markerOptions.draggable(false);
        markerOptions.title(markerStation.title);

        mAMap.addMarker(markerOptions);
    }

    private void addStationLine(@NonNull LineStation lineStation) {
        List<LatLng> latLngs = new ArrayList<>();
        if (null == lineStation.mapPositionList) {
            return;
        }
        for (int i = 0; i < lineStation.mapPositionList.size(); i++) {
            MapPosition mapPosition = lineStation.mapPositionList.get(i);
            latLngs.add(new LatLng(mapPosition.latitude, mapPosition.longitude));
        }
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.addAll(latLngs)
                .color(lineStation.color)
                .width(lineStation.width);
        mAMap.addPolyline(polylineOptions);
    }
}
