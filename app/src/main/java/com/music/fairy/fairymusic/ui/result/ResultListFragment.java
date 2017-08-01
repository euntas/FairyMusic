package com.music.fairy.fairymusic.ui.result;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.music.fairy.fairymusic.R;
import com.music.fairy.fairymusic.ui.result.ResultContent;

/**
 * Shows a list of all available quotes.
 * <p/>
 * Created by Andreas Schrade on 14.12.2015.
 */
public class ResultListFragment extends ListFragment {

    private Callback callback = dummyCallback;

    /**
     * A callback interface. Called whenever a item has been selected.
     */
    public interface Callback {
        void onItemSelected(String id);
    }

    /**
     * A dummy no-op implementation of the Callback interface. Only used when no active Activity is present.
     */
    private static final Callback dummyCallback = new Callback() {
        @Override
        public void onItemSelected(String id) {
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListAdapter(new MyListAdapter());
        setHasOptionsMenu(true);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        // notify callback about the selected list item
        callback.onItemSelected(ResultContent.ITEMS.get(position).id);
    }

    /**
     * onAttach(Context) is not called on pre API 23 versions of Android.
     * onAttach(Activity) is deprecated but still necessary on older devices.
     */
    @TargetApi(23)
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onAttachToContext(context);
    }

    /**
     * Deprecated on API 23 but still necessary for pre API 23 devices.
     */
    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            onAttachToContext(activity);
        }
    }

    /**
     * Called when the fragment attaches to the context
     */
    protected void onAttachToContext(Context context) {
        if (!(context instanceof Callback)) {
            throw new IllegalStateException("Activity must implement callback interface.");
        }

        callback = (Callback) context;
    }

    private class MyListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return ResultContent.ITEMS.size();
        }

        @Override
        public Object getItem(int position) {
            return ResultContent.ITEMS.get(position);
        }

        @Override
        public long getItemId(int position) {
            return ResultContent.ITEMS.get(position).id.hashCode();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup container) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.result_item_article, container, false);
            }

            final ResultContent.ResultItem item = (ResultContent.ResultItem) getItem(position);
            if(item.storyNum == 1){
                ((TextView) convertView.findViewById(R.id.result_title)).setText( "헨젤과 그레텔 - " + String.valueOf(item.selectionNum) + "회");
                ((TextView) convertView.findViewById(R.id.result_subtitle)).setText(item.startDate + " ~ " + item.endDate);
            }

            else if(item.storyNum == 2){
                ((TextView) convertView.findViewById(R.id.result_title)).setText( "백설공주 - " + String.valueOf(item.selectionNum) + "회");
                ((TextView) convertView.findViewById(R.id.result_subtitle)).setText(item.id);
            }

            else{
                ((TextView) convertView.findViewById(R.id.result_title)).setText(String.valueOf(item.selectionNum));
                ((TextView) convertView.findViewById(R.id.result_subtitle)).setText(item.id);
            }

            final ImageView img = (ImageView) convertView.findViewById(R.id.result_thumbnail);
            Glide.with(getActivity()).load(R.drawable.ryan1).asBitmap().fitCenter().into(new BitmapImageViewTarget(img) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(getActivity().getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    img.setImageDrawable(circularBitmapDrawable);
                }
            });

            return convertView;
        }
    }

    public ResultListFragment() {
    }
}
