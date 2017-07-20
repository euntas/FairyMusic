package com.music.fairy.fairymusic.ui.quote;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.music.fairy.fairymusic.R;


/**
 * Created by kita on 2017-07-19.
 */

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void goMain(View v) {
        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);
    }
}



