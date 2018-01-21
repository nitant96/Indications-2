package gov.cipam.gi.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import gov.cipam.gi.R;
import gov.cipam.gi.fragments.ProductDetailFragment;
import gov.cipam.gi.model.Product;
import gov.cipam.gi.utils.Constants;

public class ProductDetailActivity extends BaseActivity {
    CollapsingToolbarLayout     collapsingToolbarLayout;
    ProductDetailFragment       productDetailFragment;
    ImageView                   imageView;
    ProgressBar                 progressBar;

    public static Product currentProduct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        setUpToolbar(this);

        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        currentProduct=(Product) bundle.getSerializable("ppp");

        collapsingToolbarLayout=findViewById(R.id.collapsing_toolbar);
        progressBar=findViewById(R.id.progressBarDetails);
        collapsingToolbarLayout.setTitleEnabled(false);
        mToolbar.setBackgroundColor(Color.TRANSPARENT);
        mToolbar.setTitle(currentProduct.getName());
        mToolbar.setSubtitle(currentProduct.getState());

        imageView=findViewById(R.id.details_image);
        loadImage(imageView);

        productDetailFragment=ProductDetailFragment.newInstance(currentProduct,null);
        if(savedInstanceState==null){
            fragmentInflate(productDetailFragment);
        }
    }

    @Override
    public void onAttachFragment(android.app.Fragment fragment) {
        super.onAttachFragment(fragment);

    }

    @Override
    protected int getToolbarID() {
        return R.id.detail_activity_toolbar;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_details_page, menu);
        return true;
    }

    private void loadImage(final ImageView imageView) {
        this.imageView=imageView;


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean image_download = sharedPreferences.getBoolean(Constants.KEY_DOWNLOAD_IMAGES, true);
        progressBar.setVisibility(View.VISIBLE);

        if(image_download){
            Picasso.with(this)
                    .load(currentProduct.getDpurl())
                    .fit()
                    .into(imageView, new Callback() {
                        @Override
                        public void onSuccess() {
                        progressBar.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onError() {

                        }
                    });
        }
        else
        {
            imageView.setImageResource(R.drawable.image_gradient);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void fragmentInflate(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.product_detail_frame_layout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id =item.getItemId();

        switch (id){

            /*case R.id.home:
                onBackPressed();
                break;
            case R.id.action_save_for_later:
                Toast.makeText(this,R.string.save_for_later,Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_url:
                Toast.makeText(this,R.string.url,Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, WebViewActivity.class));
                break;
            case R.id.action_location:
                Toast.makeText(this,R.string.open_in_map,Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this,MapsActivity.class));
                break;*/
        }
        return super.onOptionsItemSelected(item);
    }
}
