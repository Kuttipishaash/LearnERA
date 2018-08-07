package com.learnera.app.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.MetricAffectingSpan;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.learnera.app.R;
import com.learnera.app.Utils;
import com.learnera.app.WelcomeActivity;
import com.learnera.app.data.Constants;
import com.learnera.app.data.NothingSelectedSpinnerAdapter;
import com.learnera.app.data.User;
import com.learnera.app.data.UserDatabaseHandler;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Prejith on 7/4/2017.
 */

public class LoginFragment extends Fragment {

    UserDatabaseHandler userDatabaseHandler;
    ArrayList<User> rememberedUsers;
    ArrayList<String> listToDisplay;

    Spinner mDepartment;
    CheckBox mRememberMe;
    AutoCompleteTextView mUserName;
    EditText mPassword;
    TextView mTitleRsms;
    TextView mTitleLogin;
    TextView mCreators;
    TextInputLayout mUserInput;
    TextInputLayout mPassInput;
    Button mLogin;
    ProgressDialog mProgressDialog;
    InputMethodManager inputMethodManager;

    User user;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    Connection.Response res;
    Elements u;
    View view;

    String name;
    ArrayList<String> brancheslist;

    int countSemesters = 0;

    public LoginFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        user = new User();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_login, container, false);
        setHasOptionsMenu(true);

        //Initialize database handler for users remembered
        userDatabaseHandler = new UserDatabaseHandler(getContext());


        initView();
        setupDropDownUsersList();


        initProgressDialog();

        initSpinner();

        SpannableString s = new SpannableString("LEARNERA");
        s.setSpan(new TypefaceSpan(getActivity(), "Pasajero.otf"), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getActivity().setTitle(s);

        //set fonts to rsmslogin
        mTitleRsms = (TextView) view.findViewById(R.id.text_title_rsms);
        mTitleLogin = (TextView) view.findViewById(R.id.text_title_login);
        mCreators = (TextView) view.findViewById(R.id.text_creators);
        Typeface boldSans = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SourceSansPro-Bold.ttf");
        Typeface exLightSans = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SourceSansPro-ExtraLight.ttf");
        mTitleRsms.setTypeface(boldSans);
        mTitleLogin.setTypeface(exLightSans);
        mCreators.setTypeface(boldSans);

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = mUserName.getText().toString();
                String password = mPassword.getText().toString();
                if (TextUtils.isEmpty(userName)) {
                    //set error on username field
                    mUserInput.setError("Username cannot be empty");

                    //request focus for username field
                    mUserInput.requestFocus();

                    //show keyboard on emtpy username entered
                    inputMethodManager.showSoftInput(mUserName, InputMethodManager.SHOW_IMPLICIT);
                } else if (TextUtils.isEmpty(password)) {
                    mPassInput.setError("Password cannot be empty");
                    mPassInput.requestFocus();
                    inputMethodManager.showSoftInput(mPassword, InputMethodManager.SHOW_IMPLICIT);
                } else if (mDepartment.getSelectedItemPosition() == 0) {
                    TextView errorText = (TextView) mDepartment.getSelectedView();
                    errorText.setError("No branch selected");
                    errorText.setTextColor(Color.RED);//just to highlight that this is an error
                    errorText.setText("Please select a branch");//changes the selected item text to this
                } else {
                    if (Utils.isNetworkAvailable(getActivity())) {

                        JSoupLoginTask jSoupLoginTask = new JSoupLoginTask();
                        jSoupLoginTask.execute();

                        Handler handler = new Handler();
                        Utils.testInternetConnectivity(jSoupLoginTask, handler);

                        user.setUserName(userName);
                        user.setPassword(Integer.parseInt(password));
                    } else {
                        Utils.doWhenNoNetwork(getActivity());
                    }
                }
            }
        });

        return view;
    }


    //Login History Implementation//
    private void setupDropDownUsersList() {
        rememberedUsers = userDatabaseHandler.getAllUsers();
        listToDisplay = new ArrayList<String>();
        String tempUsersName;
        int tempIndexOfSpaceInUserName;
        if (rememberedUsers.size() != 0) {
            for (int i = 0; i < rememberedUsers.size(); i++) {
                tempUsersName = rememberedUsers.get(i).getUser();
                tempIndexOfSpaceInUserName = tempUsersName.indexOf(" ");
                tempUsersName = tempUsersName.substring(0, tempIndexOfSpaceInUserName);
                listToDisplay.add(rememberedUsers.get(i).getUserName());
            }
            mUserName.setThreshold(1);
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, listToDisplay);
            mUserName.setAdapter(arrayAdapter);
            mUserName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    int position = listToDisplay.indexOf(adapterView.getItemAtPosition(i));
                    mPassword.setText(Integer.toString(rememberedUsers.get(position).getPassword()));
                    int indexOfSpinner = brancheslist.indexOf(rememberedUsers.get(position).getDept());
                    mDepartment.setSelection(indexOfSpinner);

                }
            });
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private boolean isLoginInfoCorrect() {
        //Check if URL redirects to home page. Login redirects to home page only when entered username & password are correct
        if (res.url().toString().equals(Constants.homeURL))
            return true;
        else {
            Toast.makeText(view.getContext(), "Incorrect username and password", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void initView() {
        mLogin = (Button) view.findViewById(R.id.button_login);
        mUserName = (AutoCompleteTextView) view.findViewById(R.id.et_uid);
        mPassword = (EditText) view.findViewById(R.id.et_password);
        mUserInput = (TextInputLayout) view.findViewById(R.id.text_input_username_field);
        mPassInput = (TextInputLayout) view.findViewById(R.id.text_input_password_field);
        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        mDepartment = (Spinner) view.findViewById(R.id.department_spinner);
        mRememberMe = (CheckBox) view.findViewById(R.id.checkbox_remember_me);


        //decreased spinner height due to issues in low res screens
        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            // Get private mPopup member variable and try cast to ListPopupWindow
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(mDepartment);

            // Set popupWindow height to 300px
            popupWindow.setHeight(300);
        } catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            // silently fail...
        }
    }

    private void initSpinner() {
        brancheslist = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.branches)));
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.branches_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mDepartment.setAdapter(new NothingSelectedSpinnerAdapter(
                adapter,
                R.layout.contact_spinner_row_nothing_selected,
                getActivity()
        ));

        //item selection handling
        mDepartment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                user.setDept(brancheslist.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void initProgressDialog() {
        mProgressDialog = new ProgressDialog(view.getContext(),R.style.ProgressDialogCustom);
        mProgressDialog.setMessage("Checking login information...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
    }

    private void login() {
        //write username and password to sharedpreference file
        sharedPreferences = getActivity().getSharedPreferences(Constants.PREFERENCE_FILE, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString("user",
                user.getUser());
        editor.putString("sem",
                user.getSem());
        editor.putString("dept",
                user.getDept());
        editor.putString("username",
                user.getUserName());
        editor.putInt("password",
                user.getPassword());
        editor.putInt("attendanceCutoff",
                75);
        editor.commit();
        if (mRememberMe.isChecked()) {
            int indexCheck = -1;
            indexCheck = listToDisplay.indexOf(user.getUserName());
            if (indexCheck != -1)       //Checks if user is in the remembered users list
                userDatabaseHandler.updateUser(user);   //if the user is remembered user his current values are updated
            else
                userDatabaseHandler.addUser(user);  //if it is a new user then his
        } else {
            int indexCheck = -1;
            indexCheck = listToDisplay.indexOf(user.getUserName());
            if (indexCheck != -1)       //Checks if user is in the remembered users list
                userDatabaseHandler.deleteUser(user);   //if the user is remembered user his current values are updated
        }

        Toast.makeText(view.getContext(), "Logged in as: \n" + user.getUser(), Toast.LENGTH_SHORT)
                .show();

        startActivity(new Intent(getActivity(), WelcomeActivity.class));
    }

    private class JSoupLoginTask extends AsyncTask<Void, Void, Void> {
        Elements list;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mProgressDialog.show();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();

            if (mProgressDialog.isShowing()) {
                mProgressDialog.hide();
                Utils.doWhenNoNetwork(getActivity());
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            for (Element ls : list) {
                for (Element opt : ls.select("option")) {
                    countSemesters += 1;
                }
            }
            String x = "s" + countSemesters;
            user.setSem(x);

            name = u.text();
            user.setUser(name);

            mProgressDialog.dismiss();

            if (isLoginInfoCorrect()) {
                login();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                res = Jsoup.connect(Constants.loginURL)
                        .data("user", user.getUserName())
                        .data("pass", String.valueOf(user.getPassword()))
                        .followRedirects(true)
                        .method(Connection.Method.POST)
                        .execute();
                Document doc = Jsoup.connect(Constants.homeURL)
                        .cookies(res.cookies())
                        .get();
                u = doc.select("strong");
                Document doc2 = Jsoup.connect(Constants.attendanceURL)
                        .cookies(res.cookies())
                        .get();
                list = doc2.select("select[name=code]");
            } catch (IOException e) {
                Log.e("LOGIN_ACTIVITY", "Error checking login info");
            }
            return null;
        }
    }

    /**
     * Style a {@link Spannable} with a custom {@link Typeface}.
     *
     * @author Tristan Waddington
     */
    public class TypefaceSpan extends MetricAffectingSpan {
        /**
         * An <code>LruCache</code> for previously loaded typefaces.
         */
        private LruCache<String, Typeface> sTypefaceCache =
                new LruCache<String, Typeface>(12);

        private Typeface mTypeface;

        /**
         * Load the {@link Typeface} and apply to a {@link Spannable}.
         */
        public TypefaceSpan(Context context, String typefaceName) {
            mTypeface = sTypefaceCache.get(typefaceName);

            if (mTypeface == null) {
                mTypeface = Typeface.createFromAsset(context.getApplicationContext()
                        .getAssets(), String.format("fonts/%s", typefaceName));

                // Cache the loaded Typeface
                sTypefaceCache.put(typefaceName, mTypeface);
            }
        }

        @Override
        public void updateMeasureState(TextPaint p) {
            p.setTypeface(mTypeface);

            // Note: This flag is required for proper typeface rendering
            p.setFlags(p.getFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        }

        @Override
        public void updateDrawState(TextPaint tp) {
            tp.setTypeface(mTypeface);

            // Note: This flag is required for proper typeface rendering
            tp.setFlags(tp.getFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        }
    }
}
