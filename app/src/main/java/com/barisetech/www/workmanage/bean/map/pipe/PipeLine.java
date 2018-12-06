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

    /**
     * 警报标记
     */
    public Marker alarmMarker;

    public PipeLine(String pipeId, LineStation lineStation) {
        this.pipeId = pipeId;
        this.lineStation = lineStation;
    }

    public void show(boolean isShow) {
        if (startSiteMarker != null) {
            startSiteMarker.setVisible(isShow);
            endSiteMarker.setVisible(isShow);
        }
        if (endSiteMarker != null) {
            startSiteText.setVisible(isShow);
            endSiteText.setVisible(isShow);
        }
        if (polyline != null) {
            polyline.setVisible(isShow);
        }
        if (alarmMarker != null) {
            alarmMarker.setVisible(isShow);
        }
    }
}
