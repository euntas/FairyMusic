package com.music.fairy.fairymusic.ui.result;

import com.music.fairy.fairymusic.R;

import org.json.JSONObject;

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

    public static Map<String, String> MBTI = new HashMap<>(2);

    public static Map<String, FBResource> HOUSE = new HashMap<>(5);

    public static Map<String, FBResource> HTP = new HashMap<>(3);

    public static Map<Integer, ColorInfo> COLOR = new HashMap<>(10);

    public static String PERSON = "";

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

    public static void clearAllItem(){
        MBTI.clear();
        HTP.clear();
        COLOR.clear();
        HOUSE.clear();
    }

    public static class ResultItem {
        public final String id;
        public final int selectionNum;
        public String startDate;
        public String endDate;
        public int storyNum;

        public ResultItem(String id, int selectionNum) {
            this.id = id;
            this.selectionNum = selectionNum;
        }

        public ResultItem(String id, int selectionNum, String startDate, String endDate, int storyNum) {
            this.id = id;
            this.selectionNum = selectionNum;
            this.startDate = startDate;
            this.endDate = endDate;
            this.storyNum = storyNum;
        }
    }

    public static class ColorInfo{
        public String colorName;
        public String colorAnalysis;
        public int colorCount;

        public ColorInfo(String colorName, String colorAnalysis, int colorCount) {
            this.colorName = colorName;
            this.colorAnalysis = colorAnalysis;
            this.colorCount = colorCount;
        }
    }
}
