package com.learnera.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.learnera.app.fragments.LoginFragment;

/**  Use show password toggle on password field. Insert fragment to login in intro screen. Logout option in
 all screens. Go back to last activity on pressing login and login successful
 */
public class LoginActivity extends AppCompatActivity {

    public static final String PREFERENCE_FILE="user_preferences";

    private Fragment fragment;
    private FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        fragment = new LoginFragment();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_login, fragment);
        fragmentTransaction.commit();

    }
}
