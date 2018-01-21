package gov.cipam.gi.fragments;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Vibrator;
import android.widget.ScrollView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import gov.cipam.gi.R;
import gov.cipam.gi.activities.ProductListActivity;

import gov.cipam.gi.adapters.CategoryAdapter;
import gov.cipam.gi.adapters.StatesAdapter;
import gov.cipam.gi.common.DatabaseFetch;
import gov.cipam.gi.database.Database;
import gov.cipam.gi.model.Categories;
import gov.cipam.gi.model.States;
import gov.cipam.gi.utils.RecyclerViewClickListener;
import gov.cipam.gi.utils.StartSnapHelper;

/**
 * Created by karan on 11/20/2017.
 */

public class HomePageFragment extends Fragment implements RecyclerViewClickListener, CategoryAdapter.setOnCategoryClickListener, StatesAdapter.setOnStateClickedListener {

    RecyclerView                    rvState,rvCategory,autoScrollViewPager;
    ScrollView                      scrollView;
    FirebaseAuth                    mAuth;
    DatabaseReference               mDatabaseState,mDatabaseCategory;
    StartSnapHelper                 startSnapHelper,startSnapHelper1,startSnapHelper2;
    private DatabaseFetch           databaseFetch;
    public static ArrayList<States> mDisplayStateList=new ArrayList<>();
    public static ArrayList<Categories>  mDisplayCategoryList=new ArrayList<>();

    public static HomePageFragment newInstance() {

        Bundle args = new Bundle();

        HomePageFragment fragment = new HomePageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_homepage, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mAuth = FirebaseAuth.getInstance();

        databaseFetch=new DatabaseFetch();
        databaseFetch.populateDisplayListFromDB(getContext());

        mDatabaseState = FirebaseDatabase.getInstance().getReference("States");
        mDatabaseCategory = FirebaseDatabase.getInstance().getReference("Categories");
        startSnapHelper=new StartSnapHelper();
        startSnapHelper1=new StartSnapHelper();
        startSnapHelper2=new StartSnapHelper();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        rvState =  view.findViewById(R.id.recycler_states);
        rvCategory =  view.findViewById(R.id.recycler_categories);
        autoScrollViewPager = view.findViewById(R.id.viewpager);
        scrollView=view.findViewById(R.id.scroll_view_home);
        scrollView.setSmoothScrollingEnabled(true);
        rvState.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        rvCategory.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        autoScrollViewPager.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));

        startSnapHelper.attachToRecyclerView(rvState);
        startSnapHelper1.attachToRecyclerView(rvCategory);
        startSnapHelper2.attachToRecyclerView(autoScrollViewPager);
        rvState.setAdapter(new StatesAdapter(this,mDisplayStateList,getContext()));
        rvCategory.setAdapter(new CategoryAdapter(mDisplayCategoryList,getContext(),this));
        autoScrollViewPager.setAdapter(new CategoryAdapter(mDisplayCategoryList,getContext(),this));
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
        setRetainInstance(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        setRetainInstance(true);
    }


    @Override
    public void onClick(View view, int position) {
        startActivity(new Intent(getContext(), ProductListActivity.class));
    }


    @Override
    public void onLongClick(View view, int position) {
        Vibrator vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(100);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();

        switch (id){
            case R.id.action_settings:
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCategoryClicked(View view, int position) {
        Intent intent=new Intent(getContext(),ProductListActivity.class);
        intent.putExtra("type", Database.GI_CATEGORY);
        intent.putExtra("value",mDisplayCategoryList.get(position).getName());
        startActivity(intent);
    }

    @Override
    public void onStateClickedListener(View view, int position) {
        Intent intent=new Intent(getContext(),ProductListActivity.class);
        intent.putExtra("type",Database.GI_STATE);
        intent.putExtra("value",mDisplayStateList.get(position).getName());
        startActivity(intent);
    }
}
