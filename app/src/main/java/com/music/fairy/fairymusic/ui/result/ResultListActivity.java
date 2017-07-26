package com.music.fairy.fairymusic.ui.result;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.music.fairy.fairymusic.R;
import com.music.fairy.fairymusic.ui.base.BaseActivity;
import com.music.fairy.fairymusic.ui.quote.ArticleDetailActivity;
import com.music.fairy.fairymusic.ui.quote.ArticleDetailFragment;
import com.music.fairy.fairymusic.ui.quote.ArticleListFragment;
import com.music.fairy.fairymusic.ui.quote.ListActivity;
import com.music.fairy.fairymusic.util.LogUtil;

import com.music.fairy.fairymusic.ui.result.ResultListActivity;
import com.music.fairy.fairymusic.ui.result.ResultListFragment;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Lists all available quotes. This Activity supports a single pane (= smartphones) and a two pane mode (= large screens with >= 600dp width).
 *
 * Created by Andreas Schrade on 14.12.2015.
 */
public class ResultListActivity extends BaseActivity implements ResultListFragment.Callback {
    static final String TAG = "eunji";
    String mySelectionList = null;
    /**
     * Whether or not the activity is running on a device with a large screen
     */
    private boolean twoPaneMode;
    private JSONObject jObject = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_list);

        setupToolbar();

        if (isTwoPaneLayoutUsed()) {
            twoPaneMode = true;
            LogUtil.logD("TEST","TWO POANE TASDFES");
            enableActiveItemState();
        }

        //메인 스레드에서 서버 네트워크를 접속하려면 권한을 모두 부여 받아야 한다. (비추천)
        // 별도의 스레드를 구성하는것을 추천한다.
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        getListdata();

        if (savedInstanceState == null && twoPaneMode) {
            setupDetailFragment();
        }

    }

    public void getListdata(){
        URL url = null;
        HttpURLConnection con = null;
        StringBuilder sb = new StringBuilder();

        try{
            // http://203.233.196.130:8888/webre/
            url = new URL("http://203.233.196.130:8888/fairybook/app/getResultSelectionNum");
        }catch (MalformedURLException e){
            Toast.makeText(getApplicationContext(), "잘못된 URL입니다.", Toast.LENGTH_SHORT).show();
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

                String request_str = "{'loginId' : 'test'}";
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

                    ResultContent.clearItem();

                    mySelectionList = sb.toString();

                    JSONArray jarr = new JSONArray(mySelectionList);

                    for(int i=0; i<jarr.length(); i++){
                        JSONObject jo = jarr.getJSONObject(i);
                        int selNum = jo.getInt("selectionNum");
                        String selectionNum = String.valueOf(selNum);

                        ResultContent.addItem(new ResultContent.ResultItem(selectionNum, selNum));
                    }

                    Log.i(TAG, mySelectionList);

                }
            }
        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(), "여기"+e.toString(), Toast.LENGTH_LONG).show();
            Log.i(TAG, e.toString());
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
            ResultDetailFragment fragment = ResultDetailFragment.newInstance(id);
            getFragmentManager().beginTransaction().replace(R.id.result_detail_container, fragment).commit();
        } else {
            // Start the detail activity in single pane mode.
            Intent detailIntent = new Intent(this, ResultDetailActivity.class);
            detailIntent.putExtra(ArticleDetailFragment.ARG_ITEM_ID, id);
            startActivity(detailIntent);
        }
    }

    private void setupToolbar() {
        final ActionBar ab = getActionBarToolbar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
    }

    private void setupDetailFragment() {
        ResultDetailFragment fragment =  ResultDetailFragment.newInstance(ResultContent.ITEMS.get(0).id);
        getFragmentManager().beginTransaction().replace(R.id.result_detail_container, fragment).commit();
    }

    /**
     * Enables the functionality that selected items are automatically highlighted.
     */
    private void enableActiveItemState() {
        ResultListFragment fragmentById = (ResultListFragment) getFragmentManager().findFragmentById(R.id.article_list);
        fragmentById.getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

    /**
     * Is the container present? If so, we are using the two-pane layout.
     *
     * @return true if the two pane layout is used.
     */
    private boolean isTwoPaneLayoutUsed() {
        return findViewById(R.id.result_detail_container) != null;
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
