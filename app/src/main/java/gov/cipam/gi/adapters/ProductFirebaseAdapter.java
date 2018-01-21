package gov.cipam.gi.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import gov.cipam.gi.model.Product;
import gov.cipam.gi.viewholder.ProductViewHolder;

/**
 * Created by karan on 11/26/2017.
 */

public class ProductFirebaseAdapter extends FirebaseRecyclerAdapter<Product, ProductViewHolder> {

    private DatabaseReference mRef;
    private Context mContext;
    private ChildEventListener mChildEventListener;


    public ProductFirebaseAdapter(Context context, Class<Product> modelClass, int modelLayout,
                                  Class<ProductViewHolder> viewHolderClass,
                                  Query ref, ChildEventListener childEventListener) {

        super(modelClass, modelLayout, viewHolderClass, ref);
        mRef = ref.getRef();
        mChildEventListener = childEventListener;
        mContext = context;
    }

    @Override
    protected void populateViewHolder(ProductViewHolder viewHolder, Product model, int position) {
        viewHolder.bindProductDetails(model);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void cleanup() {
        super.cleanup();
        mRef.removeEventListener(mChildEventListener);
    }

    @Override
    public void onCancelled(DatabaseError error) {
        super.onCancelled(error);
    }
}
