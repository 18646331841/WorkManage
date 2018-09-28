package com.barisetech.www.workmanage.utils;

import android.graphics.Color;
import android.util.Log;

import com.barisetech.www.workmanage.bean.DataRateBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

public class ChartUtil {
    private static final String TAG = "ChartUtil";
    private static final String[] colors = new String[]{"#FEB04C", "#008aff"};

    public static void setChartViewData(List<DataRateBean> heartList, LineChartView lineChart) {

        //X轴数据点  各个属性介绍地址：http://www.jianshu.com/p/7e8de03dad79
        List<PointValue> mPointValues = new ArrayList<>();
        //底部时间
        List<AxisValue> axisXBottomValues = new ArrayList<>();
        //获取list中最大最小值 （纯属打印、非必须）
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < heartList.size(); i++) {
            //数据点
            mPointValues.add(new PointValue(i, heartList.get(i).getData()));
            //底部数据点
            axisXBottomValues.add(new AxisValue(i).setLabel(heartList.get(i).getTime()));

            //将所有点添加至新list中
            list.add((int) heartList.get(i).getData());
        }
        //数据大小
        int dataSize = heartList.size();
        //最大的点
        int maxPoint = Collections.max(list);
        //最小的点
        int minPoint = Collections.min(list);

        Log.e(TAG, "最大的点：" + maxPoint);
        Log.e(TAG, "最小的点：" + minPoint);
        Log.e(TAG, "数据点：" + dataSize);

        Line line = new Line(mPointValues);
        //存放线条的集合
        List<Line> lines = new ArrayList<>();
        //LineChartValueFormatter chartValueFormatter = new SimpleLineChartValueFormatter(2);
        //line.setFormatter(chartValueFormatter);//显示小数点（后2位）
        line.setHasLabels(false);//曲线的数据坐标是否加上备注
        line.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状 这里是圆形 （
        //有三种 ：ValueShape.SQUARE(方形)  ValueShape.CIRCLE(圆形)  ValueShape.DIAMOND (菱形)）
        line.setPointRadius(3);//坐标点大小
        //line.setSquare(true);//是否以直角的形式显示线
        line.setCubic(true);//曲线是否平滑
        line.setFilled(false);//是否填充曲线的面积
        line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        line.setHasLines(true);//是否用直线显示。如果为false 则没有曲线只有点显示
        line.setHasPoints(false);//是否显示圆点 如果为false 则没有原点只有点显示
        line.setColor(Color.parseColor("#FEB04C"));
        line.setStrokeWidth(1);//设置线的宽度
        lines.add(line);

        LineChartData data = new LineChartData();
        data.setLines(lines);
        //设置数据的初始值，即所有的数据从baseValue开始计算，默认值为0。
        data.setBaseValue(Float.NEGATIVE_INFINITY);

        //传入底部list数据
        Axis axisX = new Axis();
        //设置底部标题(自行选择) 只能设置在正中间
        //axisX.setName("时间(h)");
        //底部标注是否斜着显示
        //axisX.setHasTiltedLabels(true);
        //字体大小
        axisX.setTextSize(12);
        //字体颜色
        axisX.setTextColor(Color.parseColor("#666666"));
        //距离各标签之间的距离 (0-32之间)
        //axisX.setMaxLabelChars(4);
        //是否显示坐标线、如果为false 则没有曲线只有点显示
        axisX.setHasLines(false);
        axisX.setValues(axisXBottomValues);
        data.setAxisXBottom(axisX);

        //左边参数设置
        Axis axisY = new Axis();
        //axisY.setMaxLabelChars(6); //最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数
        axisY.setTextSize(12);
        axisY.setTextColor(Color.parseColor("#666666"));
        axisY.setHasLines(false);
        //axisY.setValues(axisXLeftValues);
        //设置坐标轴在左边
        data.setAxisYLeft(axisY);

        //设置行为属性，支持缩放、滑动以及平移
        lineChart.setInteractive(true);
        //水平缩放
        //lineChart.setZoomType(ZoomType.HORIZONTAL);
        //是否可滑动
        lineChart.setScrollEnabled(true);
        //放入数据源至控件中
        lineChart.setLineChartData(data);

        //设置最大缩放比例。默认值20
        //lineChart.setMaxZoom(10);
        //将视图窗口（viewport）移动至指定位置。如果可以移动，viewport将以该点为视图的中心。
        //lineChart.moveTo(5, 5);
        //以坐标（x,y）为中心，自动缩放表格。注意，该方法应在设置完chartview的数据（chartdata）后再调用。
        //lineChart.setZoomLevel(5,5,5);
        //设置是否允许点击图标上的值，默认为true。
        //lineChart.setValueTouchEnabled(true);
        //设置是否可以选中图表中的值，即当点击图表中的数据值后，会一直处于选中状态，直到用户点击其他空间
        //lineChart.setValueSelectionEnabled(true);
        //选中一个数据值
        //lineChart.selectValue();
        //设置是否允许图表在父容器中滑动。
        //lineChart.setContainerScrollEnabled();
        //开始以动画的形式更新图表数据
//        lineChart.startDataAnimation();
        //动画时长
//        lineChart.startDataAnimation(500);
        //取消动画
//        lineChart.cancelDataAnimation();

        //图表触摸事件
//        lineChart.setOnValueTouchListener(new LineChartOnValueSelectListener() {
//            @Override
//            public void onValueSelected(int i, int i1, PointValue pointValue) {
//                LogUtils.e("onValueSelected：" + i + "\n" + i1);
//                LogUtils.e("X轴：" + pointValue.getX());
//                LogUtils.e("Y轴：" + pointValue.getX());
//            }
//            @Override
//            public void onValueDeselected() {
//            }
//        });

        //viewport必须设置在setLineChartData后面，设置一个当前viewport，再设置一个maxviewport，
        //就可以实现滚动，高度要设置数据的上下限
        //设置是否允许在动画进行中或设置完表格数据后，自动计算viewport的大小。如果禁止，则需要可以手动设置。
        lineChart.setViewportCalculationEnabled(true);
        Viewport v = new Viewport(lineChart.getMaximumViewport());
        //Y轴最大值为 加上20、防止显示不全
//        v.top = maxPoint + 20;
        //最小值 Y轴最低点就为0
//        v.bottom = minPoint + 20;//最小值
        //设置最大化的viewport （chartdata）后再调用
        //这2个属性的设置一定要在lineChart.setMaximumViewport(v);这个方法之后,
        // 不然显示的坐标数据是不能左右滑动查看更多数据的
        lineChart.setMaximumViewport(v);
        //左边起始位置 轴
        v.left = 0;
        //如果数据点超过20，显示20个、否则，显示数据本身大小{自己根据需求设置}
        if (dataSize > 20) {
            // Y轴显示多少数据
            v.right = 20;
        } else {
            v.right = dataSize;
        }
        //左右滑动
        lineChart.setCurrentViewport(v);
    }

    public static void setChartViewData(Map<String, List<DataRateBean>> mapData, LineChartView lineChart) {

        if (mapData == null || mapData.size() <= 0) {
            return;
        }

        //存放线条的集合
        List<Line> lines = new ArrayList<>();
        //底部时间
        List<AxisValue> axisXBottomValues = new ArrayList<>();
        int dataSize = 0;

        for (Map.Entry<String, List<DataRateBean>> entry : mapData.entrySet()) {
            List<DataRateBean> dataRateBeanList = entry.getValue();
            if (dataRateBeanList != null && dataRateBeanList.size() > 0) {
                if (dataSize < dataRateBeanList.size()) {
                    dataSize = dataRateBeanList.size();
                    axisXBottomValues.clear();
                }
                List<PointValue> mPointValues = new ArrayList<>();
                for (int i = 0; i < dataRateBeanList.size(); i++) {
                    DataRateBean dataRateBean = dataRateBeanList.get(i);
                    //数据点
                    mPointValues.add(new PointValue(i, dataRateBean.getData()));
                    if (axisXBottomValues.size() < dataSize) {
                        if (i % 500 == 0) {
                            axisXBottomValues.add(new AxisValue(i).setLabel(dataRateBean.getTime()));
                        }
                    }
                }
                Line line = new Line(mPointValues);
                LogUtil.d(TAG, "wave key = " + entry.getKey());
                //LineChartValueFormatter chartValueFormatter = new SimpleLineChartValueFormatter(2);
                //line.setFormatter(chartValueFormatter);//显示小数点（后2位）
                line.setHasLabels(false);//曲线的数据坐标是否加上备注
                line.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状 这里是圆形 （
                //有三种 ：ValueShape.SQUARE(方形)  ValueShape.CIRCLE(圆形)  ValueShape.DIAMOND (菱形)）
                line.setPointRadius(3);//坐标点大小
                //line.setSquare(true);//是否以直角的形式显示线
                line.setCubic(true);//曲线是否平滑
                line.setFilled(false);//是否填充曲线的面积
                line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
                line.setHasLines(true);//是否用直线显示。如果为false 则没有曲线只有点显示
                line.setHasPoints(false);//是否显示圆点 如果为false 则没有原点只有点显示
                line.setColor(Color.parseColor(colors[0]));
                String[] keys = entry.getKey().split("_");
                if (keys.length == 2) {
                    if (keys[1].equals("2")) {
                        line.setColor(Color.parseColor(colors[1]));
                    }
                }
                line.setStrokeWidth(1);//设置线的宽度

                lines.add(line);
            }
        }

        if (lines.size() <= 0) {
            return;
        }

        LogUtil.d(TAG, "lines size = " + lines.size() + " axis size = " + axisXBottomValues.size());

        LineChartData data = new LineChartData();
        data.setLines(lines);
        //设置数据的初始值，即所有的数据从baseValue开始计算，默认值为0。
        data.setBaseValue(Float.NEGATIVE_INFINITY);

        //传入底部list数据
        Axis axisX = new Axis();
        //设置底部标题(自行选择) 只能设置在正中间
        //axisX.setName("时间(h)");
        //底部标注是否斜着显示
        //axisX.setHasTiltedLabels(true);
        //字体大小
        axisX.setTextSize(12);
        //字体颜色
        axisX.setTextColor(Color.parseColor("#666666"));
        //距离各标签之间的距离 (0-32之间)
        //axisX.setMaxLabelChars(4);
        //是否显示坐标线、如果为false 则没有曲线只有点显示
        axisX.setHasLines(false);
        axisX.setValues(axisXBottomValues);
        data.setAxisXBottom(axisX);

        //左边参数设置
        Axis axisY = new Axis();
        axisY.setMaxLabelChars(6); //最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数
        axisY.setTextSize(12);
        axisY.setTextColor(Color.parseColor("#666666"));
        axisY.setHasLines(false);
        //axisY.setValues(axisXLeftValues);
        //设置坐标轴在左边
        data.setAxisYLeft(axisY);

        //设置行为属性，支持缩放、滑动以及平移
        lineChart.setInteractive(true);
        //水平缩放
        lineChart.setZoomType(ZoomType.HORIZONTAL);
        //是否可滑动
        lineChart.setScrollEnabled(true);
        //放入数据源至控件中
        lineChart.setLineChartData(data);
        //设置最大缩放比例。默认值20
        //lineChart.setMaxZoom(10);
        //将视图窗口（viewport）移动至指定位置。如果可以移动，viewport将以该点为视图的中心。
        //lineChart.moveTo(5, 5);
        //以坐标（x,y）为中心，自动缩放表格。注意，该方法应在设置完chartview的数据（chartdata）后再调用。
        //lineChart.setZoomLevel(5,5,5);
        //设置是否允许点击图标上的值，默认为true。
        //lineChart.setValueTouchEnabled(true);
        //设置是否可以选中图表中的值，即当点击图表中的数据值后，会一直处于选中状态，直到用户点击其他空间
        //lineChart.setValueSelectionEnabled(true);
        //选中一个数据值
        //lineChart.selectValue();
        //设置是否允许图表在父容器中滑动。
        //lineChart.setContainerScrollEnabled();
        //开始以动画的形式更新图表数据
//        lineChart.startDataAnimation();
        //动画时长
//        lineChart.startDataAnimation(500);
        //取消动画
//        lineChart.cancelDataAnimation();

        //图表触摸事件
//        lineChart.setOnValueTouchListener(new LineChartOnValueSelectListener() {
//            @Override
//            public void onValueSelected(int i, int i1, PointValue pointValue) {
//                LogUtils.e("onValueSelected：" + i + "\n" + i1);
//                LogUtils.e("X轴：" + pointValue.getX());
//                LogUtils.e("Y轴：" + pointValue.getX());
//            }
//            @Override
//            public void onValueDeselected() {
//            }
//        });

        //viewport必须设置在setLineChartData后面，设置一个当前viewport，再设置一个maxviewport，
        //就可以实现滚动，高度要设置数据的上下限
        //设置是否允许在动画进行中或设置完表格数据后，自动计算viewport的大小。如果禁止，则需要可以手动设置。
        lineChart.setViewportCalculationEnabled(true);
        Viewport v = new Viewport(lineChart.getMaximumViewport());
        //Y轴最大值为 加上20、防止显示不全
//        v.top = maxPoint + 20;
        //最小值 Y轴最低点就为0
//        v.bottom = minPoint + 20;//最小值
        //设置最大化的viewport （chartdata）后再调用
        //这2个属性的设置一定要在lineChart.setMaximumViewport(v);这个方法之后,
        // 不然显示的坐标数据是不能左右滑动查看更多数据的
        lineChart.setMaximumViewport(v);
        //左边起始位置 轴
        v.left = 0;
        //如果数据点超过20，显示20个、否则，显示数据本身大小{自己根据需求设置}
        if (dataSize > 10) {
            // Y轴显示多少数据
            v.right = 10;
        } else {
            v.right = dataSize;
        }
        //左右滑动
        lineChart.setCurrentViewport(v);
    }
}
