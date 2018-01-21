package gov.cipam.gi.activities;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import gov.cipam.gi.R;
import gov.cipam.gi.common.SharedPref;
import gov.cipam.gi.model.Users;
import gov.cipam.gi.utils.Constants;

public class AccountInfoActivity extends BaseActivity implements View.OnClickListener {
    private TextView                tvChangePass,tvUpdatePass;
    private TextInputLayout         mChangePassFieldLayout;
    private EditText                mNameField,mEmailField,mChangePassField;
    private String                  name,email;
    private DatabaseReference       mDatabaseUser;
    private static String           TAG = "AccountInfoActivity";
    private static String           user_id;
    private FirebaseAuth            mAuth;
    private Users                   user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_info);
        setUpToolbar(this);
        mDatabaseUser = FirebaseDatabase.getInstance().getReference(Constants.KEY_USERS);
        mAuth = FirebaseAuth.getInstance();

        mNameField =findViewById(R.id.nameField);
        mEmailField =findViewById(R.id.emailField);
        tvChangePass =findViewById(R.id.changePass);
        tvChangePass.setOnClickListener(this);


        }

    @Override
    protected int getToolbarID() {
        return R.id.account_info_toolbar;
    }

    @Override
    protected void onStart() {
        Users user = SharedPref.getSavedObjectFromPreference(AccountInfoActivity.this,Constants.KEY_USER_INFO,Constants.KEY_USER_DATA,Users.class);
        if(user!=null) {
            mEmailField.setText(user.getEmail());
            mNameField.setText(user.getName());

        }
        else {
            Toast.makeText(AccountInfoActivity.this,getString(R.string.toast_signin_again_request),Toast.LENGTH_LONG).show();
        }
        super.onStart();
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.changePass:
                changePasswordDialog();
                break;
        }
    }

    private void changePasswordDialog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(AccountInfoActivity.this);
        alertDialog.setTitle("Change Password");
        alertDialog.setMessage("Enter new password");

        final EditText newPass = new EditText(AccountInfoActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        newPass.setLayoutParams(lp);
        alertDialog.setView(newPass);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                final String newPassword = newPass.getText().toString();
                if (!TextUtils.isEmpty(newPassword)){
                    user.updatePassword(newPassword)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(AccountInfoActivity.this, getString(R.string.toast_password_change_success),
                                                Toast.LENGTH_SHORT).show();
                                        Log.d(TAG, "User password updated.");
                                    }
                                    else Toast.makeText(AccountInfoActivity.this, getString(R.string.toast_password_change_failed),
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                else newPass.setError("Enter Password");
            }
        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();

        switch (id){
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }
}
