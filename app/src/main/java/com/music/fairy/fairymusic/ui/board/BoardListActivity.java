package com.music.fairy.fairymusic.ui.board;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.music.fairy.fairymusic.R;
import com.music.fairy.fairymusic.dummy.BoardContent;
import com.music.fairy.fairymusic.ui.base.BaseActivity;
import com.music.fairy.fairymusic.util.LogUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by KITA on 2017-07-24.
 */

public class BoardListActivity  extends BaseActivity implements BArticleListFragment.Callback{
    /**
     * Whether or not the activity is running on a device with a large screen
     * 
     */
    private boolean twoPaneMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_activity_list);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Toast.makeText(BoardListActivity.this, "Board", Toast.LENGTH_SHORT).show();

        setupToolbar();

        if (isTwoPaneLayoutUsed()) {
            twoPaneMode = true;
            LogUtil.logD("TEST","TWO POANE TASDFES");
            enableActiveItemState();
        }

        if (savedInstanceState == null && twoPaneMode) {
            setupDetailFragment();
        }
    }

    public void setList(){
        URL url = null;
        HttpURLConnection con = null;
        StringBuilder sb = new StringBuilder();
        JSONObject json = null;
        JSONArray jarray = null;
        JSONObject item = null;
        try {
            url = new URL("localhost:8888/fairybook/app/boardList"); //203.233.196.130
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            con = (HttpURLConnection) url.openConnection();
            if (con != null){
                con.setConnectTimeout(1000000);
                con.setUseCaches(false);
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type","application/json");
                con.setDoOutput(true);
                con.setDoInput(true);
                if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStreamReader in = new InputStreamReader(con.getInputStream());
                    int ch;
                    while((ch = in.read()) != -1){
                        sb.append((char) ch);
                    }
                    in.close();
                    String jsontext = sb.toString();
                    String jt = jsontext;
                    if (jt != null) {
                        try {
                            json = new JSONObject(jt);
                            jarray = json.getJSONArray("boardList");
                            //StringBuilder sb2 = new StringBuilder();
                            for (int i = 0; i < jarray.length(); i++) {
                                item = jarray.getJSONObject(i);

                                BoardContent.BoardItem bi = new BoardContent.BoardItem(item.getInt("boardnum"), R.drawable.ryan1, item.getString("id"), item.getString("title")
                                        , item.getString("content"), item.getString("inputdate"),item.getInt("hit"));
                                System.out.println(bi.toString());

                            }
                            //textview.setText(sb2.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Called when an item has been selected
     *
     * @param id the selected quote ID
     */
    @Override
    public void onItemSelected(String id) {
        if (twoPaneMode) {
            // Show the quote detail information by replacing the DetailFragment via transaction.
            BArticleDetailFragment fragment = BArticleDetailFragment.newInstance(id);
            getFragmentManager().beginTransaction().replace(R.id.article_detail_container, fragment).commit();
        } else {
            // Start the detail activity in single pane mode.
            Intent detailIntent = new Intent(this, BArticleDetailActivity.class);
            detailIntent.putExtra(BArticleDetailFragment.ARG_ITEM_ID, id);
            startActivity(detailIntent);
        }
    }

    private void setupToolbar() {
        final ActionBar ab = getActionBarToolbar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
    }

    private void setupDetailFragment() {
        BArticleDetailFragment fragment =  BArticleDetailFragment.newInstance(BoardContent.ITEMS.get(0).id);
        getFragmentManager().beginTransaction().replace(R.id.article_detail_container, fragment).commit();
    }

    /**
     * Enables the functionality that selected items are automatically highlighted.
     */
    private void enableActiveItemState() {
        BArticleListFragment fragmentById = (BArticleListFragment) getFragmentManager().findFragmentById(R.id.board_article_list);
        fragmentById.getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

    /**
     * Is the container present? If so, we are using the two-pane layout.
     *
     * @return true if the two pane layout is used.
     */
    private boolean isTwoPaneLayoutUsed() {
        return findViewById(R.id.article_detail_container) != null;
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
}
