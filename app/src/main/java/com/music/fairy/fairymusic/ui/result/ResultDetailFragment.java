package com.music.fairy.fairymusic.ui.result;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.music.fairy.fairymusic.R;
import com.music.fairy.fairymusic.dummy.DummyContent;
import com.music.fairy.fairymusic.ui.base.BaseActivity;
import com.music.fairy.fairymusic.ui.base.BaseFragment;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import butterknife.Bind;

/**
 * Shows the quote detail page.
 *
 * Created by Andreas Schrade on 14.12.2015.
 */
public class ResultDetailFragment extends BaseFragment implements OnChartValueSelectedListener{

    /**
     * The argument represents the dummy item ID of this fragment.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content of this fragment.
     */
    private ResultContent.ResultItem resultItem;

    @Bind(R.id.chart_color)
    PieChart pieChart;

    @Bind(R.id.quote)
    TextView quote;

    @Bind(R.id.author)
    TextView author;

    @Bind(R.id.mbti_title)
    TextView mbti_title;

    @Bind(R.id.mbti_content)
    TextView mbti_content;

    @Bind(R.id.htp_tree_title)
    TextView htp_tree_title;

    @Bind(R.id.htp_tree_content)
    TextView htp_tree_content;

    @Bind(R.id.img_tree)
    ImageView img_tree;

    @Bind(R.id.htp_house_title)
    TextView htp_house_title;

    @Bind(R.id.htp_house_content)
    TextView htp_house_content;

    @Bind(R.id.backdrop)
    ImageView backdropImg;

    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // load dummy item by using the passed item ID.
            resultItem = ResultContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
        }

        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflateAndBind(inflater, container, R.layout.fragment_result_detail);

        if (!((BaseActivity) getActivity()).providesActivityToolbar()) {
            // No Toolbar present. Set include_toolbar:
            ((BaseActivity) getActivity()).setToolbar((Toolbar) rootView.findViewById(R.id.toolbar));
        }

        if (resultItem != null) {
            loadBackdrop();
            collapsingToolbar.setTitle("셀넘:" + String.valueOf(resultItem.selectionNum));

            setColorResult();

            setMBTIResult();

            setHTPResult();
        }

        return rootView;
    }

    public void setColorResult(){

        ArrayList<Entry> entries = new ArrayList<>();
        // entries.add(new Entry(4f, 0));
        for(int i=0; i<ResultContent.COLOR.size(); i++){
            entries.add(new Entry(ResultContent.COLOR.get(i).colorCount, i, ResultContent.COLOR.get(i).colorName));
        }


        PieDataSet dataset = new PieDataSet(entries, "# of Calls");

        ArrayList<String> labels = new ArrayList<String>();
        // labels.add("January");
        for(int i=0; i<ResultContent.COLOR.size(); i++){
            labels.add(ResultContent.COLOR.get(i).colorName);
        }


        PieData data = new PieData(labels, dataset);
        dataset.setColors(ColorTemplate.COLORFUL_COLORS);

//        pieChart.setTransparentCircleRadius(24f);
        //       pieChart.setHoleRadius(24f);
        pieChart.setDescription("Description");
        pieChart.setData(data);

        pieChart.animateY(5000);


        pieChart.setOnChartValueSelectedListener(this);

        author.setText("색채검사");
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        for(int i=0; i<ResultContent.COLOR.size(); i++){
            if( e.getData().toString().equals(ResultContent.COLOR.get(i).colorName)){
                quote.setText(ResultContent.COLOR.get(i).colorName + " : " + ResultContent.COLOR.get(i).colorAnalysis);
            }
        }
    }

    @Override
    public void onNothingSelected() {
        Log.i("chart", "nothing");
    }

    public void setMBTIResult(){
        mbti_title.setText("MBTI 결과");
        String mbtiType = ResultContent.MBTI.get("mbtiType").toString();
        String mbtiAnalysis = ResultContent.MBTI.get("mbtiAnalysis").toString();
        mbti_content.setText(mbtiType + " : " + mbtiAnalysis);
    }

    public void setHTPResult(){
        htp_tree_title.setText("HTP - 나무 결과");
        htp_tree_content.setText(ResultContent.HTP.get("htpTree").getAnalysis());

        switch (ResultContent.HTP.get("htpTree").getName()){
            case "yard01":
                img_tree.setImageResource(R.drawable.yard01);
                break;
            case "yard02":
                img_tree.setImageResource(R.drawable.yard02);
                break;
            case "yard03":
                img_tree.setImageResource(R.drawable.yard03);
                break;
            case "yard04":
                img_tree.setImageResource(R.drawable.yard04);
                break;
            case "yard05":
                img_tree.setImageResource(R.drawable.yard05);
                break;
            case "yard06":
                img_tree.setImageResource(R.drawable.yard06);
                break;
            case "yard07":
                img_tree.setImageResource(R.drawable.yard07);
                break;
            case "yard08":
                img_tree.setImageResource(R.drawable.yard08);
                break;
        }

    }

    private void loadBackdrop() {
        Glide.with(this).load(R.drawable.ryan1).centerCrop().into(backdropImg);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.sample_actions, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // your logic
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static ResultDetailFragment newInstance(String itemID) {
        ResultDetailFragment fragment = new ResultDetailFragment();
        Bundle args = new Bundle();
        args.putString(ResultDetailFragment.ARG_ITEM_ID, itemID);
        fragment.setArguments(args);
        return fragment;
    }

    public ResultDetailFragment() {}
}
