package com.music.fairy.fairymusic.ui.board;

import android.os.Bundle;

import com.music.fairy.fairymusic.R;
import com.music.fairy.fairymusic.ui.base.BaseActivity;

/**
 * Created by KITA on 2017-07-24.
 */

public class BArticleDetailActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Show the Up button in the action bar.
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        BArticleDetailFragment fragment =  BArticleDetailFragment.newInstance(getIntent().getStringExtra(BArticleDetailFragment.ARG_ITEM_ID));
        getFragmentManager().beginTransaction().replace(R.id.article_detail_container, fragment).commit();
    }

    @Override
    public boolean providesActivityToolbar() {
        return false;
    }
}
