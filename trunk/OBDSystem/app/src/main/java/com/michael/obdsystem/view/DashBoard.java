package com.michael.obdsystem.view;

import java.util.List;
import org.xclcharts.chart.DialChart;
import org.xclcharts.common.MathHelper;
import org.xclcharts.renderer.XEnum;
import org.xclcharts.renderer.plot.PlotAttrInfo;
import org.xclcharts.renderer.plot.Pointer;
import org.xclcharts.view.GraphicalView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;

import android.util.AttributeSet;
import android.util.Log;

/**
 * @ClassName DialChart例子
 * @Description  仪表盘例子
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 *
 */
public class DashBoard extends GraphicalView {

    private String TAG = "DialChart03View";

    private DialChart chart = new DialChart();
    private float mPercentage = 0.9f;
    private float change = 0.0f;
    public DashBoard(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        initView();
    }

    public DashBoard(Context context, AttributeSet attrs){
        super(context, attrs);
        initView();
    }

    public DashBoard(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }


    private void initView()
    {
        chartRender();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        chart.setChartRange((int)(w) ,(int)(h*1.075) );
    }

    public void chartRender()
    {
        try {

            chart.getPointer().setPercentage(mPercentage);
            chart.getPointer().hideBaseCircle();

            //设置指针长度
            //当指针长度无限小的时候 线就不见了
            chart.getPointer().setLength(0.000001f);

            //增加指针
            addPointer();
            //设置附加信息
            addAttrInfo();
            /////////////////////////////////////////////////////////////

        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e(TAG, e.toString());
        }

    }


    //增加指针
    public void addPointer()
    {
        chart.addPointer();

        List<Pointer> mp = chart.getPlotPointer();
        change = (float)(((change*236.5)-7)/236.5);
        mp.get(0).setPercentage( change);

        //设置指针长度
        mp.get(0).setLength(0.75f);
        mp.get(0).getPointerPaint().setColor(Color.WHITE);
        mp.get(0).setPointerStyle(XEnum.PointerStyle.TRIANGLE);
        mp.get(0).hideBaseCircle();

    }


    private void addAttrInfo()
    {
        /////////////////////////////////////////////////////////////
        PlotAttrInfo plotAttrInfo = chart.getPlotAttrInfo();
        //设置附加信息
        Paint paintTB = new Paint();
        paintTB.setColor(Color.WHITE);
        paintTB.setTextAlign(Align.CENTER);
        paintTB.setTextSize(30);
        paintTB.setAntiAlias(true);
        plotAttrInfo.addAttributeInfo(XEnum.Location.TOP, "当前车速", 0.3f, paintTB);

        Paint paintBT = new Paint();
        paintBT.setColor(Color.WHITE);
        paintBT.setTextAlign(Align.CENTER);
        paintBT.setTextSize(35);
        paintBT.setFakeBoldText(true);
        paintBT.setAntiAlias(true);
        plotAttrInfo.addAttributeInfo(XEnum.Location.BOTTOM,
                Float.toString(MathHelper.getInstance().round(mPercentage * 236.5f,2)), 0.6f, paintBT);

        Paint paintBT2 = new Paint();
        paintBT2.setColor(Color.WHITE);
        paintBT2.setTextAlign(Align.CENTER);
        paintBT2.setTextSize(30);
        paintBT2.setFakeBoldText(true);
        paintBT2.setAntiAlias(true);
        plotAttrInfo.addAttributeInfo(XEnum.Location.BOTTOM, "MB/S", 0.7f, paintBT2);
    }

    public void setCurrentStatus(float percentage)
    {
        mPercentage =  percentage;
        chart.clearAll();
        change = percentage;
        //设置当前百分比
//		chart.getPointer().setPercentage(mPercentage);

        //增加指针
        addPointer();
        addAttrInfo();
    }


    @Override
    public void render(Canvas canvas) {
        // TODO Auto-generated method stub
        try{
            chart.render(canvas);
        } catch (Exception e){
            Log.e(TAG, e.toString());
        }
    }
}
