package gov.cipam.gi.adapters;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import gov.cipam.gi.R;
import gov.cipam.gi.model.Product;

/**
 * Created by NITANT SOOD on 29-11-2017.
 */

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ProductViewHolder> {

    private ArrayList<Product>          GIList;
    private Context                     mContext;
    private setOnProductClickedListener mListener;
    View mItemView;

    public ProductListAdapter(ArrayList<Product> GIList, Context mContext, setOnProductClickedListener mListener) {
        this.GIList = GIList;
        this.mContext = mContext;
        this.mListener = mListener;
    }

    public interface setOnProductClickedListener{
        void onProductClicked(ProductListAdapter.ProductViewHolder view, int position);
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.card_view_product_list,parent,false);
        return  new ProductListAdapter.ProductViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        ViewCompat.setTransitionName(holder.itemView.findViewById(R.id.productListImage),String.valueOf(position+"_image"));
        holder.mTitle.setText(GIList.get(position).getName());
        holder.mFiller.setText(GIList.get(position).getDetail());
        holder.mState.setText(GIList.get(position).getState());
        holder.mCategory.setText(GIList.get(position).getCategory());

        Picasso.with(mContext)
                .load(GIList.get(position).getDpurl())
                .fit()
//                .placeholder(R.drawable.image)
                .into(holder.imageView);
        ViewCompat.setTransitionName(holder.imageView,GIList.get(position).getUid());
    }

    @Override
    public int getItemCount() {
        return GIList.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView        mTitle,mFiller,mState,mCategory;
        public ImageView       imageView;
        private RelativeLayout  relativeLayout;

        private ProductViewHolder(View itemView) {
            super(itemView);
            mItemView=itemView;
            relativeLayout=itemView.findViewById(R.id.relativeLayoutProduct);
            mTitle=itemView.findViewById(R.id.productListTitle);
            mFiller=itemView.findViewById(R.id.productListDesc);
            mState=itemView.findViewById(R.id.productListStateName);
            mCategory=itemView.findViewById(R.id.productListCategoryName);
            imageView=itemView.findViewById(R.id.productListImage);
            relativeLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onProductClicked(this,getAdapterPosition());
        }
    }

}
