package gov.cipam.gi.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;


import gov.cipam.gi.R;
import gov.cipam.gi.model.Seller;

/**
 * Created by karan on 11/26/2017.
 */

public class SellerViewHolder extends RecyclerView.ViewHolder {
   private TextView textViewSellerName,textViewSellerAddress,textViewSellerContact;

    public SellerViewHolder(View itemView) {
        super(itemView);

        textViewSellerName =itemView.findViewById(R.id.card_seller_name);
        textViewSellerAddress =itemView.findViewById(R.id.card_seller_address);
        textViewSellerContact=itemView.findViewById(R.id.card_seller_contact);
    }

    public void bindSellerDetails(Seller seller){
        textViewSellerContact.setText(seller.getcontact());
        textViewSellerAddress.setText(seller.getaddress());
        textViewSellerName.setText(seller.getName());
    }
}
