package gov.cipam.gi.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;
import gov.cipam.gi.R;
import gov.cipam.gi.firebasemanager.FirebaseAuthentication;
import gov.cipam.gi.utils.CommonUtils;
import gov.cipam.gi.utils.Constants;

/**
 * Created by karan on 12/17/2017.
 */

public class SignUpFragment extends Fragment implements View.OnClickListener {

    private EditText mEmailField, mPassField, mNameField;
    private TextView mLoginTextView;
    Button mSignupButton;
    ProgressDialog mProgressDialog;
    FirebaseAuth mAuth;
    CircleImageView imageView;
    private String email, password, name;
    private static String TAG = "Create Account";
    private DatabaseReference mUsersDatabase, mUserExists;
    private ProgressDialog mProgress;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        mUsersDatabase = FirebaseDatabase.getInstance().getReference(Constants.KEY_USERS);

        mProgressDialog = new ProgressDialog(getContext());

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mEmailField = view.findViewById(R.id.signUpEmailField);
        mPassField = view.findViewById(R.id.signUpPassField);
        mNameField = view.findViewById(R.id.nameField);

        mSignupButton = view.findViewById(R.id.sign_up_button);

        imageView = view.findViewById(R.id.ImageViewSignUp);

        imageView.setImageResource(R.drawable.image1);

        mSignupButton.setOnClickListener(this);

    }

    private void signUp() {

        mProgressDialog.setTitle(R.string.register_progress_dialog_title);
        mProgressDialog.setMessage(String.valueOf(R.string.register_progress_dialog_message));
        mProgressDialog.setCanceledOnTouchOutside(false);

        email = mEmailField.getText().toString().trim();
        password = mPassField.getText().toString().trim();
        name = mNameField.getText().toString().trim();

        if (CommonUtils.isEmailValid(email, getActivity())) {
            mEmailField.setError(getString(R.string.email_error));
        }
        if (CommonUtils.isPasswordValid(password, getActivity())) {
            mPassField.setError(getString(R.string.password_error));
        }
        if (TextUtils.isEmpty(name)) {
            mNameField.setError(getString(R.string.name_error));
        } else {
            mProgressDialog.show();
            // register user to firebase authentication
            // store name and email to firebase database
            FirebaseAuthentication firebaseAuthentication = new FirebaseAuthentication(getContext());
            firebaseAuthentication.startSignUp(email, password, name, mProgressDialog);
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.sign_up_button:
                signUp();
                break;
        }
    }
}
