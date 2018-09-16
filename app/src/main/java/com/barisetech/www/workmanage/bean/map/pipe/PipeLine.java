package com.barisetech.www.workmanage.bean.map.pipe;

import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.Text;
import com.barisetech.www.workmanage.bean.map.LineStation;
import com.barisetech.www.workmanage.bean.site.SiteBean;

/**
 * Created by LJH on 2018/9/13.
 */
public class PipeLine {

    public String pipeId;

    public String pipeName;

    /**
     * 首站
     */
    public SiteBean startSite;

    /**
     * 末站
     */
    public SiteBean endSite;

    public LineStation lineStation;

    /**
     * 地图管线
     */
    public Polyline polyline;

    /**
     * 首站站点标记
     */
    public Marker startSiteMarker;

    /**
     * 首站站点文字
     */
    public Text startSiteText;

    /**
     * 末站站点文字
     */
    public Text endSiteText;

    /**
     * 末站站点标记
     */
    public Marker endSiteMarker;

    public PipeLine(String pipeId, LineStation lineStation) {
        this.pipeId = pipeId;
        this.lineStation = lineStation;
    }

    public void show(boolean isShow) {
        startSiteMarker.setVisible(isShow);
        endSiteMarker.setVisible(isShow);
        startSiteText.setVisible(isShow);
        endSiteText.setVisible(isShow);
        polyline.setVisible(isShow);
    }
}
