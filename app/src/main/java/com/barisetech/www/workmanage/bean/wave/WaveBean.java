package com.barisetech.www.workmanage.bean.wave;

import java.util.List;

/**
 * Created by LJH on 2018/9/26.
 */
public class WaveBean {
    /**
     * ActualTimeRange : {"StartTime":"2018-09-04T00:00:00","EndTime":"2018-09-04T00:00:29.999"}
     * ExpectTimeRange : {"StartTime":"2018-09-04T00:00:00","EndTime":"2018-09-04T00:00:30"}
     * Points : [{"TimeStamp":1536019200000,"Data":0},{"TimeStamp":1536019200001,"Data":1},
     * {"TimeStamp":1536019200002,"Data":2}]
     * SampleRate : 1000
     * IsStartMatch : true
     * IsEndMatch : true
     */

    public String siteId;
    public String type;

    public ActualTimeRangeBean ActualTimeRange;
    public ExpectTimeRangeBean ExpectTimeRange;
    public int SampleRate;
    public boolean IsStartMatch;
    public boolean IsEndMatch;
    public List<PointsBean> Points;

    public static class ActualTimeRangeBean {
        /**
         * StartTime : 2018-09-04T00:00:00
         * EndTime : 2018-09-04T00:00:29.999
         */

        public String StartTime;
        public String EndTime;
    }

    public static class ExpectTimeRangeBean {
        /**
         * StartTime : 2018-09-04T00:00:00
         * EndTime : 2018-09-04T00:00:30
         */

        public String StartTime;
        public String EndTime;
    }

    public static class PointsBean {
        /**
         * TimeStamp : 1536019200000
         * Data : 0
         */

        public long TimeStamp;
        public int Data;
    }
}
