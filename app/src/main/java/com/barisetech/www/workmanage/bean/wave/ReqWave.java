package com.barisetech.www.workmanage.bean.wave;

/**
 * Created by LJH on 2018/9/26.
 */
public class ReqWave {
    /**
     * MachineCode : 305c5344-b222-4a22-a9f8-3cef587381b0
     * siteId : 0
     * sensorType : 1       {
     *                      SubsonicMain = 1,             //主次声波传感器
     *                      SubsonicSub = 2,              //从次声波传感器
     *                      Pressure = 3,                 //压力传感器
     *                      Flux = 4,                     //流量传感器
     *                      Other = 5                     //其它类型传感器
     *                      }
     * startTime : 2018-09-04 00:00:00
     * endTime : 2018-09-04 00:02:00
     */
    public String MachineCode;
    public String siteId;
    public String sensorType;
    public String startTime;
    public String endTime;
}
