package com.learnera.app;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.learnera.app.data.Constants;
import com.learnera.app.data.User;
import com.learnera.app.fragments.AboutFragment;
import com.yalantis.guillotine.animation.GuillotineAnimation;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WelcomeActivity extends AppCompatActivity {

    LinearLayout mAnnouncement;
    LinearLayout mAttendance;
    LinearLayout mLogout;
    LinearLayout mSyllabus;
    LinearLayout mMarks;
    LinearLayout mSeating;
    TextView mLoginStatus;

    TextView mAppName;
    AlertDialog.Builder mSeatingDialogAlert;

    SharedPreferences sharedPreferences;
    SharedPreferences preferences;

    private static final long RIPPLE_DURATION = 250;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    //TODO: implement bindview
    //@BindView(R.id.welcome_root)
    FrameLayout welcome_root;

    //TODO: implement bindview
    //@BindView(R.id.content_hamburger)
    View contentHamburger;
    private boolean isOpened = true;

    String user;

    User userInfo;

    String retID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        initViews();


        ButterKnife.bind(this);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(null);

        }
        welcome_root = findViewById(R.id.welcome_root);
        contentHamburger = findViewById(R.id.content_hamburger);

        View guillotineMenu = getLayoutInflater().inflate(R.layout.guillotine, null);
        final GuillotineAnimation gmenu = new GuillotineAnimation.GuillotineBuilder(guillotineMenu, guillotineMenu.findViewById(R.id.guillotine_hamburger), contentHamburger)
                .setStartDelay(RIPPLE_DURATION)
                .setActionBarViewForAnimation(toolbar)
                .setClosedOnStart(true)
                .build();

//        @Override
//        public void onBackPressed () {
//            if (!isOpened) {
//                super.onBackPressed();
//            }
//            gmenu.close();
//        }


        welcome_root.addView(guillotineMenu);
        LinearLayout glogout = (LinearLayout) findViewById(R.id.guil_logout);
        glogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gmenu.close();
                SharedPreferences sharedPreferences = WelcomeActivity.this.getSharedPreferences(Constants.PREFERENCE_FILE, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                User.logout(WelcomeActivity.this);
            }
        });
        LinearLayout gaboutus = (LinearLayout) findViewById(R.id.guil_about_us);
        gaboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//TODO: Implement About
            }
        });


//        On Click for Log Out
        LinearLayout glogut = (LinearLayout) findViewById(R.id.guil_contact_us);
        glogut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gmenu.close();
                Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
                sendIntent.setData(Uri.parse("mailto:"));
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"learneraproject@gmail.com"});
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Feedback for LearnEra");
                startActivity(sendIntent);
            }
        });

        LinearLayout groot = (LinearLayout) findViewById(R.id.guil_root);
        groot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gmenu.close();
            }
        });

//        TextView gloggedin = (TextView) findViewById(R.id.guil_logged_user);
//        user = sharedPreferences.getString("user", null);
//        gloggedin.setText(user);


        sharedPreferences = getSharedPreferences(Constants.PREFERENCE_FILE, Context.MODE_PRIVATE);

        preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean previouslyStarted = preferences.getBoolean(getString(R.string.pref_previously_started), false);

        if (!previouslyStarted) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(getString(R.string.pref_previously_started), true);
            editor.apply();
            startActivity(new Intent(WelcomeActivity.this, IntroActivity.class));
        } else if (!User.isLoggedIn(this)) {
            Toast.makeText(this, "Please login to continue", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
        }


        //Set font
        Typeface appName = Typeface.createFromAsset(getAssets(), "fonts/Pasajero.otf");
        Typeface loginName = Typeface.createFromAsset(getAssets(), "fonts/SourceSansPro-ExtraLight.ttf");
        mLoginStatus.setTypeface(loginName);
        mAppName.setTypeface(appName);


    }

    @Override
    protected void onResume() {
        super.onResume();

        //Set logged in user status
        setUserStatus();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finishAffinity();
    }

    public void setUserStatus() {

        user = sharedPreferences.getString("user", null);

        String longText;
        if (user != null) {
            longText = "Logged in as : " + user;
            mLoginStatus.setText(longText);
        } else {
            longText = "Not logged into RSMS";
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
                SharedPreferences sharedPreferences = WelcomeActivity.this.getSharedPreferences(Constants.PREFERENCE_FILE, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
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
                boolean alertFlag = preferences.getBoolean("alert_dialog_enabled", true);

                if (alertFlag) {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("alert_dialog_enabled", false);
                    editor.apply();
                    mSeatingDialogAlert.show();
                } else {
                    openSeatingPdf();
                }

            }
        });

        mSeatingDialogAlert = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogCustom));
        mSeatingDialogAlert.setTitle("Attention");
        mSeatingDialogAlert.setMessage("If you receive a message that the 'PDF cannot be opened', then it implies that the seating plan has not been uploaded." +
                " This is caused by the improper implementation used by RSMS to display the file");
        mSeatingDialogAlert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
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
        ktuIdDialog.setTitle("Confirm KTU ID");
        setIDField(input);
        ktuIdDialog.setPositiveButton("CONFIRM",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        retID = input.getText().toString().toUpperCase();
                        if (checkBox.isChecked()) {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("ktu_id", retID);
                            editor.apply();
                        }
                        launchChromeCustomTab();
                    }
                });

        ktuIdDialog.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        ktuIdDialog.show();
    }

    private void setIDField(EditText input) {
        String rememberedKTUId = sharedPreferences.getString("ktu_id", "");
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