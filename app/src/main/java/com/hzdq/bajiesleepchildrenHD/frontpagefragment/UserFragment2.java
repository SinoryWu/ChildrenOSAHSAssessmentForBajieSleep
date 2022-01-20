package com.hzdq.bajiesleepchildrenHD.frontpagefragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.hzdq.bajiesleepchildrenHD.R;
import com.hzdq.bajiesleepchildrenHD.databinding.FragmentUserBinding;
import com.hzdq.bajiesleepchildrenHD.utils.Shp;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class UserFragment2 extends Fragment {

    private FragmentUserBinding binding;

    private Shp shp;
    private String barPosition;

    private BarChart barChart;
    List<BarEntry> list = new ArrayList<>();//实例化一个List用来存储数据

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        shp = new Shp(requireContext());

        barPosition = shp.getUserBarPosition();


        barChart = view.findViewById(R.id.user_base_info_barChart);
        list.add(new BarEntry(1, 21));
        list.add(new BarEntry(2, 16));
        list.add(new BarEntry(3, 18));
        list.add(new BarEntry(4, 13));
        list.add(new BarEntry(5, 7));

        BarDataSet barDataSet = new BarDataSet(list, "");
        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);

        barChart.getDescription().setEnabled(false);//隐藏右下角英文
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);//X轴的位置 默认为上面
        barChart.getAxisRight().setEnabled(false);//隐藏右侧Y轴   默认是左右两侧都有Y轴

        barChart.getXAxis().setDrawGridLines(false);  //是否绘制X轴上的网格线（背景里面的竖线）
        barChart.getAxisLeft().setDrawGridLines(false);  //是否绘制Y轴上的网格线（背景里面的横线）

        setXXX();
        setYYY();
        setBar(barDataSet);

        binding.userBarInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.userBarInfo.setBackgroundResource(R.drawable.user_bar_button_background_true);
                binding.userBarInfo.setTextColor(Color.parseColor("#FFFFFF"));

                binding.userBarEvaluate.setBackgroundResource(R.drawable.user_bar_button_background);
                binding.userBarEvaluate.setTextColor(Color.parseColor("#2051BD"));
                binding.userBarQuestion.setBackgroundResource(R.drawable.user_bar_button_background);
                binding.userBarQuestion.setTextColor(Color.parseColor("#2051BD"));
                binding.userBarRecord.setBackgroundResource(R.drawable.user_bar_button_background);
                binding.userBarRecord.setTextColor(Color.parseColor("#2051BD"));
                binding.userBarReport.setBackgroundResource(R.drawable.user_bar_button_background);
                binding.userBarReport.setTextColor(Color.parseColor("#2051BD"));

                binding.userCardInfo.setVisibility(View.VISIBLE);
                binding.userCardEvaluate.setVisibility(View.GONE);
                binding.userCardQuestion.setVisibility(View.GONE);
                binding.userCardRecord.setVisibility(View.GONE);
                binding.userCardReport.setVisibility(View.GONE);

                shp.saveToSp("userbarpostion", "1");
            }
        });

        binding.userBarEvaluate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.userBarInfo.setBackgroundResource(R.drawable.user_bar_button_background);
                binding.userBarInfo.setTextColor(Color.parseColor("#2051BD"));
                binding.userBarEvaluate.setBackgroundResource(R.drawable.user_bar_button_background_true);
                binding.userBarEvaluate.setTextColor(Color.parseColor("#FFFFFF"));

                binding.userBarQuestion.setBackgroundResource(R.drawable.user_bar_button_background);
                binding.userBarQuestion.setTextColor(Color.parseColor("#2051BD"));
                binding.userBarRecord.setBackgroundResource(R.drawable.user_bar_button_background);
                binding.userBarRecord.setTextColor(Color.parseColor("#2051BD"));
                binding.userBarReport.setBackgroundResource(R.drawable.user_bar_button_background);
                binding.userBarReport.setTextColor(Color.parseColor("#2051BD"));

                binding.userCardInfo.setVisibility(View.GONE);
                binding.userCardEvaluate.setVisibility(View.VISIBLE);
                binding.userCardQuestion.setVisibility(View.GONE);
                binding.userCardRecord.setVisibility(View.GONE);
                binding.userCardReport.setVisibility(View.GONE);

                shp.saveToSp("userbarpostion", "4");
            }
        });

        binding.userBarQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.userBarInfo.setBackgroundResource(R.drawable.user_bar_button_background);
                binding.userBarInfo.setTextColor(Color.parseColor("#2051BD"));
                binding.userBarEvaluate.setBackgroundResource(R.drawable.user_bar_button_background);
                binding.userBarEvaluate.setTextColor(Color.parseColor("#2051BD"));
                binding.userBarQuestion.setBackgroundResource(R.drawable.user_bar_button_background_true);
                binding.userBarQuestion.setTextColor(Color.parseColor("#FFFFFF"));

                binding.userBarRecord.setBackgroundResource(R.drawable.user_bar_button_background);
                binding.userBarRecord.setTextColor(Color.parseColor("#2051BD"));
                binding.userBarReport.setBackgroundResource(R.drawable.user_bar_button_background);
                binding.userBarReport.setTextColor(Color.parseColor("#2051BD"));

                binding.userCardInfo.setVisibility(View.GONE);
                binding.userCardEvaluate.setVisibility(View.GONE);
                binding.userCardQuestion.setVisibility(View.VISIBLE);
                binding.userCardRecord.setVisibility(View.GONE);
                binding.userCardReport.setVisibility(View.GONE);

                shp.saveToSp("userbarpostion", "3");
            }
        });

        binding.userBarRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.userBarInfo.setBackgroundResource(R.drawable.user_bar_button_background);
                binding.userBarInfo.setTextColor(Color.parseColor("#2051BD"));
                binding.userBarEvaluate.setBackgroundResource(R.drawable.user_bar_button_background);
                binding.userBarEvaluate.setTextColor(Color.parseColor("#2051BD"));
                binding.userBarQuestion.setBackgroundResource(R.drawable.user_bar_button_background);
                binding.userBarQuestion.setTextColor(Color.parseColor("#2051BD"));
                binding.userBarRecord.setBackgroundResource(R.drawable.user_bar_button_background_true);
                binding.userBarRecord.setTextColor(Color.parseColor("#FFFFFF"));

                binding.userBarReport.setBackgroundResource(R.drawable.user_bar_button_background);
                binding.userBarReport.setTextColor(Color.parseColor("#2051BD"));

                binding.userCardInfo.setVisibility(View.GONE);
                binding.userCardEvaluate.setVisibility(View.GONE);
                binding.userCardQuestion.setVisibility(View.GONE);
                binding.userCardRecord.setVisibility(View.VISIBLE);
                binding.userCardReport.setVisibility(View.GONE);

                shp.saveToSp("userbarpostion", "2");
            }
        });


        binding.userBarReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.userBarInfo.setBackgroundResource(R.drawable.user_bar_button_background);
                binding.userBarInfo.setTextColor(Color.parseColor("#2051BD"));
                binding.userBarEvaluate.setBackgroundResource(R.drawable.user_bar_button_background);
                binding.userBarEvaluate.setTextColor(Color.parseColor("#2051BD"));
                binding.userBarQuestion.setBackgroundResource(R.drawable.user_bar_button_background);
                binding.userBarQuestion.setTextColor(Color.parseColor("#2051BD"));
                binding.userBarRecord.setBackgroundResource(R.drawable.user_bar_button_background);
                binding.userBarRecord.setTextColor(Color.parseColor("#2051BD"));
                binding.userBarReport.setBackgroundResource(R.drawable.user_bar_button_background_true);
                binding.userBarReport.setTextColor(Color.parseColor("#FFFFFF"));

                binding.userCardInfo.setVisibility(View.GONE);
                binding.userCardEvaluate.setVisibility(View.GONE);
                binding.userCardQuestion.setVisibility(View.GONE);
                binding.userCardRecord.setVisibility(View.GONE);
                binding.userCardReport.setVisibility(View.VISIBLE);
                shp.saveToSp("userbarpostion", "5");
            }
        });

        if (barPosition.equals("1")){
            binding.userBarInfo.performClick();
        }else if (barPosition.equals("2")){
            binding.userBarRecord.performClick();
        }else if (barPosition.equals("3")){
            binding.userBarQuestion.performClick();
        }else if (barPosition.equals("4")){
            binding.userBarEvaluate.performClick();
        }else if (barPosition.equals("5")){
            binding.userBarReport.performClick();
        }
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
                if (v==1){
                    return "1";
                }
                if (v==2){
                    return "2";
                }
                if (v==3){
                    return "3";
                }
                if (v==4){
                    return "4";
                }
                if (v==5){
                    return "5";
                }
                return "";//注意这里需要改成 ""
            }
        });
        xAxis.setAxisMaximum(6);   //X轴最大数值
        xAxis.setAxisMinimum(0);   //X轴最小数值
        //X轴坐标的个数    第二个参数一般填false     true表示强制设置标签数 可能会导致X轴坐标显示不全等问题
        xAxis.setLabelCount(6,false);

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
                for (int a=0;a<31;a++){     //用个for循环方便
                    if (a==v){
                        return String.valueOf(a);
                    }
                }

                return "";
            }
        });
        AxisLeft.setAxisMaximum(30);   //Y轴最大数值
        AxisLeft.setAxisMinimum(0);   //Y轴最小数值
        //Y轴坐标的个数    第二个参数一般填false     true表示强制设置标签数 可能会导致X轴坐标显示不全等问题
        AxisLeft.setLabelCount(15,false);

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
        barDataSet.setBarBorderWidth(0);       //柱子边框厚度
        barDataSet.setBarShadowColor(Color.RED);
        barDataSet.setHighlightEnabled(false);//选中柱子是否高亮显示  默认为true
        barDataSet.setStackLabels(new String[]{"aaa","bbb","ccc"});
        //定义柱子上的数据显示    可以实现加单位    以及显示整数（默认是显示小数）
//        barDataSet.setValueFormatter(new IValueFormatter(){
//
//            @Override
//            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
//                return null;
//            }
//        });

        //数据更新
        barChart.notifyDataSetChanged();
        barChart.invalidate();

        //动画（如果使用了动画可以则省去更新数据的那一步）
        barChart.animateY(3000); //在Y轴的动画  参数是动画执行时间 毫秒为单位
//        line.animateX(2000); //X轴动画
//        line.animateXY(2000,2000);//XY两轴混合动画

    }
}