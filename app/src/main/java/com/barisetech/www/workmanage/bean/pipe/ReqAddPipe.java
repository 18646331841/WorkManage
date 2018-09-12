package com.barisetech.www.workmanage.bean.pipe;

import java.util.List;

/**
 * Created by LJH on 2018/8/28.
 */
public class ReqAddPipe {
    /**
     * PipeInfo : [{"PipeId":"2","Name":"dsasda","SortID":"2","PipeCollectID":[{"Id":"1"}],"Length":"1",
     * "PipeMaterial":"df","Company":"barisetech","StartSiteId":"1","Sites":[{"siteId":"0","name":"22",
     * "longitude":"22","latitude":"22","company":"","isAlive":"true","EnergyRatio":"11","IsDwtEnabled":"true",
     * "IsAvgEnabled":"false","NegPeakThreshold":"1","FreqStart":"1","FreqEnd":"1","FreqStep":"1","TimeNum":"1",
     * "Telephone":"1237354534","Manager":"oweir","IsDualSensor":"false","IsEnabled1":"true","IirType1":"1",
     * "IirBandtype1":"1","IirN1":"1","IirFc1":"1","IirF01":"1","IirAp1":"1","IirAs1":"1","IsEnabled2":"false",
     * "IirType2":"1","IirBandtype2":"1","IirN2":"1","IirFc2":"1","IirF02":"1","IirAp2":"1","IirAs2":"1"},
     * {"siteId":"1","name":"22","longitude":"22","latitude":"22","company":"","isAlive":"true","EnergyRatio":"11",
     * "IsDwtEnabled":"true","IsAvgEnabled":"false","NegPeakThreshold":"1","FreqStart":"1","FreqEnd":"1",
     * "FreqStep":"1","TimeNum":"1","Telephone":"1237354534","Manager":"oweir","IsDualSensor":"false",
     * "IsEnabled1":"true","IirType1":"1","IirBandtype1":"1","IirN1":"1","IirFc1":"1","IirF01":"1","IirAp1":"1",
     * "IirAs1":"1","IsEnabled2":"false","IirType2":"1","IirBandtype2":"1","IirN2":"1","IirFc2":"1","IirF02":"1",
     * "IirAp2":"1","IirAs2":"1"}],"Speed":"10","LeakCheckGap":"5","Algorithm":"false","IsTestMode":"true",
     * "BallChokLocation":"true","LeakageAssessment":"false","LdPluginName":"Default",
     * "LdPluginId":"c0f607c5-fe14-4655-a125-c8fcb5dc1f4c","OriginSlavePressureDifferenceLowLimit":"0",
     * "OriginSlavePressureDifferenceHighLimit":"0","OriginSlavePressureDifferenceAmend":"0",
     * "OriginSlaveFlowCumulativeTime":"0","OriginSlaveFlowThresholdLowLimit":"0",
     * "OriginSlaveFlowThresholdHighLimit":"0","OriginSlaveFlowThresholdAmend":"1"}]
     * Operation : 0
     */

    private String Operation;
    private List<ReqPipeInfo> PipeInfo;

    public String getOperation() {
        return Operation;
    }

    public void setOperation(String operation) {
        Operation = operation;
    }

    public List<ReqPipeInfo> getPipeInfo() {
        return PipeInfo;
    }

    public void setPipeInfo(List<ReqPipeInfo> pipeInfo) {
        PipeInfo = pipeInfo;
    }
}
