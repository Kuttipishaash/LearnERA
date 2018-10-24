package com.learnera.app.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.learnera.app.R;
import com.learnera.app.models.Constants;
import com.learnera.app.models.User;
import com.learnera.app.utils.Utils;
import com.yalantis.guillotine.animation.GuillotineAnimation;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WelcomeActivity extends AppCompatActivity {

    private LinearLayout mAnnouncement;
    private LinearLayout mAttendance;
    private LinearLayout mLogout;
    private LinearLayout mSyllabus;
    private LinearLayout mMarks;
    private LinearLayout mSeating;
    private TextView mLoginStatus;
    private TextView mGuilLoginStatus;

    private boolean about_us_open = false;

    private boolean doubleBackToExitPressedOnce = false;

    private TextView mAppName;
    private AlertDialog.Builder mSeatingDialogAlert;

    private SharedPreferences sharedPreferences;
    private SharedPreferences preferences;

    private static final long RIPPLE_DURATION = 250;

    @BindView(R.id.toolbar)
    private Toolbar toolbar;

    //TODO: implement bindview
    //@BindView(R.id.welcome_root)
    private FrameLayout welcome_root;

    //TODO: implement bindview
    //@BindView(R.id.content_hamburger)
    private View contentHamburger;
    private boolean isOpened = true;

    private String user;

    private User userInfo;

    private String retID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        runUpdatesIfNecessary();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Set login status
        checkLogin();
    }

    @Override
    public void onBackPressed() {
        if (!about_us_open) {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                finishAffinity();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        } else {
            super.onBackPressed();
            finishAffinity();
        }
    }

    private void checkLogin() {
        sharedPreferences = getSharedPreferences(Constants.PREFERENCE_FILE, Context.MODE_PRIVATE);

        preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean previouslyStarted = preferences.getBoolean(getString(R.string.pref_previously_started), false);

        if (!previouslyStarted) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(getString(R.string.pref_previously_started), true);
            editor.apply();
            startActivity(new Intent(WelcomeActivity.this, IntroScreensActivity.class));
        } else if (!User.isLoggedIn(this)) {

            startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
            finish();
            //TODO: REMOVE AFTER TEST
//            doWhenLoggedIn();

        } else {
            doWhenLoggedIn();
        }
    }

    void runUpdatesIfNecessary() {
        int versionCode = 0;
        try {
            versionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (preferences == null)
            preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        else {
            if (preferences.getInt(getString(R.string.pref_update_version), 0) != versionCode) {
                try {
                    //TODO : Preferences updates for this version here
                    boolean firstStart = preferences.getBoolean(getString(R.string.pref_previously_started), false);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.clear();
                    editor.putInt(getString(R.string.pref_update_version), versionCode);
                    editor.putBoolean(getString(R.string.pref_previously_started), firstStart);
                    editor.apply();
                } catch (Throwable t) {
                    // update failed, or cancelled
                    t.printStackTrace();
                }
            }
        }
    }


    private void doWhenLoggedIn() {
        setContentView(R.layout.activity_welcome);
        final FragmentActivity current_frag = this;

        initViews();
        setFonts();

        ButterKnife.bind(this);


        if (toolbar != null) {
            setSupportActionBar(toolbar);
            Objects.requireNonNull(getSupportActionBar()).setTitle(null);

        }
        welcome_root = findViewById(R.id.welcome_root);
        contentHamburger = findViewById(R.id.content_hamburger);

        View guillotineMenu = getLayoutInflater().inflate(R.layout.guillotine, null);
        final GuillotineAnimation gmenu = new GuillotineAnimation.GuillotineBuilder(guillotineMenu, guillotineMenu.findViewById(R.id.guillotine_hamburger), contentHamburger)
                .setStartDelay(RIPPLE_DURATION)
                .setActionBarViewForAnimation(toolbar)
                .setClosedOnStart(true)
                .build();

//        public void onBackPressed () {
//            if (!isOpened) {
//                super.onBackPressed();
//            }
//            gmenu.close();
//        }


        welcome_root.addView(guillotineMenu);
        LinearLayout glogout = findViewById(R.id.guil_logout);
        glogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gmenu.close();
                User.logout(WelcomeActivity.this);
            }
        });
        LinearLayout gabout_us = findViewById(R.id.guil_about_us);
        gabout_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                about_us_open = true;

                Utils.showAbout(current_frag);
            }
        });


//        On Click for Contact Us
        LinearLayout gcont_us = findViewById(R.id.guil_contact_us);
        gcont_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gmenu.close();
                Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
                sendIntent.setData(Uri.parse("mailto:"));
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.feedback_mail_title));
                sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.feedback_mail_address)});
                sendIntent.putExtra(Intent.EXTRA_TEXT, "");
                startActivity(sendIntent);
            }
        });

        RelativeLayout groot = findViewById(R.id.guil_root);
        groot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gmenu.close();
            }
        });

        mGuilLoginStatus = findViewById(R.id.guil_logged_user);
        setUserStatus();

    }


    public void setFonts() {
        //Set font
        Typeface appName = Typeface.createFromAsset(getAssets(), "fonts/Pasajero.otf");
        Typeface loginName = Typeface.createFromAsset(getAssets(), "fonts/SourceSansPro-ExtraLight.ttf");
        mLoginStatus.setTypeface(loginName);
        mAppName.setTypeface(appName);
    }

    public void setUserStatus() {

        user = sharedPreferences.getString(getString(R.string.pref_user), null);

        String longText;
        if (user != null) {
            longText = "Logged in as : " + user;
            mLoginStatus.setText(longText);
            String[] fname = user.split(" ", 2);

            mGuilLoginStatus.setText("Hey, " + fname[0] + "!");

        } else {
            longText = getString(R.string.status_not_logged_in);
            mLoginStatus.setText(longText);
        }
    }

    public void initViews() {

        mLoginStatus = findViewById(R.id.login_status);
//        mGuilLoginStatus = findViewById(R.id.guil_logged_user);

        mAppName = findViewById(R.id.app_name);

        mAnnouncement = findViewById(R.id.drawable_announcement);
        mAnnouncement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomeActivity.this, AnnouncementsActivity.class));
            }
        });

        mAttendance = findViewById(R.id.drawable_attendance);
        mAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomeActivity.this, AttendanceActivity.class));
            }
        });

        mLogout = findViewById(R.id.drawable_logout);
        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User.logout(WelcomeActivity.this);
            }
        });

        mSyllabus = findViewById(R.id.drawable_syllabus);
        mSyllabus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomeActivity.this, SyllabusActivity.class));
            }
        });

        mMarks = findViewById(R.id.drawable_marks);
        mMarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomeActivity.this, MarksActivity.class));
            }
        });

        mSeating = findViewById(R.id.drawable_seating_plan);
        mSeating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean alertFlag = preferences.getBoolean(getString(R.string.pref_seating_dialog_enabled), true);

                if (alertFlag) {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean(getString(R.string.pref_seating_dialog_enabled), false);
                    editor.apply();
                    mSeatingDialogAlert.show();
                } else {
                    openSeatingPdf();
                }

            }
        });

        mSeatingDialogAlert = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogCustom));
        mSeatingDialogAlert.setTitle(getString(R.string.dialog_warn_seating_title));
        mSeatingDialogAlert.setMessage(getString(R.string.dialog_warn_seating_description));
        mSeatingDialogAlert.setPositiveButton(getString(R.string.btn_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                openSeatingPdf();
            }
        });
    }

    public void openSeatingPdf() {
        userInfo = User.getLoginInfo(WelcomeActivity.this);


        retID = "RET" + userInfo.getUserName().substring(1, 3) + userInfo.getDept().toUpperCase() +
                userInfo.getUserName().substring(5);

        LayoutInflater inflater = getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.dialog_ktu_id, null);
        AlertDialog.Builder ktuIdDialog = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogCustom));
        ktuIdDialog.setView(dialoglayout);
        final EditText input = dialoglayout.findViewById(R.id.et_ktu_id_dki);
        final CheckBox checkBox = dialoglayout.findViewById(R.id.cb_remember_ktu_id_dki);
        ktuIdDialog.setTitle(getString(R.string.dialog_seating_ktu_id_confirm));
        setIDField(input);
        ktuIdDialog.setPositiveButton(getString(R.string.btn_confirm),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        retID = input.getText().toString().toUpperCase();
                        if (checkBox.isChecked()) {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(getString(R.string.pref_ktu_id), retID);
                            editor.apply();
                        }
                        launchChromeCustomTab();
                    }
                });

        ktuIdDialog.setNegativeButton(getString(R.string.btn_cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        ktuIdDialog.show();
    }

    private void setIDField(EditText input) {
        String rememberedKTUId = sharedPreferences.getString(getString(R.string.pref_ktu_id), "");
        if (rememberedKTUId.equals("")) {
            input.setText(retID);
        } else {
            input.setText(rememberedKTUId);
        }
    }

    private void launchChromeCustomTab() {

        String completeUrl = Constants.seatPlanURL + retID + ".pdf";
        //Launch chrome custom tab
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent intent = builder.build();
        builder.setToolbarColor(getResources().getColor(R.color.md_red_700));
        builder.setStartAnimations(WelcomeActivity.this, R.anim.slide_in_right, R.anim.slide_out_left);
        builder.setExitAnimations(WelcomeActivity.this, R.anim.slide_in_left, R.anim.slide_out_right);
        intent.launchUrl(WelcomeActivity.this, Uri.parse(completeUrl));
    }

}