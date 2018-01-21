package gov.cipam.gi.firebasemanager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import gov.cipam.gi.R;
import gov.cipam.gi.activities.HomePageActivity;
import gov.cipam.gi.common.SharedPref;
import gov.cipam.gi.model.Users;
import gov.cipam.gi.utils.Constants;

import static android.content.ContentValues.TAG;

/**
 * Created by karan on 11/22/2017.
 */

public class FirebaseAuthentication {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase, mUserExists;
    private Context mContext;

    public FirebaseAuthentication(Context context) {
        this.mContext = context;
        mAuth = FirebaseAuth.getInstance();
    }

    public void startSignUp(final String email, final String password, final String name, final ProgressDialog progressDialog) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            Toast.makeText(mContext, mContext.getString(R.string.toast_registration_success),
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            String UID = currentUser.getUid();

                            // create a database reference for the user
                            mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.KEY_USERS).child(UID);
                            mDatabase.child(Constants.KEY_CHILD_USERS).setValue(email);
                            mDatabase.child(Constants.KEY_CHILD_NAME).setValue(name);
                            //userEmailVerification();
                            Users user = new Users();
                            user.setEmail(email);
                            user.setName(name);
                            SharedPref.saveObjectToSharedPreference(mContext, Constants.KEY_USER_INFO, Constants.KEY_USER_DATA, user);
                            progressDialog.dismiss();

                            mContext.startActivity(new Intent(mContext, HomePageActivity.class));
                            ((Activity) mContext).finish();

                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(mContext, mContext.getString(R.string.toast_signup_failure),
                                    Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                });
    }


    public void startSignIn(final String email, final String password, final ProgressDialog progressDialog) {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Log.d(TAG, "signInWithEmail:success");
                            Toast.makeText(mContext, mContext.getString(R.string.toast_signin_success),
                                    Toast.LENGTH_SHORT).show();

                            String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.KEY_USERS).child(user_id);
                            DatabaseReference current_user = mDatabase;
                            current_user.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Users user = dataSnapshot.getValue(Users.class);
                                    SharedPref.saveObjectToSharedPreference(mContext, Constants.KEY_USER_INFO, Constants.KEY_USER_DATA, user);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            progressDialog.dismiss();
                            mContext.startActivity(new Intent(mContext, HomePageActivity.class));
                            ((Activity) mContext).finish();


                        } else {

                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(mContext, mContext.getString(R.string.toast_signin_failure),
                                    Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }

                    }
                });
    }

    /*public void userEmailVerification(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(mContext, mContext.mContext.getString(R.string.toast_verification_email_sent),
                                    Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Email sent.");
                        }
                    }
                });
    }*/
}


