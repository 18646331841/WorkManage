package com.barisetech.www.workmanage.view.fragment;


import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.base.BaseFragment;
import com.barisetech.www.workmanage.bean.ToolbarInfo;
import com.barisetech.www.workmanage.bean.map.LineStation;
import com.barisetech.www.workmanage.bean.map.MapPosition;
import com.barisetech.www.workmanage.bean.map.MarkerStation;
import com.barisetech.www.workmanage.bean.map.pipe.PipeLine;
import com.barisetech.www.workmanage.bean.map.pipe.PipeTrackInfo;
import com.barisetech.www.workmanage.bean.pipe.PipeInfo;
import com.barisetech.www.workmanage.bean.pipe.ReqAllPipe;
import com.barisetech.www.workmanage.bean.site.SiteBean;
import com.barisetech.www.workmanage.databinding.FragmentMapBinding;
import com.barisetech.www.workmanage.utils.LogUtil;
import com.barisetech.www.workmanage.utils.MapUtil;
import com.barisetech.www.workmanage.utils.ToastUtil;
import com.barisetech.www.workmanage.viewmodel.MapViewModel;
import com.barisetech.www.workmanage.viewmodel.PipeViewModel;

import java.util.ArrayList;
import java.util.HashMap;
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
    private Map<String, List<PipeTrackInfo>> curPipeTracks = new HashMap<>();
    private List<PipeLine> curPipeLines;
    private Marker curMarker;

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
//        mAMap.setMyLocationEnabled(true);

//        MarkerStation markerStation = new MarkerStation();
//        markerStation.title = "test";
//        MapPosition position = new MapPosition(39.906901f, 116.397972f);
//        markerStation.position = position;
//        addStationMarker(markerStation);

    }

    AMap.OnMarkerClickListener markerClickListener = marker -> {
        LogUtil.d("marker", marker.getTitle());

        onClickPipeLine(marker);
        return false;
    };

    private Marker addStationMarker(@NonNull MarkerStation markerStation, boolean showWindow) {
        MarkerOptions markerOptions = new MarkerOptions();
        LatLng latLng = new LatLng(markerStation.position.latitude, markerStation.position.longitude);
        markerOptions.position(latLng);
        markerOptions.draggable(false);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(getMyBitmap(markerStation.title)));
        markerOptions.title(markerStation.title);
        Marker marker = mAMap.addMarker(markerOptions);
        marker.setInfoWindowEnable(showWindow);
        return marker;
    }

    /**
     * 在地图上画管线
     * @param lineStation
     */
    private Polyline addStationLine(@NonNull LineStation lineStation) {
        LogUtil.d(TAG, "addStationLine");
        List<LatLng> latLngs = new ArrayList<>();
        if (null == lineStation.mapPositionList) {
            return null;
        }
        for (int i = 0; i < lineStation.mapPositionList.size(); i++) {
            MapPosition mapPosition = lineStation.mapPositionList.get(i);
            latLngs.add(new LatLng(mapPosition.latitude, mapPosition.longitude));
        }
        PolylineOptions polylineOptions = MapUtil.GetPolylineOptions();
        polylineOptions.addAll(latLngs);
        return mAMap.addPolyline(polylineOptions);
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

    private void onClickPipeLine(Marker marker) {
        if (marker == null || curPipeLines == null) {
            return;
        }

        if (curMarker != null && marker.getTitle().equals(curMarker.getTitle())) {
            return;
        }

        String title = marker.getTitle();
        for(int i = 0; i < curPipeLines.size(); i++) {
            PipeLine pipeLine = curPipeLines.get(i);
            if (pipeLine.startSiteMarker.getTitle().equals(title) || pipeLine.endSiteMarker.getTitle().equals(title)) {
                mAMap.clear();
                curMarker = marker;
                addStationLine(pipeLine.lineStation);
                addSiteMarker(pipeLine);
                return;
            }
        }
    }

    /**
     * 接口数据装换成地图使用数据
     * @param pipeTrackInfos
     * @return
     */
    private List<PipeLine> toData(Map<String, List<PipeTrackInfo>> pipeTrackInfos) {
        List<PipeLine> pipeLines = new ArrayList<>();

        for (Map.Entry<String, List<PipeTrackInfo>> entry : pipeTrackInfos.entrySet()) {
            List<PipeTrackInfo> value = entry.getValue();
            if (value == null || value.size() <= 0) {
                continue;
            }
            List<MapPosition> mapPositions = new ArrayList<>();
            for(int i = 0; i < value.size(); i++) {
                PipeTrackInfo pipeTrackInfo = value.get(i);
                MapPosition mapPosition = new MapPosition(pipeTrackInfo.getLatitude(), pipeTrackInfo
                        .getLongitude());
                mapPositions.add(mapPosition);
            }
            LineStation lineStation = new LineStation();
            lineStation.mapPositionList = mapPositions;
            PipeLine pipeLine = new PipeLine(entry.getKey(), lineStation);
            //增加首站、末站信息
            for(int i = 0; i < pipeInfoList.size(); i++) {
                PipeInfo pipeInfo = pipeInfoList.get(i);
                if (pipeInfo.PipeId == Integer.valueOf(entry.getKey())) {
                    int startSiteId = pipeInfo.StartSiteId;
                    List<SiteBean> siteBeans = pipeInfo.Sites;
                    for(int j = 0; j < siteBeans.size(); j++) {
                        SiteBean siteBean = siteBeans.get(j);
                        if (siteBean.SiteId == startSiteId) {
                            pipeLine.startSite = siteBean;
                        } else {
                            pipeLine.endSite = siteBean;
                        }
                    }
                }
            }
            pipeLines.add(pipeLine);
        }
        return pipeLines;
    }

    /**
     * 增加站点marker
     * @param pipeLine
     */
    private void addSiteMarker(PipeLine pipeLine) {
        List<MapPosition> mapPositionList = pipeLine.lineStation.mapPositionList;
        if (mapPositionList != null && mapPositionList.size() > 0) {

            //首站不为空，增加首站标记
            if (pipeLine.startSite != null) {
                MarkerStation markerStation = new MarkerStation();
                markerStation.position = mapPositionList.get(0);
                markerStation.title = pipeLine.startSite.Name;
                pipeLine.startSiteMarker = addStationMarker(markerStation, false);
            }
            //末站不为空，增加首站标记
            if (pipeLine.endSite != null) {
                MarkerStation markerStation = new MarkerStation();
                markerStation.position = mapPositionList.get(mapPositionList.size() - 1);
                markerStation.title = pipeLine.endSite.Name;
                pipeLine.endSiteMarker = addStationMarker(markerStation, false);
            }
        }
    }

    protected Bitmap getMyBitmap(String pm_val) {
        Bitmap bitmap = BitmapDescriptorFactory.fromResource(
                R.drawable.icon_news).getBitmap().copy(Bitmap.Config.ARGB_8888, true);
        bitmap = Bitmap.createBitmap(bitmap, 0 ,0, bitmap.getWidth(),
                bitmap.getHeight());
        Canvas canvas = new Canvas(bitmap);
        TextPaint textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(25f);
        textPaint.setColor(getResources().getColor(R.color.text_black));
        canvas.drawText(pm_val, 17, 35 ,textPaint);// 设置bitmap上面的文字位置
        return bitmap;
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
                        curPipeLines = toData(curPipeTracks);
                        if (curPipeLines.size() > 0) {
                            for(int i = 0; i < curPipeLines.size(); i++) {
                                PipeLine pipeLine = curPipeLines.get(i);
                                pipeLine.polyline = addStationLine(pipeLine.lineStation);

                                addSiteMarker(pipeLine);

                            }
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
