package gov.cipam.gi.fragments;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import gov.cipam.gi.R;
import gov.cipam.gi.activities.ProductListActivity;
import gov.cipam.gi.adapters.ProductListAdapter;
import gov.cipam.gi.utils.DetailsTransition;

/**
 * Created by karan on 12/14/2017.
 */

public class ProductListFragment extends Fragment implements  ProductListAdapter.setOnProductClickedListener {

    RecyclerView productListRecycler;
    ProductListAdapter productListAdapter;

    public static ProductListFragment newInstance() {

        Bundle args = new Bundle();

        ProductListFragment fragment;
        fragment = new ProductListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product_list,container,false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
//
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        productListRecycler=view.findViewById(R.id.product_list_recycler_view);
        productListAdapter=new ProductListAdapter(ProductListActivity.subGIList,getContext(),this);
        productListRecycler.setAdapter(productListAdapter);

        onConfigurationChanged(new Configuration());
        //productListRecycler.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        //productListRecycler.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
    }

    /*@Override
    public void onProductClicked(View view, int position) {
        Product clickedProduct=ProductListActivity.subGIList.get(position);
        Bundle bundle=new Bundle();
        bundle.putSerializable("clickedProduct",clickedProduct);
        Intent intent=new Intent(getContext(),ProductDetailActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);

    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();

        switch (id){
            case android.R.id.home:
                getActivity().onBackPressed();
            //onCreateDialog(productListRecycler);
        }
        return true;

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem urlOption=menu.findItem(R.id.action_url);
        urlOption.setVisible(false);
        MenuItem locationAction=menu.findItem(R.id.action_location);
        locationAction.setVisible(false);
    }


    @Override
    public void onProductClicked(ProductListAdapter.ProductViewHolder holder, int position) {
        boolean isImageLoaded=false;
        Bitmap bitmap=null;
        BitmapDrawable drawable = (BitmapDrawable) holder.imageView.getDrawable();
        if(drawable!=null) {
            isImageLoaded=true;
            bitmap = drawable.getBitmap();
        }
        ProductDetailFragment productDetailFragment=ProductDetailFragment.newInstance(ProductListActivity.subGIList.get(position),bitmap);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                productDetailFragment.setSharedElementEnterTransition(new DetailsTransition());
                productDetailFragment.setEnterTransition(new Fade());
                this.setSharedElementEnterTransition(new DetailsTransition());
                this.setEnterTransition(new Fade());
            }

        ProductListFragment productListFragment=new ProductListFragment();
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .addSharedElement(holder.imageView, "commonImage")
                .replace(R.id.product_list_frame_layout, productDetailFragment)
                .addToBackStack(productListFragment.getClass().getName())
                .commit();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Configuration c=getContext().getResources().getConfiguration();

        if(c.orientation==Configuration.ORIENTATION_PORTRAIT){
            productListRecycler.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        }
        else if(c.orientation==Configuration.ORIENTATION_LANDSCAPE){
            productListRecycler.setLayoutManager(new GridLayoutManager(getContext(),2));
        }
    }

}
