package gov.cipam.gi.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import de.hdodenhof.circleimageview.CircleImageView;
import gov.cipam.gi.R;
import gov.cipam.gi.utils.Constants;

public class NewUserActivity extends AppCompatActivity implements View.OnClickListener{

    CircleImageView     appLogoImage;
    Button              skipLoginButton,registerUserButton;
    FirebaseAuth        mAuth;
    int                 width,height;
    float               scaledDensity,xdpi,ydpi,densityDpi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        launchActivity();
        mAuth = FirebaseAuth.getInstance();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        DisplayMetrics metrics= Resources.getSystem().getDisplayMetrics();
        width= metrics.widthPixels;
        height=metrics.heightPixels;
        scaledDensity=metrics.scaledDensity;
        densityDpi=metrics.densityDpi;
        xdpi=metrics.xdpi;
        ydpi=metrics.ydpi;


        appLogoImage=findViewById(R.id.appLogoImage);

        skipLoginButton=findViewById(R.id.skipLogin);
        registerUserButton=findViewById(R.id.registerUser);

        appLogoImage.setImageResource(R.drawable.image1);

        skipLoginButton.setOnClickListener(this);
        registerUserButton.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(this, HomePageActivity.class));
            finish();
        }
    }

    public void launchActivity() {
        SharedPreferences preferences =
                getSharedPreferences(Constants.MY_PREFERENCES, MODE_PRIVATE);

        if (!preferences.getBoolean(Constants.ONBOARDING_COMPLETE, false)) {

            startActivity(new Intent(this, IntroActivity.class));
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.skipLogin:
                Toast.makeText(this,"Screen width= "+String.valueOf(width)+"\n"+"Screen Height= "+String.valueOf(height)+"\n"+
                        "Xdpi= "+String.valueOf(xdpi)+"\n"+"Ydpi="+String.valueOf(ydpi)+"\n"+"Density dpi="+String.valueOf(densityDpi)+
                        "\n"+"Density= "+String.valueOf(scaledDensity),Toast.LENGTH_LONG).show();
                break;

            case R.id.registerUser:
                startActivity(new Intent(this,LoginActivity.class));
                finish();
                break;
        }
    }
}
