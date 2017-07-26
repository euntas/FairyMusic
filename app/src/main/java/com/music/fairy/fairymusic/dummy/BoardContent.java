package com.music.fairy.fairymusic.dummy;

import android.widget.TextView;

import com.music.fairy.fairymusic.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by KITA on 2017-07-24.
 * rrrr
 */

public class BoardContent {
    public static TextView textview;
    public static String jsontext;

    // An array of sample items.




    public static final List<BoardItem> ITEMS = new ArrayList<>();

    /*
            * A map of sample items. Key: sample ID; Value: Item.
     */
    public static final Map<String, BoardItem> ITEM_MAP = new HashMap<>();

    static {
        URL url = null;
        HttpURLConnection con = null;
        StringBuilder sb = new StringBuilder();
        JSONObject json = null;
        JSONArray jarray = null;
        JSONObject item = null;
        try {
            url = new URL("http://203.233.196.130:8888/fairybook/app/boardList"); //203.233.196.130
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            con = (HttpURLConnection) url.openConnection();
            if (con != null){
                con.setConnectTimeout(10000000);
                con.setUseCaches(false);
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type","application/json; charset=utf-8");
                con.setDoOutput(true);
                con.setDoInput(true);
                if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStreamReader in = new InputStreamReader(con.getInputStream(), "utf-8");
                    int ch;
                    while((ch = in.read()) != -1){
                        sb.append((char) ch);
                    }
                    in.close();
                    jsontext = sb.toString();
                    String jt = jsontext;
                    if (jt != null) {
                        try {
                            json = new JSONObject(jt);
                            jarray = json.getJSONArray("boardList");
                            for (int i = 0; i < jarray.length(); i++) {
                                item = jarray.getJSONObject(i);
                                BoardItem bi = new BoardItem(item.getInt("boardnum"), R.drawable.ryan1, item.getString("id"), item.getString("title")
                                        , item.getString("content"), item.getString("inputdate"),item.getInt("hit"));
                                System.out.println(bi.toString());
                                addItem(bi);
                            }
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

    private static void addItem(BoardItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    public static class BoardItem {
        public final int boardnum;
        public final int photoId;
        public final String id;
        public final String title;
        public final String content;
        public final String inputdate;
        public final int hit;

        public BoardItem(int boardnum, int photoId, String id, String title,  String content, String inputdate, int hit) {
            this.boardnum = boardnum;
            this.photoId = photoId;
            this.id = id;
            this.title = title;
            this.content = content;
            this.inputdate = inputdate;
            this.hit = hit;
        }
    }
}
