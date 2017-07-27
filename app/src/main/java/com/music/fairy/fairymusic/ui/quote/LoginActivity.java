package com.music.fairy.fairymusic.ui.quote;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.music.fairy.fairymusic.R;
import com.music.fairy.fairymusic.dummy.DummyContent;
import com.music.fairy.fairymusic.ui.board.BoardListActivity;

import org.json.JSONObject;

import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Created by kita on 2017-07-19.
 */

public class LoginActivity extends AppCompatActivity {
    private EditText textId;
    private EditText textPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //메인 스레드에서 서버 네트워크를 접속하려면 권한을 모두 부여 받아야 한다. (비추천)
        // 별도의 스레드를 구성하는것을 추천한다.
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    public void goMain(View v) {
        URL url = null;
        HttpURLConnection con = null;
        StringBuilder sb = new StringBuilder();
        try{
            // http://203.233.196.130:8888/webre/
            url = new URL("http://203.233.196.130:8888/fairybook/app/login");
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

                textId = (EditText)findViewById(R.id.sample_text_username);
                textPassword = (EditText)findViewById(R.id.sample_text_email);

                String id = textId.getText().toString();
                String password = textPassword.getText().toString();

                String request_str = "{'id' : " +  id +  " , 'password' : " + password + "}";
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

                    // 로그인 성공시
                    if(sb.toString().equals("true")){
                        DummyContent.LOGIN_ID = textId.getText().toString();
                        Intent intent = new Intent(this, BoardListActivity.class);
                        startActivity(intent);
                    }

                    else if(sb.toString().equals("child")){
                        textId.setText("");
                        textPassword.setText("");
                        Toast.makeText(this, "보호자용으로 로그인해주세요!", Toast.LENGTH_SHORT).show();
                    }

                    // 로그인 실패시
                    else{
                        textId.setText("");
                        textPassword.setText("");
                        Toast.makeText(this, "로그인 실패", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        }
        catch (Exception e){
            Toast.makeText(this, ""+e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}



