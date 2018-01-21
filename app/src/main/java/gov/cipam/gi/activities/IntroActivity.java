package gov.cipam.gi.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.WindowManager;

import com.github.paolorotolo.appintro.AppIntro;

import gov.cipam.gi.R;
import gov.cipam.gi.fragments.Onboarding1;
import gov.cipam.gi.fragments.Onboarding2;
import gov.cipam.gi.utils.Constants;
import gov.cipam.gi.utils.downloadThread;

public class IntroActivity extends AppIntro {
downloadThread downloadThread1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        downloadThread1=new downloadThread(this);
        addSlide(new Onboarding1());
        addSlide(new Onboarding2());
        setSpecs();
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        finishOnboarding();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        finishOnboarding();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }

    public void finishOnboarding() {
        SharedPreferences preferences =
                getSharedPreferences(Constants.MY_PREFERENCES, MODE_PRIVATE);

        preferences.edit()
                .putBoolean(Constants.ONBOARDING_COMPLETE,true).apply();

        startActivity(new Intent(this, NewUserActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void setSpecs(){
        setBarColor(Color.TRANSPARENT);
        setSeparatorColor(Color.TRANSPARENT);
        setImageNextButton(ContextCompat.getDrawable(getApplicationContext(),R.drawable.next));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setColorTransitionsEnabled(true);
        // Hide Skip/Done button.
        showSkipButton(true);
        // Turn vibration on and set intensity.
        // NOTE: you will probably need to ask VIBRATE permission in Manifest.
        setVibrate(true);
        setVibrateIntensity(40);
        }
    }
