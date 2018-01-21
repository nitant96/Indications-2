package gov.cipam.gi.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import de.hdodenhof.circleimageview.CircleImageView;
import gov.cipam.gi.R;
import gov.cipam.gi.firebasemanager.FirebaseAuthentication;
import gov.cipam.gi.firebasemanager.GoogleAuthentication;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by karan on 12/17/2017.
 */

public class SignInFragment extends Fragment implements View.OnClickListener {

    TextView mForgotPass;
    SignInButton googleSignInButton;
    private EditText mEmailField, mPassField;
    Button mSignInButton;
    CircleImageView imageView;
    ProgressDialog mProgressDialog;
    private FirebaseAuth mAuth;
    private String email, password;
    private static String TAG = "SignInActivity";
    GoogleSignInClient mGoogleSignInClient;
    GoogleAuthentication gAuth;
    private static final int RC_SIGN_IN = 9001;
    SharedPreferences mPrefs;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mPrefs = getActivity().getPreferences(MODE_PRIVATE);

        mProgressDialog = new ProgressDialog(getContext());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mForgotPass = view.findViewById(R.id.forgotPass);

        mEmailField = view.findViewById(R.id.signInEmailField);
        mPassField = view.findViewById(R.id.signInPassField);

        mSignInButton = view.findViewById(R.id.sign_in_button);
        googleSignInButton = view.findViewById(R.id.google_signin_button);

        imageView = view.findViewById(R.id.signin_image_view);

        imageView.setImageResource(R.drawable.image1);

        mSignInButton.setOnClickListener(this);
        mForgotPass.setOnClickListener(this);
        googleSignInButton.setOnClickListener(this);

    }

    private void forgotPassword() {
        email = mEmailField.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError(getString(R.string.email_error));
            Toast.makeText(getContext(), getString(R.string.toast_empty_email),
                    Toast.LENGTH_SHORT).show();
        } else {
            mEmailField.setError(getString(R.string.empty_email_error));
            mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getContext(), getString(R.string.toast_password_reset_email),
                                        Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "Email sent.");
                            }
                        }
                    });
        }
    }

    private void GoogleSignIn() {
        mProgressDialog.setMessage(getString(R.string.register_progress_dialog_title));
        mProgressDialog.show();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void startSignIn() {

        mProgressDialog.setTitle(getString(R.string.register_progress_dialog_title));
        mProgressDialog.setMessage(getString(R.string.logging_in));
        mProgressDialog.setCanceledOnTouchOutside(false);

        email = mEmailField.getText().toString().trim();
        password = mPassField.getText().toString().trim();
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            if (TextUtils.isEmpty(email)) {
                mEmailField.setError(getString(R.string.email_error));
            }
            if (TextUtils.isEmpty(password)) {
                mPassField.setError(getString(R.string.password_error));
            }

        } else {
            mProgressDialog.show();

            FirebaseAuthentication firebaseAuthentication = new FirebaseAuthentication(getContext());
            firebaseAuthentication.startSignIn(email, password, mProgressDialog);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                gAuth = new GoogleAuthentication(getContext());
                gAuth.firebaseAuthWithGoogle(account, mProgressDialog);

            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                Toast.makeText(getContext(), "failed." + e,
                        Toast.LENGTH_SHORT).show();
                mProgressDialog.dismiss();

            }
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.sign_in_button:
                startSignIn();
                break;

            case R.id.forgotPass:
                forgotPassword();
                break;

            case R.id.google_signin_button:
                GoogleSignIn();
                break;
        }
    }
}
