package gov.cipam.gi.firebasemanager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import gov.cipam.gi.activities.HomePageActivity;
import gov.cipam.gi.common.SharedPref;
import gov.cipam.gi.model.Users;
import gov.cipam.gi.utils.Constants;

import static android.content.ContentValues.TAG;

/**
 * Created by Deepak on 12/15/2017.
 */

public class GoogleAuthentication {
    private Context mContext;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    public GoogleAuthentication(Context context) {
        this.mContext = context;
        mAuth = FirebaseAuth.getInstance();
    }


    // [START auth_with_google]
    public void firebaseAuthWithGoogle(final GoogleSignInAccount acct, final ProgressDialog progressDialog) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            Toast.makeText(mContext, "Authentication Success.",
                                    Toast.LENGTH_SHORT).show();
                            String UID = currentUser.getUid();

                            // create a database reference for the user
                            mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.KEY_USERS).child(UID);
                            mDatabase.child(Constants.KEY_CHILD_USERS).setValue(acct.getEmail());
                            mDatabase.child(Constants.KEY_CHILD_NAME).setValue(acct.getDisplayName());
                            //userEmailVerification();
                            Users user = new Users();
                            user.setEmail(acct.getEmail());
                            user.setName(acct.getDisplayName());
                            SharedPref.saveObjectToSharedPreference(mContext, Constants.KEY_USER_INFO, Constants.KEY_USER_DATA, user);
                            progressDialog.dismiss();
                            mContext.startActivity(new Intent(mContext, HomePageActivity.class));


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(mContext, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        progressDialog.dismiss();

    }

    // [END auth_with_google]
}
