package com.hzdq.bajiesleepchildrenHD.user.fragment;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.hzdq.bajiesleepchildrenHD.R;
import com.hzdq.bajiesleepchildrenHD.user.viewmodel.UserViewModel;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class ChartFragment extends Fragment {

    public ChartFragment(List<Float> chartList ) {
        this.chartList = chartList;


    }

    List<Float> chartList ;
    private BarChart barChart;
    List<BarEntry> list = new ArrayList<>();//实例化一个List用来存储数据

    private UserViewModel userViewModel;
    private LifecycleOwner lifecycleOwner;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_chart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userViewModel = new ViewModelProvider(this, new SavedStateViewModelFactory(requireActivity().getApplication(),this)).get(UserViewModel.class);

        barChart = view.findViewById(R.id.bar_chart);

        Log.d("chartList", "onViewCreated: "+chartList);


        if (chartList!=null){
            for (int i = 0; i < chartList.size(); i++) {
                list.add(new BarEntry(i+1,chartList.get(i)));
            }
        }














        BarDataSet barDataSet = new BarDataSet(list, "");

        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.5f);
        barChart.setData(barData);

        barChart.getDescription().setEnabled(false);//隐藏右下角英文
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);//X轴的位置 默认为上面
        barChart.getAxisRight().setEnabled(false);//隐藏右侧Y轴   默认是左右两侧都有Y轴

        barChart.getXAxis().setDrawGridLines(false);  //是否绘制X轴上的网格线（背景里面的竖线）
        barChart.getAxisLeft().setDrawGridLines(false);  //是否绘制Y轴上的网格线（背景里面的横线）

        Legend legend=barChart.getLegend();
        legend.setEnabled(false);    //是否显示图例



        setXXX();
        setYYY();
        setBar(barDataSet);

    }



    /**
     * x轴
     */
    private void setXXX(){
        //X轴
        XAxis xAxis=barChart.getXAxis();
        xAxis.setDrawGridLines(false);  //是否绘制X轴上的网格线（背景里面的竖线）
        xAxis.setAxisLineColor(Color.parseColor("#E8E8E8"));   //X轴颜色
        xAxis.setAxisLineWidth(2);           //X轴粗细
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);        //X轴所在位置   默认为上面
        xAxis.setValueFormatter(new IndexAxisValueFormatter(){
            @Override
            public String getFormattedValue(float v) {
                for (int i = 1; i <= chartList.size(); i++) {
                    if (v == i){
                        return String.valueOf(i);
                    }
                }
                return "";//注意这里需要改成 ""
            }
        });
        if (chartList != null){
            xAxis.setAxisMaximum(chartList.size()+1);   //X轴最大数值
        }

        xAxis.setAxisMinimum(0);   //X轴最小数值
        //X轴坐标的个数    第二个参数一般填false     true表示强制设置标签数 可能会导致X轴坐标显示不全等问题
        if (chartList != null){
            xAxis.setLabelCount(chartList.size()+1,false);
        }


    }

    /**
     * y轴
     */
    private void setYYY(){
        //Y轴
        YAxis AxisLeft=barChart.getAxisLeft();
        AxisLeft.setDrawGridLines(false);  //是否绘制Y轴上的网格线（背景里面的横线）
        AxisLeft.setAxisLineColor(Color.parseColor("#E8E8E8"));  //Y轴颜色
        AxisLeft.setAxisLineWidth(2);           //Y轴粗细
        AxisLeft.setValueFormatter(new IndexAxisValueFormatter(){
            @Override
            public String getFormattedValue(float v) {
                for (int a=0;a<100;a++){     //用个for循环方便
                    if (a==v){
                        return String.valueOf(a);
                    }
                }

                return "";
            }
        });
        AxisLeft.setAxisMaximum(50);   //Y轴最大数值
        AxisLeft.setAxisMinimum(0);   //Y轴最小数值
        //Y轴坐标的个数    第二个参数一般填false     true表示强制设置标签数 可能会导致X轴坐标显示不全等问题
        if (chartList != null){
            AxisLeft.setLabelCount(chartList.size(),false);
        }


        //是否隐藏右边的Y轴（不设置的话有两条Y轴 同理可以隐藏左边的Y轴）
        barChart.getAxisRight().setEnabled(false);



    }


    /**
     * 柱子
     * @param barDataSet
     */
    private void setBar(BarDataSet barDataSet){
        //柱子
//        barDataSet.setColor(Color.BLACK);  //柱子的颜色
        barDataSet.setColors(Color.parseColor("#2051BD"));//设置柱子多种颜色  循环使用

//        barDataSet.setBarBorderColor(Color.CYAN);//柱子边框颜色
//        barDataSet.setBarBorderWidth(0);       //柱子边框厚度
//        barDataSet.setBarShadowColor(Color.RED);
        barDataSet.setHighlightEnabled(false);//选中柱子是否高亮显示  默认为true
//        barDataSet.setStackLabels(new String[]{"aaa","bbb","ccc"});
        //定义柱子上的数据显示    可以实现加单位    以及显示整数（默认是显示小数）
//        barDataSet.setValueFormatter(new IValueFormatter(){
//
//            @Override
//            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
//                return null;
//            }
//        });

//        barDataSet.setValueFormatter(new MyValueFormatter());
        //数据更新
        barChart.notifyDataSetChanged();
        barChart.invalidate();

        //动画（如果使用了动画可以则省去更新数据的那一步）
        barChart.animateY(500); //在Y轴的动画  参数是动画执行时间 毫秒为单位
//        line.animateX(2000); //X轴动画
//        line.animateXY(2000,2000);//XY两轴混合动画

    }

//    class  MyValueFormatter extends ValueFormatter implements IValueFormatter {
//
//        @Override
//        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
//
//            return "1";
//        }
//    }
}