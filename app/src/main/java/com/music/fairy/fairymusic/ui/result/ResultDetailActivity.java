package com.music.fairy.fairymusic.ui.result;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.music.fairy.fairymusic.R;
import com.music.fairy.fairymusic.ui.base.BaseActivity;
import com.music.fairy.fairymusic.ui.quote.ArticleDetailFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Simple wrapper for {@link ResultDetailFragment}
 * This wrapper is only used in single pan mode (= on smartphones)
 * Created by Andreas Schrade on 14.12.2015.
 */
public class ResultDetailActivity extends BaseActivity {

    String MBTIInfo = null;
    PieChart pieChart = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_detail);

        // Show the Up button in the action bar.
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //메인 스레드에서 서버 네트워크를 접속하려면 권한을 모두 부여 받아야 한다. (비추천)
        // 별도의 스레드를 구성하는것을 추천한다.
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        Log.i("RDA", "아이디: " + id);
        Log.i("RDA", "셀넘: " + ResultContent.ITEM_MAP.get(id).selectionNum);

        int selectionNum = ResultContent.ITEM_MAP.get(id).selectionNum;

        // MBTI HTP 결과 다 지우고 다시
        ResultContent.clearAllItem();

        setMbti(selectionNum);
        setHTPTree(selectionNum);
        pieChart = (PieChart)findViewById(R.id.chart_color);
        setColor(selectionNum);
        setHouse(selectionNum);

        ResultDetailFragment fragment =  ResultDetailFragment.newInstance(getIntent().getStringExtra(ResultDetailFragment.ARG_ITEM_ID));
        getFragmentManager().beginTransaction().replace(R.id.result_detail_container, fragment).commit();

    }


    public void setColor(int selectionNum){
        URL url = null;
        HttpURLConnection con = null;
        StringBuilder sb = new StringBuilder();

        try{
            // http://203.233.196.130:8888/webre/
            url = new URL("http://203.233.196.130:8888/fairybook/app/getColorResult");
        }catch (MalformedURLException e){
            Log.i("RDF merr", e.toString());
        }
        try {
            con = (HttpURLConnection) url.openConnection();
            if (con != null) {
                con.setConnectTimeout(10000); //연결제한시간. 0은 무한대기.
                con.setUseCaches(false); //캐쉬 사용여부
                con.setRequestMethod("POST"); //요청방식 선택 (GET, POST)
                con.setRequestProperty("Content-Type", "application/json");
                con.setDoOutput(true); //OutputStream으로 POST 데이터를 넘겨주겠다는 옵션.
                con.setDoInput(true); //InputStream으로 서버로 부터 응답을 받겠다는 옵션.

                String request_str = "{'selectionNum' : '" + selectionNum + "'}";
                JSONObject requestObj = new JSONObject(request_str);

                OutputStream os = con.getOutputStream(); //Request Body에 Data를 담기위해 OutputStream 객체를 생성.
                os.write(requestObj.toString().getBytes());
                os.flush();
                os.close();

                if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStreamReader in = new InputStreamReader(con.getInputStream());

                    int ch;

                    // 웹서버에서 반환값 받아오기
                    while ((ch = in.read()) != -1) {
                        sb.append((char) ch);
                    }

                    in.close();

                    Log.i("RDA", "받아온거: " + sb.toString());

                    JSONObject jo = new JSONObject(sb.toString());
                    JSONArray colorName = jo.getJSONArray("colorName");
                    JSONArray colorAnalysis = jo.getJSONArray("colorAnalysis");
                    JSONArray colorCount = jo.getJSONArray("colorCount");

                    for (int i = 0; i < colorName.length(); i++) {
                        ResultContent.COLOR.put(i, new ResultContent.ColorInfo(colorName.getString(i), colorAnalysis.getString(i), colorCount.getInt(i)));
                    }

                }
            }
        }
        catch (Exception e){
            Log.i("RDF", e.toString());
        }

    }


    public void setMbti(int selectionNum){
        Log.i("MBTI", "들어왔음" + selectionNum);
        URL url = null;
        HttpURLConnection con = null;
        StringBuilder sb = new StringBuilder();

        try{
            // http://203.233.196.130:8888/webre/
            url = new URL("http://203.233.196.130:8888/fairybook/app/getMBTIResult");
        }catch (MalformedURLException e){
            Log.i("RDF merr", e.toString());
        }
        try{
            con = (HttpURLConnection)url.openConnection();
            if(con != null){
                con.setConnectTimeout(10000); //연결제한시간. 0은 무한대기.
                con.setUseCaches(false); //캐쉬 사용여부
                con.setRequestMethod("POST"); //요청방식 선택 (GET, POST)
                con.setRequestProperty("Content-Type","application/json");
                con.setDoOutput(true); //OutputStream으로 POST 데이터를 넘겨주겠다는 옵션.
                con.setDoInput(true); //InputStream으로 서버로 부터 응답을 받겠다는 옵션.

                String request_str = "{'selectionNum' : '" + selectionNum + "'}";
                JSONObject requestObj = new JSONObject(request_str);

                OutputStream os = con.getOutputStream(); //Request Body에 Data를 담기위해 OutputStream 객체를 생성.
                os.write(requestObj.toString().getBytes());
                os.flush();
                os.close();

                if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStreamReader in = new InputStreamReader(con.getInputStream());

                    int ch;

                    // 웹서버에서 반환값 받아오기
                    while ((ch = in.read()) != -1) {
                        sb.append((char) ch);
                    }

                    in.close();

                    Log.i("RDA", "받아온거: " + sb.toString());

                    JSONObject jo = new JSONObject(sb.toString());
                    String mt = jo.getString("mbtiType");
                    String ma = jo.getString("mbtiAnalysis");
                    ResultContent.MBTI.put("mbtiType", mt);
                    ResultContent.MBTI.put("mbtiAnalysis", ma);

                }
            }
        }
        catch (Exception e){
            Log.i("RDF", e.toString());
        }
    }

    public void setHTPTree(int selectionNum){
        Log.i("HTPTree", "들어왔음" + selectionNum);
        URL url = null;
        HttpURLConnection con = null;
        StringBuilder sb = new StringBuilder();

        try{
            // http://203.233.196.130:8888/webre/
            url = new URL("http://203.233.196.130:8888/fairybook/app/getHTPTreeResult");
        }catch (MalformedURLException e){
            Log.i("RDF htptreeerr", e.toString());
        }
        try{
            con = (HttpURLConnection)url.openConnection();
            if(con != null){
                con.setConnectTimeout(10000); //연결제한시간. 0은 무한대기.
                con.setUseCaches(false); //캐쉬 사용여부
                con.setRequestMethod("POST"); //요청방식 선택 (GET, POST)
                con.setRequestProperty("Content-Type","application/json");
                con.setDoOutput(true); //OutputStream으로 POST 데이터를 넘겨주겠다는 옵션.
                con.setDoInput(true); //InputStream으로 서버로 부터 응답을 받겠다는 옵션.

                String request_str = "{'selectionNum' : '" + selectionNum + "'}";
                JSONObject requestObj = new JSONObject(request_str);

                OutputStream os = con.getOutputStream(); //Request Body에 Data를 담기위해 OutputStream 객체를 생성.
                os.write(requestObj.toString().getBytes());
                os.flush();
                os.close();

                if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStreamReader in = new InputStreamReader(con.getInputStream());

                    int ch;

                    // 웹서버에서 반환값 받아오기
                    while ((ch = in.read()) != -1) {
                        sb.append((char) ch);
                    }

                    in.close();

                    Log.i("RDA", "받아온거: " + sb.toString());

                    JSONObject jo = new JSONObject(sb.toString()).getJSONObject("htpTree");

                    FBResource resObj = new FBResource(jo.getInt("resourceNum"), jo.getString("name"), jo.getString("analysis"));
                    ResultContent.HTP.put("htpTree", resObj);

                }
            }
        }
        catch (Exception e){
            Log.i("RDF", e.toString());
        }
    }

    public void setHouse(int selectionNum){
        URL url = null;
        HttpURLConnection con = null;
        StringBuilder sb = new StringBuilder();

        try{
            // http://203.233.196.130:8888/webre/
            url = new URL("http://203.233.196.130:8888/fairybook/app/getHouseResult");
        }catch (MalformedURLException e){
            Log.i("RDF err", e.toString());
        }
        try{
            con = (HttpURLConnection)url.openConnection();
            if(con != null){
                con.setConnectTimeout(10000); //연결제한시간. 0은 무한대기.
                con.setUseCaches(false); //캐쉬 사용여부
                con.setRequestMethod("POST"); //요청방식 선택 (GET, POST)
                con.setRequestProperty("Content-Type","application/json");
                con.setDoOutput(true); //OutputStream으로 POST 데이터를 넘겨주겠다는 옵션.
                con.setDoInput(true); //InputStream으로 서버로 부터 응답을 받겠다는 옵션.

                String request_str = "{'selectionNum' : '" + selectionNum + "'}";
                JSONObject requestObj = new JSONObject(request_str);

                OutputStream os = con.getOutputStream(); //Request Body에 Data를 담기위해 OutputStream 객체를 생성.
                os.write(requestObj.toString().getBytes());
                os.flush();
                os.close();

                if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStreamReader in = new InputStreamReader(con.getInputStream());

                    int ch;

                    // 웹서버에서 반환값 받아오기
                    while ((ch = in.read()) != -1) {
                        sb.append((char) ch);
                    }

                    in.close();

                    //Log.i("RDA", "받아온거: " + sb.toString());

                    JSONObject jo = new JSONObject(sb.toString());
                    Log.i("RDA", "받아온거: " + jo.toString());

                    JSONObject jo2 = (JSONObject) jo.get("roof");
                    FBResource resObj = new FBResource(jo2.getString("name"), jo2.getString("analysis"));
                    ResultContent.HOUSE.put("roof", resObj);

                    jo2 = (JSONObject) jo.get("window");
                    resObj = new FBResource(jo2.getString("name"), jo2.getString("analysis"));
                    ResultContent.HOUSE.put("window", resObj);

                    jo2 = (JSONObject) jo.get("chimney");
                    resObj = new FBResource(jo2.getString("name"), jo2.getString("analysis"));
                    ResultContent.HOUSE.put("chimney", resObj);

                    jo2 = (JSONObject) jo.get("wall");
                    resObj = new FBResource(jo2.getString("name"), jo2.getString("analysis"));
                    ResultContent.HOUSE.put("wall", resObj);

                    jo2 = (JSONObject) jo.get("door");
                    resObj = new FBResource(jo2.getString("name"), jo2.getString("analysis"));
                    ResultContent.HOUSE.put("door", resObj);

                }
            }
        }
        catch (Exception e){
            Log.i("RDF", e.toString());
        }
    }


    @Override
    public boolean providesActivityToolbar() {
        return false;
    }
}
