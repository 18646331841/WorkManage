package com.barisetech.www.workmanage.view.fragment;


import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.PolylineOptions;
import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.base.BaseFragment;
import com.barisetech.www.workmanage.bean.ToolbarInfo;
import com.barisetech.www.workmanage.bean.map.LineStation;
import com.barisetech.www.workmanage.bean.map.MapPosition;
import com.barisetech.www.workmanage.bean.map.MarkerStation;
import com.barisetech.www.workmanage.bean.map.pipe.PipeTrackInfo;
import com.barisetech.www.workmanage.bean.pipe.PipeInfo;
import com.barisetech.www.workmanage.bean.pipe.ReqAllPipe;
import com.barisetech.www.workmanage.databinding.FragmentMapBinding;
import com.barisetech.www.workmanage.utils.LogUtil;
import com.barisetech.www.workmanage.utils.MapUtil;
import com.barisetech.www.workmanage.utils.ToastUtil;
import com.barisetech.www.workmanage.viewmodel.MapViewModel;
import com.barisetech.www.workmanage.viewmodel.PipeViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends BaseFragment {

    public static final String TAG = "MapFragment";
    private MapView mMapView;
    private AMap mAMap;
    private MyLocationStyle myLocationStyle;
    private MapViewModel mapViewModel;
    private PipeViewModel pipeViewModel;
    FragmentMapBinding mBinding;

    private List<PipeInfo> pipeInfoList = new ArrayList<>();
    private Map<String, List<PipeTrackInfo>> curPipeTracks;

    public static MapFragment newInstance() {
        MapFragment fragment = new MapFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false);
        setToolBarHeight(mBinding.toolbar.getRoot());
        mBinding.setFragment(this);
        ToolbarInfo toolbarInfo = new ToolbarInfo();
        toolbarInfo.setTitle(getString(R.string.title_map));
        observableToolbar.set(toolbarInfo);
        initView(savedInstanceState);

        return mBinding.getRoot();
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

    private void initView(Bundle savedInstanceState) {
        mMapView = mBinding.map;
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
        // 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        mAMap.setMyLocationEnabled(true);

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

    /**
     * 在地图上画管线
     * @param lineStation
     */
    private void addStationLine(@NonNull LineStation lineStation) {
        List<LatLng> latLngs = new ArrayList<>();
        if (null == lineStation.mapPositionList) {
            return;
        }
        for (int i = 0; i < lineStation.mapPositionList.size(); i++) {
            MapPosition mapPosition = lineStation.mapPositionList.get(i);
            latLngs.add(new LatLng(mapPosition.latitude, mapPosition.longitude));
        }
        PolylineOptions polylineOptions = MapUtil.GetPolylineOptions();
        polylineOptions.addAll(latLngs);
        mAMap.addPolyline(polylineOptions);
    }

    private void getPipeInfo(int fromIndex, int toIndex) {
        ReqAllPipe reqAllPipe = new ReqAllPipe();
        reqAllPipe.setPipeId("0");
        reqAllPipe.setStartIndex(String.valueOf(fromIndex));
        reqAllPipe.setNumberOfRecords(String.valueOf(toIndex));
        pipeViewModel.reqAllPipe(reqAllPipe);
    }

    @Override
    public void bindViewModel() {
        mapViewModel = ViewModelProviders.of(this).get(MapViewModel.class);
        pipeViewModel = ViewModelProviders.of(this).get(PipeViewModel.class);
    }

    @Override
    public void subscribeToModel() {
        if (!pipeViewModel.getmObservablePipeNum().hasObservers()) {
            pipeViewModel.getmObservablePipeNum().observe(this, integer -> {
                if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
                    if (null != integer && integer > 0) {
                        pipeInfoList.clear();
                        getPipeInfo(0, integer);
                    } else {
                        ToastUtil.showToast("未找到管线");
                    }
                }
            });
        }
        if (!pipeViewModel.getmObservableAllPipe().hasObservers()) {
            pipeViewModel.getmObservableAllPipe().observe(this, pipeInfos -> {
                if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
                    if (null != pipeInfos) {
                        if (pipeInfos.size() > 0) {
                            pipeInfoList.addAll(pipeInfos);

                            //接口方不给提供获取多个管线路径的接口，这里只能循环调用每个
                            //管线的路径接口
                            curPipeTracks.clear();
                            mapViewModel.reqAllPipeTrack(pipeInfos);

                        } else {
                            ToastUtil.showToast("获取管线失败");
                        }
                    } else {
                        ToastUtil.showToast("获取管线失败");
                    }
                }
            });
        }

        if (!mapViewModel.getmObservableTrack().hasObservers()) {
            mapViewModel.getmObservableTrack().observe(this, pipeTrackInfos -> {
                if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
                    if (null != pipeTrackInfos && pipeTrackInfos.size() > 0) {
                        curPipeTracks.putAll(pipeTrackInfos);
                        List<MapPosition> mapPositions = new ArrayList<>();
                        for (Map.Entry<String, List<PipeTrackInfo>> entry : pipeTrackInfos.entrySet()) {

                        }
                    }
                }
            });
        }

        if (null == pipeInfoList || pipeInfoList.size() <= 0) {
            pipeViewModel.reqPipeNum();
        }
    }
}
