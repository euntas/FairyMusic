package com.music.fairy.fairymusic.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.music.fairy.fairymusic.R;

/**
 * Just dummy content. Nothing special.
 *
 * Created by Andreas Schrade on 14.12.2015.
 */
public class DummyContent {

    /**
     * An array of sample items.
     */
    public static String LOGIN_ID = null;

    public static int STORYNUM = 1;

    public static String STORYDATE = null;

    public static final List<DummyItem> ITEMS = new ArrayList<>();

    /**
     * A map of sample items. Key: sample ID; Value: Item.
     */
    public static final Map<String, DummyItem> ITEM_MAP = new HashMap<>(5);

    static {
        addItem(new DummyItem("1", R.drawable.ryan1, "글 제목", "우리 엄마", "너네 자식들은 얼마나 잘났냐"));
        addItem(new DummyItem("2", R.drawable.ryan1, "글 제목", "친구 엄마","내 친구 아들은 성적이 어쩌고"));
        addItem(new DummyItem("3", R.drawable.ryan1, "글 제목", "너네 어머니", "패드립 아닙니다. 오해마세요"));
        addItem(new DummyItem("4", R.drawable.ryan1, "글 제목", "아빠도 있다","엄마만 애 키우냐"));
    }

    private static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    public static class DummyItem {
        public final String id;
        public final int photoId;
        public final String title;
        public final String author;
        public final String content;

        public DummyItem(String id, int photoId, String title, String author, String content) {
            this.id = id;
            this.photoId = photoId;
            this.title = title;
            this.author = author;
            this.content = content;
        }
    }
}
