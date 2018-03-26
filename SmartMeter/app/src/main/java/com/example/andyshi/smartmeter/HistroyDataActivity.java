package com.example.andyshi.smartmeter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class HistroyDataActivity extends AppCompatActivity implements OnChartGestureListener{
    LineChart rmsChart;
    private SharedPreferences pref;
    String devicename;
    String result="";
    TextView deviceID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_histroy_data);
        rmsChart = (LineChart)findViewById(R.id.chart_rms);
        deviceID = (TextView)findViewById(R.id.tv_deviicename);
        setLineChart(rmsChart);
        pref = getSharedPreferences("id",0);
        devicename = pref.getString("device","0");
        deviceID.setText("設備："+devicename);
        GetHistory getHistory = new GetHistory();
        getHistory.execute("");
    }

    private void setLineChart(LineChart chart) {
//        chart.setBackgroundColor(0xff00ff00);
        chart.setDescription(" "); // 設置圖表的描述文字，會顯示在圖表的右下角
//        chart.setDescriptionColor(Color.RED);
//        chart.setDescriptionPosition(150f,150f);  //自定義描述文字在螢幕上的位置（單位是像素）
//        chart.setDescriptionTextSize(16f);
        chart.setNoDataTextDescription("尚無資料！");
        // 设置手勢
        chart.setOnChartGestureListener(this);
        chart.setDrawGridBackground(false); // 設置網格背景
        chart.setTouchEnabled(true);
        chart.setDoubleTapToZoomEnabled(true);
        chart.setScaleEnabled(true); // 設置缩放
        chart.setDoubleTapToZoomEnabled(true); // 設置雙擊進行缩放
        chart.setAutoScaleMinMaxEnabled(false);
        // 設置X轴
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // 設置X軸的位置
//        xAxis.setTypeface(mTf); // 設置字體
        xAxis.setEnabled(true);
        xAxis.setDrawGridLines(false);
        xAxis.setSpaceBetweenLabels(6);  //設置標籤字符間的空隙，默認characters间隔是4
//        xAxis.setLabelsToSkip(6);  //設置在”繪製下一個標籤”時，要忽略的標籤數
        // 圖例
        Legend legend = chart.getLegend();
//        legend.setEnabled(false);
//        legend.setTextColor(Color.GREEN);
        legend.setTextSize(10f);
        legend.setFormSize(18f);
        legend.setForm(Legend.LegendForm.SQUARE);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setForm(Legend.LegendForm.LINE);
        // 獲得左側座標軸
        YAxis leftAxis = chart.getAxisLeft();
//        leftAxis.setValueFormatter(new MyYAxisValueFormatter());
        leftAxis.setTextSize(10f);  //設置Y軸字體大小
//        leftAxis.setTextColor(Color.GREEN);
        // 出現錯誤，leftAxis的setValueFormatter參數類型只能是 YAxisFormatter
//        leftAxis.setValueFormatter(new MyValueFormatter());
//        leftAxis.addLimitLine(yLimitLine);
//        leftAxis.setDrawLimitLinesBehindData(false);
//        leftAxis.setInverted(true);
//        leftAxis.setTypeface(mTf);
//        leftAxis.setAxisLineWidth(1.5f);
        leftAxis.setLabelCount(5, false);
//        leftAxis.setShowOnlyMinMax(true);
//        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        //設置右側座標軸
        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setDrawAxisLine(false); //右側座標軸線
        rightAxis.setDrawLabels(false); // 右側座標軸數組Label
    }

    private void loadLineChartData(LineChart chart, String str, int color, int datanumber, String[] data, String[] array) {
        MyMarkerView mv = new MyMarkerView(this,R.layout.custom_markerview);
        chart.setMarkerView(mv);
        //所有线的List
        ArrayList<LineDataSet> allLinesList = new ArrayList<LineDataSet>();

        ArrayList<Entry> entryList = new ArrayList<Entry>();
        for (int i = 0; i < datanumber; i++) {
            //Entry(yValue,xIndex);一个Entry表示一个点，第一个参数为y值，第二个为X轴List的角标
            entryList.add(new Entry(Float.parseFloat(data[i]), i));
        }
        //LineDataSet可以看做是一条线
        LineDataSet dataSet = new LineDataSet(entryList, str);
        dataSet.setLineWidth(2.5f);  //設置線寬
        dataSet.setCircleSize(2.5f);
        dataSet.setColor(color);
        dataSet.setCircleColor(color);
        dataSet.setHighLightColor(Color.GRAY); // 设置点击某个点时，横竖两条线的颜色
        dataSet.setDrawValues(false); // 是否在点上绘制Value
        dataSet.setValueTextColor(Color.RED);
        dataSet.setValueTextSize(15f);
        allLinesList.add(dataSet);

//        Typeface tf1 = Typeface.createFromAsset(getAssets(), "OpenSans-BoldItalic.ttf");
//        dataSet.setValueTypeface(tf1);

//        Typeface tf2 = Typeface.createFromAsset(getAssets(), "OpenSans-LightItalic.ttf");
//        dataSet.setValueTypeface(tf2);

        //LineData表示一个LineChart的所有数据(即一个LineChart中所有折线的数据)
        LineData mChartData = new LineData(array, allLinesList);

        // set data
        chart.setData((LineData) mChartData);
        chart.animateY(3000);
        chart.setVisibleXRangeMaximum(48);  //設置初始x軸資料筆
        chart.setVisibleYRangeMaximum(10000, YAxis.AxisDependency.LEFT);
        chart.moveViewToY(60,YAxis.AxisDependency.LEFT);
    }



    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartLongPressed(MotionEvent me) {

    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {

    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {

    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {

    }

    public class GetHistory extends AsyncTask<String,String,String> {
        String connecturl = "http://163.18.57.43/rms/rmsget_lastest_count2.php";
        String count = "100";
        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(connecturl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("machine", "UTF-8") + "=" + URLEncoder.encode(devicename, "UTF-8") + "&" + URLEncoder.encode("num","UTF-8") + "=" + URLEncoder.encode(count,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));

                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result = line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            String[] historyData = result.split(" ");
            String[] array = new String[historyData.length];
            for(int i=1;i<=historyData.length;i++){
                array[i-1] = String.valueOf(i);
            }
            loadLineChartData(rmsChart, "Irms", Color.DKGRAY, historyData.length, historyData, array);
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Intent reit = new Intent();
        reit.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        reit.setClass(HistroyDataActivity.this,MainActivity.class);
        startActivity(reit);
        HistroyDataActivity.this.finish();
        return true;
    }
}
