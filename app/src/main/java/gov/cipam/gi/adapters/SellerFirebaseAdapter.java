package gov.cipam.gi.adapters;

import android.content.Context;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import gov.cipam.gi.model.Seller;
import gov.cipam.gi.viewholder.SellerViewHolder;

/**
 * Created by karan on 11/26/2017.
 */
public class SellerFirebaseAdapter extends FirebaseRecyclerAdapter<Seller, SellerViewHolder> {

    DatabaseReference mRef;
    Context mContext;

    public SellerFirebaseAdapter(Context context, Class<Seller> modelClass, int modelLayout,
                                 Class<SellerViewHolder> viewHolderClass,
                                 Query ref) {

        super(modelClass, modelLayout, viewHolderClass, ref);

        mRef = ref.getRef();
        mContext = context;
    }

    @Override
    protected void populateViewHolder(SellerViewHolder viewHolder, Seller model, int position) {
        viewHolder.bindSellerDetails(model);
    }
}