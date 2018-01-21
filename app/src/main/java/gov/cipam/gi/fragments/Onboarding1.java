package gov.cipam.gi.fragments;

import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.github.paolorotolo.appintro.ISlideBackgroundColorHolder;

import gov.cipam.gi.R;

/**
 * Created by karan on 11/10/2017.
 */

public class Onboarding1 extends Fragment implements ISlideBackgroundColorHolder {
    LinearLayout layoutContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_onboarding_screen1, container, false);
        layoutContainer= view.findViewById(R.id.onboarding_1_linear_layout);
        return view;
        }

    @Override
    public int getDefaultBackgroundColor() {
        return ContextCompat.getColor(getContext(),R.color.md_blue_700);
    }

    @Override
    public void setBackgroundColor(@ColorInt int backgroundColor) {
        if (layoutContainer != null) {
            layoutContainer.setBackgroundColor(backgroundColor);
        }
    }
}
