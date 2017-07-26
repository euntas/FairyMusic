package com.music.fairy.fairymusic.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;

import com.music.fairy.fairymusic.R;
import com.music.fairy.fairymusic.ui.base.BaseActivity;
import com.music.fairy.fairymusic.ui.quote.ListActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Activity demonstrates some GUI functionalities from the Android support library.
 *
 * Created by Andreas Schrade on 14.12.2015.
 */
public class QuizActivity extends BaseActivity {

    TextView textView;
    String quizList;
    String quizResult;
    String quizAnswer;
    String quizFinal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        ButterKnife.bind(this);
        setupToolbar();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        URL url = null;
        HttpURLConnection con = null;
        StringBuilder sb = new StringBuilder();


        try{
            // http://203.233.196.130:8888/webre/
            url = new URL("http://203.233.196.130:8888/fairybook/app/quiz");
        }catch (MalformedURLException e){
            Toast.makeText(this, "잘못된 URL입니다.", Toast.LENGTH_SHORT).show();
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

                String request_str = "{'selectionStr' : '621'}";
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
                  quizList = sb.toString();
                    try {
                        JSONArray jarray = new JSONArray(quizList);
                        StringBuilder sb2 = new StringBuilder();
                        StringBuilder sb3 = new StringBuilder();

                        for (int i = 0; i < jarray.length(); i++) {
                            JSONObject jobject = jarray.getJSONObject(i);
                            int selectionnum = jobject.getInt("selectionnum");
                            int quiznum = jobject.getInt("quizNum");
                            String question = jobject.getString("question");
                            String select1 = jobject.getString("select1");
                            String select2 = jobject.getString("select2");
                            String select3 = jobject.getString("select3");
                            String select4 = jobject.getString("select4");
                            int answer = jobject.getInt("answer");
                            int myanswer = jobject.getInt("myanswer");

                                sb2.append(
                                        "\n" +
                                    "문제:" + question + "\n" +
                                            "① " + select1 + "\n" +
                                            "② " + select2 + "\n" +
                                            "③ " + select3 + "\n" +
                                            "④ " + select4 + "\n" +
                                            "정답: " + answer + "\n" +
                                            "고른 답: " + myanswer + "\n"
                            );

                            if (answer == myanswer){
                                sb3.append(
                                  "right"
                                );
                            } else {
                                sb3.append(
                                    "wrong"
                                );
                            }

                        }//for
                        quizResult = sb2.toString();
                        quizAnswer = sb3.toString();
                        int color = Color.RED;
                        SpannableStringBuilder builder = new SpannableStringBuilder(quizAnswer);
                        builder.setSpan(new ForegroundColorSpan(color), 1, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        quizFinal = quizResult.concat(quizAnswer);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }//if
            }
        }
        catch (Exception e){
            Toast.makeText(this, ""+e.toString(), Toast.LENGTH_LONG).show();
        }

    }

    public void quizCheck(View v){
        textView = (TextView) findViewById(R.id.textview);
        textView.setVisibility(v.VISIBLE);
        textView.setText(quizFinal);
    }


    private void setupToolbar() {
        final ActionBar ab = getActionBarToolbar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sample_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                openDrawer();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int getSelfNavDrawerItem() {
        return R.id.nav_samples;
    }

    @Override
    public boolean providesActivityToolbar() {
        return true;
    }


    /*public void quiz(View v) {

        URL url = null;
        HttpURLConnection con = null;
        StringBuilder sb = new StringBuilder();
        String quizList;
        Toast.makeText(this, "adsadasdasd", Toast.LENGTH_SHORT).show();

        try{
            // http://203.233.196.130:8888/webre/
            url = new URL("http://203.233.196.128:8888/fairybook/app/quiz");
        }catch (MalformedURLException e){
            Toast.makeText(this, "잘못된 URL입니다.", Toast.LENGTH_SHORT).show();
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

                String request_str = "{'selectionStr' : '621'}";
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
                    quizList = sb.toString();
                    textView.setText(quizList);
                    Toast.makeText(this, quizList, Toast.LENGTH_SHORT).show();


                }
            }
        }
        catch (Exception e){
            Toast.makeText(this, ""+e.toString(), Toast.LENGTH_SHORT).show();
        }
    }*/
}
