package gov.cipam.gi.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;

import gov.cipam.gi.R;

public class AboutActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setUpToolbar(this);
        mToolbar.setBackgroundColor(Color.TRANSPARENT);
        mToolbar.setTitle(getTitle());
    }

    @Override
    protected int getToolbarID() {
        return R.id.about_activity_toolbar;
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
