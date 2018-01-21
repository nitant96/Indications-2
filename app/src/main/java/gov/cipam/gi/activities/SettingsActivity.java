package gov.cipam.gi.activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.MenuItem;

import gov.cipam.gi.R;
import gov.cipam.gi.fragments.SettingsFragment;

public class SettingsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setUpToolbar(this);
        inflateSettingsFragment();
    }

    @Override
    protected int getToolbarID() {
        return R.id.settings_toolbar;
    }

    public void inflateSettingsFragment(){
        FragmentManager mFragmentManager = getFragmentManager();
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        SettingsFragment mPrefsFragment = SettingsFragment.newInstance();
        mFragmentTransaction.replace(R.id.settings_frame, mPrefsFragment);
        mFragmentTransaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }
}
