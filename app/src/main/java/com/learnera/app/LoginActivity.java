package com.learnera.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.learnera.app.data.User;

import butterknife.BindView;
import butterknife.OnClick;
/** EDIT ALL ACTIVITIES TO TAKE SPECIFIC
// preference file. Make global function to avoid implement login check. Check if username is correct on login
// screen itself. Use show password toggle on password field
 */
public class LoginActivity extends AppCompatActivity {

    public static final String PREFERENCE_FILE="user_preferences";

    EditText mUserName;
    EditText mPassword;
    Button mLogin;

    User user;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLogin = (Button) findViewById(R.id.button_login);
        mUserName = (EditText) findViewById(R.id.et_uid);
        mPassword = (EditText) findViewById(R.id.et_password);
        user = new User();

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setUserName(mUserName.getText().toString());
                user.setPassword(Integer.parseInt(mPassword.getText().toString()));

                sharedPreferences = getSharedPreferences(PREFERENCE_FILE, Context.MODE_PRIVATE);
                editor = sharedPreferences.edit();
                editor.putString("username",
                        user.getUserName());
                editor.putInt("password",
                        user.getPassword());
                editor.apply();

                Toast.makeText(LoginActivity.this, "Logged in with" + user.getUserName(), Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }
}
