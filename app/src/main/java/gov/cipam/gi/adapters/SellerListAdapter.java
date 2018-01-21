package gov.cipam.gi.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import gov.cipam.gi.R;
import gov.cipam.gi.model.Seller;

/**
 * Created by NITANT SOOD on 24-12-2017.
 */

public class SellerListAdapter extends RecyclerView.Adapter<SellerListAdapter.SellerViewHolder> {

    Context mContext;
    ArrayList<Seller> mSellerList;
    setOnSellerClickListener mListener;

    public SellerListAdapter(Context mContext, ArrayList<Seller> mSellerList, setOnSellerClickListener mListener) {
        this.mContext = mContext;
        this.mSellerList = mSellerList;
        this.mListener = mListener;
    }

    public interface setOnSellerClickListener{
        void onSellerClicked(View v,int Position);
    }
    @Override
    public SellerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.card_view_seller_item,parent,false);
        return  new SellerListAdapter.SellerViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(SellerViewHolder holder, int position) {
        holder.textViewSellerContact.setText(mSellerList.get(position).getcontact());
      holder.textViewSellerAddress.setText(mSellerList.get(position).getaddress());
      holder.textViewSellerName.setText(mSellerList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mSellerList.size();
    }

    public class SellerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView textViewSellerName,textViewSellerAddress,textViewSellerContact;

        public SellerViewHolder(View itemView) {
            super(itemView);

            textViewSellerName =itemView.findViewById(R.id.card_seller_name);
            textViewSellerAddress =itemView.findViewById(R.id.card_seller_address);
            textViewSellerContact=itemView.findViewById(R.id.card_seller_contact);
            textViewSellerContact.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onSellerClicked(v,getAdapterPosition());
        }
    }

}
