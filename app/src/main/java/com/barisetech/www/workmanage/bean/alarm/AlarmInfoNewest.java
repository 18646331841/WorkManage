package com.barisetech.www.workmanage.bean.alarm;

/**
 * Created by LJH on 2018/8/10.
 */
public class AlarmInfoNewest {
    /**
     * Result : {"Key":8,"DisplayId":0,"PipeId":0,"Latitude":0,"Longitude":0,"Distance":0,
     * "TimeStamp":"2018-12-11T03:11:11","Type":0,"TimeDiff":0,"Lifted":true,"Company":"BariseTech","LeakId":0,
     * "SiteName1":null,"Distance1":0,"SiteName2":null,"Distance2":0,"PipeName":"测试管线","LiftUser":null,
     * "WarningMessage":"0","Remark":null}
     * Id : 330
     * Exception : null
     * Status : 5
     * IsCanceled : false
     * IsCompleted : true
     * CreationOptions : 0
     * AsyncState : null
     * IsFaulted : false
     */

    private AlarmInfo Result;
    private int Id;
    private Object Exception;
    private int Status;
    private boolean IsCanceled;
    private boolean IsCompleted;
    private int CreationOptions;
    private Object AsyncState;
    private boolean IsFaulted;

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public Object getException() {
        return Exception;
    }

    public void setException(Object Exception) {
        this.Exception = Exception;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int Status) {
        this.Status = Status;
    }

    public boolean isIsCanceled() {
        return IsCanceled;
    }

    public void setIsCanceled(boolean IsCanceled) {
        this.IsCanceled = IsCanceled;
    }

    public boolean isIsCompleted() {
        return IsCompleted;
    }

    public void setIsCompleted(boolean IsCompleted) {
        this.IsCompleted = IsCompleted;
    }

    public int getCreationOptions() {
        return CreationOptions;
    }

    public void setCreationOptions(int CreationOptions) {
        this.CreationOptions = CreationOptions;
    }

    public Object getAsyncState() {
        return AsyncState;
    }

    public void setAsyncState(Object AsyncState) {
        this.AsyncState = AsyncState;
    }

    public boolean isIsFaulted() {
        return IsFaulted;
    }

    public void setIsFaulted(boolean IsFaulted) {
        this.IsFaulted = IsFaulted;
    }

    public AlarmInfo getResult() {
        return Result;
    }

    public void setResult(AlarmInfo result) {
        Result = result;
    }
}
