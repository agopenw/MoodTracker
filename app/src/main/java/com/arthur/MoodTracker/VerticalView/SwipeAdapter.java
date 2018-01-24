//In this class, images,color backgrounds are defined in good order

package com.arthur.MoodTracker.VerticalView;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arthur.MoodTracker.R;

//Here, mood with background color
public class SwipeAdapter extends PagerAdapter {
    private int[] image_resources = {R.drawable.smiley_sad, R.drawable.smiley_disappointed, R.drawable.smiley_normal, R.drawable.smiley_happy, R.drawable.smiley_super_happy};
    private String[] background_color = {"#ffde3c50", "#ff9b9b9b", "#a5468ad9", "#ffb8e986", "#fff9ec4f"};
    private Context ctx;
    private LayoutInflater mLayoutInflater;

    public SwipeAdapter(Context ctx){
        this.ctx = ctx;
    }

    @Override
    public int getCount() {
        return image_resources.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view  == (RelativeLayout)object);
    }
    //InstantiateItem create a view for a given position
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        mLayoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item_view = mLayoutInflater.inflate(R.layout.swipe_layout, container, false);
        item_view.setBackgroundColor(Color.parseColor(background_color[position]));
        ImageView imageView = (ImageView) item_view.findViewById(R.id.image_view);
        imageView.setImageResource(image_resources[position]);
        TextView textView = (TextView) item_view.findViewById(R.id.text_test);
        textView.setText(background_color[position]);
        container.addView(item_view);
        return item_view;
    }
    //Destroy item unused
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout)object);
    }
}

