package gov.cipam.gi.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.core.models.Card;

import gov.cipam.gi.R;

/**
 * Created by Deepak on 11/18/2017.
 */

public class ImageViewPageAdapter extends PagerAdapter{
    private Context             mContext;
    private LayoutInflater      mLayoutInflater;
    setOnImageClickListener     imageClickListener;
    
    private int[] mResources = {
            R.drawable.image1,
            R.drawable.image2,
            R.drawable.image3,

    };

    public interface setOnImageClickListener{
        void onViewPagerClicked(View view, int position);
    }

    public ImageViewPageAdapter(Context context) {
        this.mContext = context;


    }

    @Override
    public int getCount() {
        return mResources.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View itemView = mLayoutInflater.inflate(R.layout.pager_item,container, false);
        ImageView imageView =itemView.findViewById(R.id.ImageView);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(itemView.getContext(), "position", Toast.LENGTH_SHORT).show();
            }
        });

        Picasso.with(mContext)
                .load(mResources[position])
                .into(imageView);
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((CardView) object);
    }

}
