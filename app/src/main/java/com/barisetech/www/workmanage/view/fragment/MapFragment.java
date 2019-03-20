package com.barisetech.www.workmanage.view.fragment;


import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.maps.model.Text;
import com.amap.api.maps.model.TextOptions;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.AMapNaviViewListener;
import com.amap.api.navi.AMapNaviViewOptions;
import com.amap.api.navi.enums.NaviType;
import com.amap.api.navi.model.AMapCalcRouteResult;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapModelCross;
import com.amap.api.navi.model.AMapNaviCameraInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviRouteNotifyData;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AMapServiceAreaInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.autonavi.tbt.TrafficFacilityInfo;
import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.adapter.MyMapInfoWindow;
import com.barisetech.www.workmanage.base.BaseFragment;
import com.barisetech.www.workmanage.bean.ToolbarInfo;
import com.barisetech.www.workmanage.bean.alarm.AlarmInfo;
import com.barisetech.www.workmanage.bean.map.LineStation;
import com.barisetech.www.workmanage.bean.map.MapPosition;
import com.barisetech.www.workmanage.bean.map.MarkerStation;
import com.barisetech.www.workmanage.bean.map.pipe.PipeLine;
import com.barisetech.www.workmanage.bean.map.pipe.PipeTrackInfo;
import com.barisetech.www.workmanage.bean.pipe.PipeInfo;
import com.barisetech.www.workmanage.bean.pipe.ReqAllPipe;
import com.barisetech.www.workmanage.bean.pipecollections.PipeCollections;
import com.barisetech.www.workmanage.bean.pipecollections.ReqAllPc;
import com.barisetech.www.workmanage.bean.site.SiteBean;
import com.barisetech.www.workmanage.databinding.FragmentMapBinding;
import com.barisetech.www.workmanage.utils.BitmapUtil;
import com.barisetech.www.workmanage.utils.LogUtil;
import com.barisetech.www.workmanage.utils.MapUtil;
import com.barisetech.www.workmanage.utils.ToastUtil;
import com.barisetech.www.workmanage.viewmodel.MapViewModel;
import com.barisetech.www.workmanage.viewmodel.PipeCollectionsViewModel;
import com.barisetech.www.workmanage.viewmodel.PipeViewModel;
import com.barisetech.www.workmanage.widget.twomenu.ChildView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MapFragment extends BaseFragment {

    public static final String TAG = "MapFragment";
    private TextureMapView mMapView;
    private AMap mAMap;

    private AMapNaviView mAMapNaviView;
    private AMapLocationClient mlocationClient;
    /**
     * 起点坐标
     */
    private final List<NaviLatLng> startList = new ArrayList<>();

    /**
     * 终点坐标
     */
    private final List<NaviLatLng> endList = new ArrayList<>();
    /**
     * 导航方式
     */
    private int naviWay = WAY_DRIVE;
    public static final int WAY_DRIVE = 1;
    public static final int WAY_WALK = 2;
    public static final int WAY_RIDE = 3;
    FragmentMapBinding mBinding;

    private MyLocationStyle myLocationStyle;
    private MapViewModel mapViewModel;
    private PipeViewModel pipeViewModel;
    private PipeCollectionsViewModel pipeCollectionsViewModel;

    private List<PipeCollections> pipeCollectionsList = new ArrayList<>();

    private List<PipeInfo> pipeInfoList = new ArrayList<>();
    private Map<String, List<PipeTrackInfo>> curPipeTracks = new HashMap<>();
    private List<PipeLine> curPipeLines;
    private Marker curStartMarker;
    private Marker curEndMarker;
    private AMapNavi mAMapNavi;
    private Marker curClickMarker;

    private boolean popIsShow = false;

    private ArrayList<View> mViewArray = new ArrayList<>();

    private static final String PIPE_ID = "pipeId";
    private static final String ALARM_ID = "alarmId";
    private String curPipeId;
    private AlarmInfo curAlarmInfo;
    private ToolbarInfo toolbarInfo;

    public static MapFragment newInstance(String pipeId) {
        MapFragment fragment = new MapFragment();
        if (!TextUtils.isEmpty(pipeId)) {
            Bundle bundle = new Bundle();
            bundle.putString(PIPE_ID, pipeId);
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    public static MapFragment newInstance(AlarmInfo alarmInfo) {
        MapFragment fragment = new MapFragment();
        if (alarmInfo != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(ALARM_ID, alarmInfo);
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            curAlarmInfo = (AlarmInfo) getArguments().getSerializable(ALARM_ID);
            if (curAlarmInfo != null) {
                curPipeId = String.valueOf(curAlarmInfo.getPipeId());
            } else {
                curPipeId = getArguments().getString(PIPE_ID, "");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false);
        setToolBarHeight(mBinding.toolbar.getRoot());
        mBinding.setFragment(this);
        toolbarInfo = new ToolbarInfo();
        toolbarInfo.setTitle(getString(R.string.title_map));
        toolbarInfo.setBackText("管线");
        toolbarInfo.setOneText("卫星图");
        observableToolbar.set(toolbarInfo);

//        initMenu();
        initView(savedInstanceState);

        return mBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        if (mAMapNaviView.getVisibility() == View.VISIBLE) {
            mAMapNaviView.onResume();
        }
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
        if (mAMapNaviView.getVisibility() == View.VISIBLE) {
            mAMapNaviView.onPause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBinding.menuView.close();
        mMapView.onDestroy();
        if (mAMapNavi != null) {
            mAMapNavi.stopNavi();
            mAMapNavi.destroy();
        }
        if (mAMapNaviView != null) {
            mAMapNaviView.onDestroy();
        }
        if (null != mlocationClient) {
            mlocationClient.onDestroy();
            mlocationClient = null;
        }
    }

    private void initView(Bundle savedInstanceState) {
        mMapView = mBinding.map;
        mMapView.onCreate(savedInstanceState);

        mAMapNaviView = mBinding.mapNavi;
        mAMapNaviView.setAMapNaviViewListener(aMapNaviViewListener);
        mAMapNaviView.onCreate(savedInstanceState);
        setAmapNaviViewOptions();
        initMap();

        mBinding.toolbar.tvOne.setOnClickListener(view -> {
            if (mBinding.toolbar.tvOne.getText().equals("卫星图")) {
                mAMap.setMapType(AMap.MAP_TYPE_SATELLITE);
                mBinding.toolbar.tvOne.setText("普通图");
            } else {
                mAMap.setMapType(AMap.MAP_TYPE_NORMAL);
                mBinding.toolbar.tvOne.setText("卫星图");
            }
        });
    }

    /**
     * 初始化菜单
     *
     * @param oneList
     * @param sparseArray
     */
    private void initMenu(ArrayList<String> oneList, SparseArray<LinkedList<String>> sparseArray) {
        ChildView childView = new ChildView(getContext());
        childView.setActivity(getActivity());
        childView.setDatas(oneList, sparseArray);
        childView.setOnSelectListener(showText -> onRefresh(childView, showText));
        mViewArray.add(childView);
        mBinding.menuView.setValue(null, mViewArray);
        mBinding.menuView.initPopupWindow();

        mBinding.toolbar.tvBack.setOnClickListener(view -> {
//            if (popIsShow) {
//                mBinding.menuView.close();
//                popIsShow = false;
//            } else {
                mBinding.menuView.show();
//                popIsShow = true;
//            }
        });
    }

    //视图被点击后刷新数据
    private void onRefresh(View view, String showText) {
        LogUtil.d(TAG, "menu click = " + showText);
        mBinding.menuView.onPressBack();
        if (curPipeLines == null || curPipeLines.size() <= 0) {
            return;
        }
        for (int i = 0; i < curPipeLines.size(); i++) {
            PipeLine pipeLine = curPipeLines.get(i);
            if (pipeLine.startSiteMarker == null || pipeLine.endSiteMarker == null) {
                if (pipeLine.polyline != null) {
                    pipeLine.polyline.setVisible(false);
                }
                continue;
            }
            if (pipeLine.pipeName.equals(showText)) {
                pipeLine.show(true);
                curStartMarker = pipeLine.startSiteMarker;
                curEndMarker = pipeLine.endSiteMarker;
                setMapCenter(curStartMarker.getPosition());
                curStartMarker.setInfoWindowEnable(true);
                curEndMarker.setInfoWindowEnable(true);
            } else {
                pipeLine.show(false);
            }
        }
//        int position = getPositon(view);
    }

    //获取当前的view位置
    private int getPositon(View tView) {
        for (int i = 0; i < mViewArray.size(); i++) {
            if (mViewArray.get(i) == tView) {
                return i;
            }
        }
        return -1;
    }

    private void initMap() {
        if (null == mAMap) {
            mAMap = mMapView.getMap();
        }
        mAMap.moveCamera(CameraUpdateFactory.zoomTo(5));
        mAMap.setOnMarkerClickListener(markerClickListener);
        //初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);
        // 连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（
        // 1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle = new MyLocationStyle();
        //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.interval(2000);
        //设置定位蓝点的Style
        mAMap.setMyLocationStyle(myLocationStyle);
        mAMap.setInfoWindowAdapter(new MyMapInfoWindow(getActivity(), onClickToHere));
        //aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
        // 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
//        mAMap.setMyLocationEnabled(true);

        mAMap.setOnMapTouchListener(motionEvent -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                if (curClickMarker != null && curClickMarker.isInfoWindowShown()) {
                    curClickMarker.hideInfoWindow();
                }
            }
        });
    }

    AMap.OnMarkerClickListener markerClickListener = marker -> {
        LogUtil.d("marker", marker.getTitle());
        if (marker.getTitle().contains("警报")) {
            return false;
        }
        onClickPipeLine(marker);
        curClickMarker = marker;
        return false;
    };

    private Marker addStationMarker(@NonNull MarkerStation markerStation, boolean showWindow) {
        MarkerOptions markerOptions = new MarkerOptions();
        LatLng latLng = new LatLng(markerStation.position.latitude, markerStation.position.longitude);
        markerOptions.position(latLng);
        markerOptions.draggable(false);
//        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(getMyBitmap(markerStation.title)));
        markerOptions.title(markerStation.title);
        markerOptions.snippet(markerStation.snippet);
        Marker marker = mAMap.addMarker(markerOptions);
        marker.setInfoWindowEnable(showWindow);
        return marker;
    }

    private Marker addRedMarker(@NonNull MarkerStation markerStation, boolean showWindow) {
        MarkerOptions markerOptions = new MarkerOptions();
        LatLng latLng = new LatLng(markerStation.position.latitude, markerStation.position.longitude);
        markerOptions.position(latLng);
        markerOptions.draggable(false);
        Bitmap bitmap = BitmapDescriptorFactory.fromResource(R.drawable.red_marker).getBitmap();
        Bitmap red = BitmapUtil.bitMapScale(bitmap, 3);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(red));
        markerOptions.title(markerStation.title);
        markerOptions.snippet(markerStation.snippet);
        Marker marker = mAMap.addMarker(markerOptions);
        marker.setInfoWindowEnable(showWindow);
        return marker;
    }

    private Text addStationText(@NonNull MarkerStation markerStation) {
        TextOptions textOptions = new TextOptions();
        textOptions.backgroundColor(Color.TRANSPARENT);
        textOptions.fontSize(30);
        LatLng latLng = new LatLng(markerStation.position.latitude, markerStation.position.longitude);
        textOptions.position(latLng);
        textOptions.text(markerStation.title);

        return mAMap.addText(textOptions);
    }

    /**
     * 在地图上画管线
     *
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
        Polyline polyline = mAMap.addPolyline(polylineOptions);
        return polyline;
    }

    private void getPipeInfo(int fromIndex, int toIndex) {
        ReqAllPipe reqAllPipe = new ReqAllPipe();
        reqAllPipe.setPipeId("");
        reqAllPipe.setStartIndex("0");
        reqAllPipe.setNumberOfRecords("0");
//        reqAllPipe.setStartIndex(String.valueOf(fromIndex));
//        reqAllPipe.setNumberOfRecords(String.valueOf(toIndex));
        pipeViewModel.reqAllPipe(reqAllPipe);
    }

    @Override
    public void bindViewModel() {
        mapViewModel = ViewModelProviders.of(this).get(MapViewModel.class);
        pipeViewModel = ViewModelProviders.of(this).get(PipeViewModel.class);
        pipeCollectionsViewModel = ViewModelProviders.of(this).get(PipeCollectionsViewModel.class);
    }

    private MyMapInfoWindow.OnClickToHere onClickToHere = marker -> {
        LogUtil.d(TAG, "toHere = " + marker.getTitle());
        for (int i = 0; i < curPipeLines.size(); i++) {
            PipeLine pipeLine = curPipeLines.get(i);
            if (pipeLine.startSiteMarker == null || pipeLine.endSiteMarker == null) {
                continue;
            }
            if (pipeLine.startSiteMarker.getSnippet().equals(marker.getSnippet()) || pipeLine.endSiteMarker
                    .getSnippet().equals(marker.getSnippet()) || marker.getTitle().contains("警报")) {
                endList.clear();
                endList.add(new NaviLatLng(marker.getPosition().latitude, marker.getPosition().longitude));
                initLocation();
                marker.hideInfoWindow();
                mlocationClient.startLocation();
            }
        }
    };

    private void onClickPipeLine(Marker marker) {
        if (marker == null || curPipeLines == null) {
            return;
        }

        if (curStartMarker != null && marker.getSnippet().equals(curStartMarker.getSnippet())) {
            //已经点击过的marker不再执行此操作
            return;
        }
        if (curEndMarker != null && marker.getSnippet().equals(curEndMarker.getSnippet())) {
            //已经点击过的marker不再执行此操作
            return;
        }

        String snippet = marker.getSnippet();
        for (int i = 0; i < curPipeLines.size(); i++) {
            PipeLine pipeLine = curPipeLines.get(i);
            if (pipeLine.startSiteMarker == null || pipeLine.endSiteMarker == null) {
                if (pipeLine.polyline != null) {
                    pipeLine.polyline.setVisible(false);
                }
                continue;
            }

            if (pipeLine.startSiteMarker.getSnippet().equals(snippet) || pipeLine.endSiteMarker.getSnippet().equals
                    (snippet)) {
                curStartMarker = pipeLine.startSiteMarker;
                curEndMarker = pipeLine.endSiteMarker;
                curStartMarker.setInfoWindowEnable(true);
                curEndMarker.setInfoWindowEnable(true);
//                addStationLine(pipeLine.lineStation);
//                addSiteMarker(pipeLine, true);
            } else {
                pipeLine.show(false);
            }
        }
    }

    /**
     * 设置地图中心点
     * @param latLng
     */
    private void setMapCenter(LatLng latLng) {
        if (latLng == null) {
            return;
        }
        mAMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng, 5, 0, 0)));
    }

    /**
     * 接口数据装换成地图使用数据
     *
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
            for (int i = 0; i < value.size(); i++) {
                PipeTrackInfo pipeTrackInfo = value.get(i);
                MapPosition mapPosition = new MapPosition(pipeTrackInfo.getLatitude(), pipeTrackInfo
                        .getLongitude());
                mapPositions.add(mapPosition);
            }
            LineStation lineStation = new LineStation();
            lineStation.mapPositionList = mapPositions;
            PipeLine pipeLine = new PipeLine(entry.getKey(), lineStation);
            //增加首站、末站信息
            for (int i = 0; i < pipeInfoList.size(); i++) {
                PipeInfo pipeInfo = pipeInfoList.get(i);
                if (pipeInfo.PipeId == Integer.valueOf(entry.getKey())) {
                    pipeLine.pipeName = pipeInfo.Name;
                    int startSiteId = pipeInfo.StartSiteId;
                    List<SiteBean> siteBeans = pipeInfo.Sites;
                    for (int j = 0; j < siteBeans.size(); j++) {
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
     *
     * @param pipeLine
     */
    private void addSiteMarker(PipeLine pipeLine, boolean showWindow) {
        List<MapPosition> mapPositionList = pipeLine.lineStation.mapPositionList;
        if (mapPositionList != null && mapPositionList.size() > 0) {

            //增加首站标记和末站标记
            if (pipeLine.startSite != null && pipeLine.endSite != null) {
                LogUtil.d(TAG, "siteId = " + pipeLine.startSite.Name + " startMarker");
                MarkerStation markerStation = new MarkerStation();
//                markerStation.position = new MapPosition(pipeLine.startSite.Latitude, pipeLine.startSite.Longitude);
                markerStation.position = mapPositionList.get(0);
                markerStation.title = pipeLine.startSite.Name;
//                markerStation.snippet = String.valueOf(pipeLine.startSite.SiteId);
                markerStation.snippet = String.valueOf(pipeLine.startSite.SiteId + "_" + pipeLine.endSite.SiteId);
                pipeLine.startSiteMarker = addStationMarker(markerStation, showWindow);
                pipeLine.startSiteText = addStationText(markerStation);

                LogUtil.d(TAG, "siteId = " + pipeLine.endSite.Name + " endMarker");
                MarkerStation markerStation2 = new MarkerStation();
//                markerStation.position = new MapPosition(pipeLine.endSite.Latitude, pipeLine.endSite.Longitude);
                markerStation2.position = mapPositionList.get(mapPositionList.size() - 1);
                markerStation2.title = pipeLine.endSite.Name;
//                markerStation.snippet = String.valueOf(pipeLine.endSite.SiteId);
                markerStation2.snippet = String.valueOf(pipeLine.startSite.SiteId + "_" + pipeLine.endSite.SiteId);
                pipeLine.endSiteMarker = addStationMarker(markerStation2, showWindow);
                pipeLine.endSiteText = addStationText(markerStation2);
            }
        }
    }

    /**
     * 增加警报标记
     *
     * @param pipeLine
     * @param alarmInfo
     * @param showWindow
     */
    private void addAlarmMarker(PipeLine pipeLine, AlarmInfo alarmInfo, boolean showWindow) {
        if (alarmInfo != null) {
            MarkerStation markerStation = new MarkerStation();
            markerStation.position = new MapPosition(alarmInfo.getLatitude(), alarmInfo
                    .getLongitude());
            markerStation.title = "警报 " + (alarmInfo.isLifted() ? "已解除" : "未解除");
            markerStation.snippet = alarmInfo.isLifted() ? "已解除" : "未解除";
            pipeLine.alarmMarker = addRedMarker(markerStation, showWindow);
        }
    }

    protected Bitmap getMyBitmap(String pm_val) {
        Bitmap bitmap = BitmapDescriptorFactory.fromResource(
                R.drawable.icon_news).getBitmap().copy(Bitmap.Config.ARGB_8888, true);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight());
        Canvas canvas = new Canvas(bitmap);
        TextPaint textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(25f);
        textPaint.setColor(getResources().getColor(R.color.text_black));
        canvas.drawText(pm_val, 17, 35, textPaint);// 设置bitmap上面的文字位置
        return bitmap;
    }

    @Override
    public void subscribeToModel() {
        if (!pipeCollectionsViewModel.getmObservableAllPC().hasObservers()) {
            pipeCollectionsViewModel.getmObservableAllPC().observe(this, pipeCollections -> {
                if (this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {

                    if (null != pipeCollections && pipeCollections.size() > 0) {
                        pipeCollectionsList.addAll(pipeCollections);

                        ArrayList<String> oneList = new ArrayList<>();
                        SparseArray<LinkedList<String>> sparseArray = new SparseArray<>();

                        for (int i = 0; i < pipeCollections.size(); i++) {
                            PipeCollections pipeCollections1 = pipeCollections.get(i);
                            oneList.add(i, pipeCollections1.getName());
                            List<PipeInfo> pipeInfos = pipeCollections1.getPipeCollects();
                            LinkedList<String> twoList = new LinkedList<>();
                            if (pipeInfos != null && pipeInfos.size() > 0) {
                                for (int j = 0; j < pipeInfos.size(); j++) {
                                    twoList.add(pipeInfos.get(j).Name);
                                }
                            }
                            sparseArray.put(i, twoList);
                        }

                        initMenu(oneList, sparseArray);
                    } else {
                        ToastUtil.showToast("获取管线集合失败");
                    }
                }
            });
        }

        if (!pipeCollectionsViewModel.getmObservableNum().hasObservers()) {
            pipeCollectionsViewModel.getmObservableNum().observe(this, integer -> {
                if (this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
                    if (null != integer && integer > 0) {
                        ReqAllPc reqAllPc = new ReqAllPc();
                        reqAllPc.setStartIndex("0");
                        reqAllPc.setNumberOfRecords("0");
//                        reqAllPc.setStartIndex(String.valueOf(0));
//                        reqAllPc.setNumberOfRecords(String.valueOf(integer - 1));
                        reqAllPc.setPipeCollectionId("");

                        pipeCollectionsViewModel.reqAllPc(reqAllPc);
                    } else {
                        ToastUtil.showToast("未找到管线集合");
                    }
                }
            });
        }

        if (null == pipeCollectionsList || pipeCollectionsList.size() <= 0) {
            pipeCollectionsViewModel.reqPcNum();
        }

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
                            for (int i = 0; i < curPipeLines.size(); i++) {
                                PipeLine pipeLine = curPipeLines.get(i);
                                pipeLine.polyline = addStationLine(pipeLine.lineStation);
                                addSiteMarker(pipeLine, false);
                                //如果curPipeid 不为空，只显示这条管线
                                if (!TextUtils.isEmpty(curPipeId)) {
                                    LogUtil.d(TAG, "curPipeId = " + curPipeId);
                                    if (!pipeLine.pipeId.equals(curPipeId)) {
                                        pipeLine.show(false);
                                    } else {
                                        if (curAlarmInfo != null) {
                                            addAlarmMarker(pipeLine, curAlarmInfo, true);
                                        }
                                    }
                                }

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

    private void showMap() {
        mAMapNaviView.setVisibility(View.GONE);
        mMapView.setVisibility(View.VISIBLE);
    }

    private void showNavi() {
        mAMapNaviView.setVisibility(View.VISIBLE);
        mMapView.setVisibility(View.GONE);
    }

    /**
     * 初始化导航
     */
    private void initNavi() {
        mAMapNavi = AMapNavi.getInstance(getActivity().getApplicationContext());
        mAMapNavi.setUseInnerVoice(true);
        mAMapNavi.addAMapNaviListener(aMapNaviListener);
        LogUtil.d(TAG, "initNavi");
    }

    /**
     * 设置导航参数
     */
    private void setAmapNaviViewOptions() {
        if (mAMapNaviView == null) {
            return;
        }
        AMapNaviViewOptions viewOptions = new AMapNaviViewOptions();
        viewOptions.setSettingMenuEnabled(false);//设置菜单按钮是否在导航界面显示
        viewOptions.setNaviNight(false);//设置导航界面是否显示黑夜模式
        viewOptions.setTrafficInfoUpdateEnabled(true);//设置交通播报是否打开
        viewOptions.setScreenAlwaysBright(true);//设置导航状态下屏幕是否一直开启。
        viewOptions.setTrafficBarEnabled(false);  //设置 返回路况光柱条是否显示（只适用于驾车导航，需要联网）
        // viewOptions.setLayoutVisible(false);  //设置导航界面UI是否显示
        //viewOptions.setNaviViewTopic(mThemeStle);//设置导航界面的主题
        //viewOptions.setZoom(16);
//        viewOptions.setTilt(0);  //2D显示
        mAMapNaviView.setViewOptions(viewOptions);
    }

    /**
     * 获取定位坐标
     */
    public void initLocation() {
        if (mlocationClient != null) {
            return;
        }
        mlocationClient = new AMapLocationClient(getActivity());
        //初始化定位参数
        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
        //设置定位监听
        mlocationClient.setLocationListener(aMapLocationListener);
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(true);
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。
        //如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会。
        mLocationOption.setOnceLocationLatest(true);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        //设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        //启动定位
    }

    private AMapLocationListener aMapLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    //定位成功回调信息，设置相关消息
                    startList.add(new NaviLatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude()));
                    initNavi();
                    aMapNaviListener.onInitNaviSuccess();
                } else {
                    //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                    LogUtil.e("AmapError", "location Error, ErrCode:" + aMapLocation.getErrorCode() +
                            ", errInfo:" + aMapLocation.getErrorInfo());
                    ToastUtil.showToast("定位失败，请重新导航");
                }
            }
        }
    };

    private AMapNaviViewListener aMapNaviViewListener = new AMapNaviViewListener() {
        @Override
        public void onNaviSetting() {

        }

        @Override
        public void onNaviCancel() {
            LogUtil.d(TAG, "导航结束");
            showMap();
        }

        @Override
        public boolean onNaviBackClick() {
            return false;
        }

        @Override
        public void onNaviMapMode(int i) {

        }

        @Override
        public void onNaviTurnClick() {

        }

        @Override
        public void onNextRoadClick() {

        }

        @Override
        public void onScanViewButtonClick() {

        }

        @Override
        public void onLockMap(boolean b) {

        }

        @Override
        public void onNaviViewLoaded() {

        }

        @Override
        public void onMapTypeChanged(int i) {

        }

        @Override
        public void onNaviViewShowMode(int i) {

        }
    };

    private AMapNaviListener aMapNaviListener = new AMapNaviListener() {
        @Override
        public void onInitNaviFailure() {
            LogUtil.e(TAG, "导航创建失败");
            ToastUtil.showToast("导航创建失败");
        }

        @Override
        public void onInitNaviSuccess() {
            LogUtil.e(TAG, "导航创建成功");

            /**
             * 方法: int strategy=mAMapNavi.strategyConvert(congestion,
             * avoidhightspeed, cost, hightspeed, multipleroute); 参数:
             *
             * @congestion 躲避拥堵
             * @avoidhightspeed 不走高速
             * @cost 避免收费
             * @hightspeed 高速优先
             * @multipleroute 多路径
             *
             *  说明:
             *      以上参数都是boolean类型，其中multipleroute参数表示是否多条路线，如果为true则此策略会算出多条路线。
             *      注意: 不走高速与高速优先不能同时为true 高速优先与避免收费不能同时为true
             */
            int strategy = 0;
            try {
                strategy = mAMapNavi.strategyConvert(true, false, false, true, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (naviWay == WAY_WALK) {
                mAMapNavi.calculateWalkRoute(startList.get(0), endList.get(0)); // 步行导航
            } else if (naviWay == WAY_RIDE) {
                mAMapNavi.calculateRideRoute(startList.get(0), endList.get(0));// 骑车导航
            } else if (naviWay == WAY_DRIVE) {
                mAMapNavi.calculateDriveRoute(startList, endList, null, strategy);// 驾车导航
            }
        }

        /**
         * 开始导航回调
         * i - 导航类型，1 ： 实时导航，2 ：模拟导航
         * @param i
         */
        @Override
        public void onStartNavi(int i) {
            LogUtil.d(TAG, "启动导航后回调函数=" + i);
        }

        @Override
        public void onTrafficStatusUpdate() {

        }

        @Override
        public void onLocationChange(AMapNaviLocation aMapNaviLocation) {
            LogUtil.d(TAG, "accuracy = " + aMapNaviLocation.getAccuracy() + " beering = " + aMapNaviLocation
                    .getBearing());
        }

        @Override
        public void onGetNavigationText(int i, String s) {

        }

        @Override
        public void onGetNavigationText(String s) {

        }

        @Override
        public void onEndEmulatorNavi() {

        }

        @Override
        public void onArriveDestination() {
            LogUtil.e(TAG, "到达目的地");
        }

        @Override
        public void onCalculateRouteFailure(int i) {
            ToastUtil.showToast("路径规划失败=" + i + ",失败原因查看官方错误码对照表");
            LogUtil.e(TAG, "路径规划失败=" + i);
        }

        @Override
        public void onReCalculateRouteForYaw() {

        }

        @Override
        public void onReCalculateRouteForTrafficJam() {

        }

        @Override
        public void onArrivedWayPoint(int i) {

        }

        @Override
        public void onGpsOpenStatus(boolean b) {
            if (!b) {
                ToastUtil.showToast("请打开GPS开关");
            }
        }

        @Override
        public void onNaviInfoUpdate(NaviInfo naviInfo) {
            LogUtil.d(TAG, "Navi latitude = " + naviInfo.m_Latitude + " longitude = " + naviInfo.m_Longitude);
//            ToastUtil.showToast(;"latitude = " + naviInfo.m_Latitude + " longitude = " + naviInfo.m_Longitude);
        }

        @Override
        public void onNaviInfoUpdated(AMapNaviInfo aMapNaviInfo) {

        }

        @Override
        public void updateCameraInfo(AMapNaviCameraInfo[] aMapNaviCameraInfos) {

        }

        @Override
        public void updateIntervalCameraInfo(AMapNaviCameraInfo aMapNaviCameraInfo, AMapNaviCameraInfo
                aMapNaviCameraInfo1, int i) {

        }

        @Override
        public void onServiceAreaUpdate(AMapServiceAreaInfo[] aMapServiceAreaInfos) {

        }

        @Override
        public void showCross(AMapNaviCross aMapNaviCross) {

        }

        @Override
        public void hideCross() {

        }

        @Override
        public void showModeCross(AMapModelCross aMapModelCross) {

        }

        @Override
        public void hideModeCross() {

        }

        @Override
        public void showLaneInfo(AMapLaneInfo[] aMapLaneInfos, byte[] bytes, byte[] bytes1) {

        }

        @Override
        public void showLaneInfo(AMapLaneInfo aMapLaneInfo) {

        }

        @Override
        public void hideLaneInfo() {

        }

        @Override
        public void onCalculateRouteSuccess(int[] ints) {
            LogUtil.d(TAG, "路径规划完毕，开始导航");
            showNavi();
            mAMapNavi.startNavi(NaviType.GPS);
        }

        @Override
        public void notifyParallelRoad(int i) {

        }

        @Override
        public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo aMapNaviTrafficFacilityInfo) {

        }

        @Override
        public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] aMapNaviTrafficFacilityInfos) {

        }

        @Override
        public void OnUpdateTrafficFacility(TrafficFacilityInfo trafficFacilityInfo) {

        }

        @Override
        public void updateAimlessModeStatistics(AimLessModeStat aimLessModeStat) {

        }

        @Override
        public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo aimLessModeCongestionInfo) {

        }

        @Override
        public void onPlayRing(int i) {

        }

        @Override
        public void onCalculateRouteSuccess(AMapCalcRouteResult aMapCalcRouteResult) {

        }

        @Override
        public void onCalculateRouteFailure(AMapCalcRouteResult aMapCalcRouteResult) {

        }

        @Override
        public void onNaviRouteNotify(AMapNaviRouteNotifyData aMapNaviRouteNotifyData) {

        }
    };
}
