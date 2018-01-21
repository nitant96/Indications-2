package gov.cipam.gi.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.WindowManager;


import gov.cipam.gi.R;
import gov.cipam.gi.adapters.LoginViewPagerAdapter;
import gov.cipam.gi.fragments.SignInFragment;
import gov.cipam.gi.fragments.SignUpFragment;

public class LoginActivity extends BaseActivity {
    SignInFragment         signInFragment;
    SignUpFragment         signUpFragment;
    ViewPager              viewPager;
    TabLayout              tabLayout;
    LoginViewPagerAdapter  loginViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setUpToolbar(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        signInFragment = new SignInFragment();
        signUpFragment = new SignUpFragment();
        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setBackgroundColor(Color.TRANSPARENT);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected int getToolbarID() {
        return R.id.login_activity_toolbar;
    }

    private void setupViewPager(ViewPager viewPager) {

        loginViewPagerAdapter = new LoginViewPagerAdapter(getSupportFragmentManager());
        loginViewPagerAdapter.addFragment(signInFragment, "Sign In");
        loginViewPagerAdapter.addFragment(signUpFragment, "Sign Up");
        viewPager.setAdapter(loginViewPagerAdapter);
    }
}

