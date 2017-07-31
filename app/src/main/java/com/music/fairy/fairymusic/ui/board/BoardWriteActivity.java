package com.music.fairy.fairymusic.ui.board;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.music.fairy.fairymusic.R;
import com.music.fairy.fairymusic.ui.base.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by KITA on 2017-07-26.
 */

public class BoardWriteActivity extends BaseActivity{ //글쓰기

    EditText title;
    EditText content;
    Button save;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_write);

        setupToolbar();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Toast.makeText(BoardWriteActivity.this, "Write", Toast.LENGTH_SHORT).show();

        save = (Button) findViewById(R.id.btnSave);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(BoardWriteActivity.this, "저장눌림", Toast.LENGTH_SHORT).show();
                //saveNewBoard();
            }
        });
    }

    private void saveNewBoard() {
        title = (EditText) findViewById(R.id.txtTitle);
        content = (EditText) findViewById(R.id.txtContent);

        JSONObject jObject = null;
        try {
            jObject.put("title", title);
            jObject.put("content", content);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        URL url = null;
        HttpURLConnection con = null;
        StringBuilder sb = new StringBuilder();
        try {
            url = new URL("http://1.235.198.41:8888/fairybook/app/writeBoard");
        } catch (MalformedURLException e) {
            Toast.makeText(this, "잘못된 URL입니다.", Toast.LENGTH_SHORT).show();
        }
        try {
            con = (HttpURLConnection) url.openConnection();
            if (con != null) {
                con.setConnectTimeout(10000);  //연결제한시간. 0은 무한대기.
                con.setUseCaches(false);      //캐쉬 사용여부
                con.setRequestMethod("POST");  //요청방식 선택 (GET, POST)
                con.setRequestProperty("Content-Type", "application/json");
                con.setDoOutput(true);  //OutputStream으로 POST 데이터를 넘겨주겠다는 옵션.
                con.setDoInput(true);  //InputStream으로 서버로 부터 응답을 받겠다는 옵션.
                OutputStream os = con.getOutputStream();    //Request Body에 Data를 담기위해 OutputStream 객체를 생성.
                os.write(jObject.toString().getBytes("utf-8"));
                os.flush();
                os.close();
                if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStreamReader in = new InputStreamReader(con.getInputStream());
                    int ch;
                    while ((ch = in.read()) != -1) {
                        sb.append((char) ch);
                    }
                    in.close();
                    String jsontext = sb.toString();
                    Toast.makeText(this, jsontext, Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, "" + e.toString(), Toast.LENGTH_SHORT).show();
        }
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
        return R.id.nav_quotes;
    }

    @Override
    public boolean providesActivityToolbar() {
        return true;
    }

    private void setupToolbar() {
        final ActionBar ab = getActionBarToolbar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
    }
}
