package com.music.fairy.fairymusic.ui.result;

import com.music.fairy.fairymusic.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Just dummy content. Nothing special.
 *
 * Created by Andreas Schrade on 14.12.2015.
 */
public class ResultContent {

    /**
     * An array of sample items.
     */
    public static final List<ResultItem> ITEMS = new ArrayList<>();

    /**
     * A map of sample items. Key: sample ID; Value: Item.
     */
    public static final Map<String, ResultItem> ITEM_MAP = new HashMap<>(2);

    static {
        addItem(new ResultItem("1", 621));
        addItem(new ResultItem("2", 496));
    }

    public static void addItem(ResultItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    public static void clearItem(){
        ITEMS.clear();
        ITEM_MAP.clear();
    }

    public static class ResultItem {
        public final String id;
        public final int selectionNum;

        public ResultItem(String id, int selectionNum) {
            this.id = id;
            this.selectionNum = selectionNum;
        }
    }
}
