package com.hzdq.bajiesleepchildrenHD.test;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.github.mikephil.charting.data.BarEntry;
import com.hzdq.bajiesleepchildrenHD.R;

import java.util.ArrayList;
import java.util.List;

public class TestActivity2 extends AppCompatActivity {

    private List<Integer> list = new ArrayList<>();

    private List<List<Integer>> list2 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);

        int pages = 25;

        for (int i = 1; i <= pages; i++) {
            list.add(i);
        }

        for (int i = 0; i <= 10; i++) {

        }

        Log.d("hello", "onCreate: "+list);

       List<List<Integer>> sList =  splitList(list,10);
        Log.d("hello", "onCreate: "+sList);
 new BarEntry(1,3);

    }


    private List<List<Integer>> splitList(List<Integer> list , int groupSize){
        int length = list.size();
        // 计算可以分成多少组
        int num = ( length + groupSize - 1 )/groupSize ; // TODO
        List<List<Integer>> newList = new ArrayList<>(num);
        for (int i = 0; i < num; i++) {
            // 开始位置
            int fromIndex = i * groupSize;
            // 结束位置
            int toIndex = (i+1) * groupSize < length ? ( i+1 ) * groupSize : length ;
            newList.add(list.subList(fromIndex,toIndex)) ;
        }
        return  newList ;
    }
}