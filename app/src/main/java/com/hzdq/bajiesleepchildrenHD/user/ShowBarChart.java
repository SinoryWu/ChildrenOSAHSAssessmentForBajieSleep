package com.hzdq.bajiesleepchildrenHD.user;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

public class ShowBarChart {
    List<BarEntry> list=new ArrayList<>();

    public void show(BarChart bar){
        //添加数据
        list.add(new BarEntry(1,3));
        list.add(new BarEntry(2,8));
        list.add(new BarEntry(3,6));
        list.add(new BarEntry(4,9));


        BarDataSet barDataSet=new BarDataSet(list,"语文");
        BarData barData=new BarData(barDataSet);
        bar.setData(barData);

        bar.getDescription().setEnabled(false);//隐藏右下角英文
        bar.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);//X轴的位置 默认为上面
        bar.getAxisRight().setEnabled(false);//隐藏右侧Y轴   默认是左右两侧都有Y轴

    }

}
