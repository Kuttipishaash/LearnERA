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
import android.support.annotation.NonNull;
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
import com.learnera.app.activities.WelcomeActivity;
import com.learnera.app.adapters.NothingSelectedSpinnerAdapter;
import com.learnera.app.database.LearnEraRoomDatabase;
import com.learnera.app.database.dao.UserDAO;
import com.learnera.app.models.Constants;
import com.learnera.app.models.User;
import com.learnera.app.utils.Utils;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Created by Prejith on 7/4/2017.
 */

public class LoginFragment extends Fragment implements View.OnClickListener {
    // Constants
    private static final String TAG = "LoginFragment";

    // Data
    private List<User> rememberedUsers;
    private ArrayList<String> listToDisplay;
    private ArrayList<String> brancheslist;
    private User user;

    // Views
    private View parentView;
    private Spinner departmentSpinner;
    private CheckBox rememberMeCheckbox;
    private AutoCompleteTextView userNameAutoCompTextView;
    private EditText passwordEditText;
    private TextView rsmsTitleTextView;
    private TextView loginTitleTextView;
    private TextView creatorsTextView;
    private TextInputLayout mUserInput;
    private TextInputLayout mPassInput;
    private Button loginButton;
    private ProgressDialog mProgressDialog;
    private InputMethodManager inputMethodManager;

    // Jsoup handling variables
    private Connection.Response res;
    private String userName;

    // Database objects
    private UserDAO userDAO;

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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        parentView = inflater.inflate(R.layout.fragment_login, container, false);
        setHasOptionsMenu(true);

        //Initialize database access object(DAO) for users remembered
        userDAO = LearnEraRoomDatabase.getDatabaseInstance(getActivity()).usersDAO();

        initViews();
        setSpinnerHeight();
        setupDropDownUsersList();
        initProgressDialog();
        setupDepartmentSpinnerContents();
        setFonts();

        loginButton.setOnClickListener(this);
        return parentView;
    }


    private void setFonts() {
        //Setting app title with custom font
        SpannableString s = new SpannableString(getString(R.string.app_name_all_caps));
        s.setSpan(new TypefaceSpan(getActivity(), "Pasajero.otf"), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getActivity().setTitle(s);

        //set fonts to rsmslogin
        Typeface boldSans = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SourceSansPro-Bold.ttf");
        Typeface exLightSans = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SourceSansPro-ExtraLight.ttf");
        rsmsTitleTextView.setTypeface(boldSans);
        loginTitleTextView.setTypeface(exLightSans);
        creatorsTextView.setTypeface(boldSans);
    }

    // function to decrease spinner height due to issues in low res screens
    //TODO: Make the decrease only for low res screens
    private void setSpinnerHeight() {
        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            // Get private mPopup member variable and try cast to ListPopupWindow
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(departmentSpinner);

            // Set popupWindow height to 300px
            popupWindow.setHeight(300);
        } catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    //Login History Implementation//
    private void setupDropDownUsersList() {
        rememberedUsers = userDAO.getUsers();
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
            userNameAutoCompTextView.setThreshold(1);
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Objects.requireNonNull(getActivity()), android.R.layout.simple_list_item_1, listToDisplay);
            userNameAutoCompTextView.setAdapter(arrayAdapter);
            userNameAutoCompTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    int position = listToDisplay.indexOf(adapterView.getItemAtPosition(i));
                    passwordEditText.setText(Integer.toString(rememberedUsers.get(position).getPassword()));
                    int indexOfSpinner = brancheslist.indexOf(rememberedUsers.get(position).getDept());
                    departmentSpinner.setSelection(indexOfSpinner);

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
            Toast.makeText(parentView.getContext(), getString(R.string.toast_login_incorrect_cred), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void initViews() {
        loginButton = parentView.findViewById(R.id.button_login);
        userNameAutoCompTextView = parentView.findViewById(R.id.et_uid);
        passwordEditText = parentView.findViewById(R.id.et_password);
        mUserInput = parentView.findViewById(R.id.text_input_username_field);
        mPassInput = parentView.findViewById(R.id.text_input_password_field);
        inputMethodManager = (InputMethodManager) Objects.requireNonNull(getActivity())
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        departmentSpinner = parentView.findViewById(R.id.department_spinner);
        rememberMeCheckbox = parentView.findViewById(R.id.checkbox_remember_me);
        rsmsTitleTextView = parentView.findViewById(R.id.text_title_rsms);
        loginTitleTextView = parentView.findViewById(R.id.text_title_login);
        creatorsTextView = parentView.findViewById(R.id.text_creators);
    }


    private void setupDepartmentSpinnerContents() {
        brancheslist = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.branches_code_array)));
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(Objects.requireNonNull(getActivity()),
                R.array.branches_name_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        departmentSpinner.setAdapter(new NothingSelectedSpinnerAdapter(
                adapter,
                R.layout.contact_spinner_row_nothing_selected,
                getActivity()
        ));

        //item selection handling
        departmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        mProgressDialog = new ProgressDialog(parentView.getContext(), R.style.ProgressDialogCustom);
        mProgressDialog.setMessage(getString(R.string.msg_checking_login));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
    }

    private void login() {
        //write username and password to sharedpreference file
        SharedPreferences sharedPreferences = Objects.requireNonNull(getActivity())
                .getSharedPreferences(Constants.PREFERENCE_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.pref_user),
                user.getUser());
        editor.putInt(getString(R.string.pref_sem),
                user.getSem());
        editor.putString(getString(R.string.pref_department),
                user.getDept());
        editor.putString(getString(R.string.pref_username),
                user.getUserName());
        editor.putInt(getString(R.string.pref_password),
                user.getPassword());
        editor.putInt(getString(R.string.pref_attendance_cutoff),
                75);
        editor.apply();
        if (rememberMeCheckbox.isChecked()) {
            int indexCheck = listToDisplay.indexOf(user.getUserName());
            if (indexCheck != -1)       //Checks if user is in the remembered users list
                userDAO.updateUser(user);   //if the user is remembered user his current values are updated
            else
                userDAO.insertUser(user);  //if it is a new user then his
        } else {
            int indexCheck = listToDisplay.indexOf(user.getUserName());
            if (indexCheck != -1)       //Checks if user is in the remembered users list
                userDAO.deleteUser(user);   //if the user is remembered user his current values are updated
        }

        startActivity(new Intent(getActivity(), WelcomeActivity.class));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_login) {
            String userName = userNameAutoCompTextView.getText().toString();
            String password = passwordEditText.getText().toString();
            if (TextUtils.isEmpty(userName)) {
                //set error on username field
                mUserInput.setError(getString(R.string.msg_err_incorrect_uid));

                //request focus for username field
                mUserInput.requestFocus();

                //show keyboard on emtpy username entered
                inputMethodManager.showSoftInput(userNameAutoCompTextView, InputMethodManager.SHOW_IMPLICIT);
            } else if (TextUtils.isEmpty(password)) {
                mPassInput.setError(getString(R.string.msg_err_empty_password));
                mPassInput.requestFocus();
                inputMethodManager.showSoftInput(passwordEditText, InputMethodManager.SHOW_IMPLICIT);
            } else if (departmentSpinner.getSelectedItemPosition() == 0) {
                TextView errorText = (TextView) departmentSpinner.getSelectedView();
                errorText.setError(getString(R.string.msg_err_no_branch));
                errorText.setTextColor(Color.RED);//just to highlight that this is an error
                errorText.setText(getString(R.string.msg_err_select_branch));//changes the selected item text to this
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
                Utils.doWhenNoNetwork(Objects.requireNonNull(getActivity()));
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            user.setSem(0);

            Element ls = list.first();
            user.setSem(ls.select("option").size());

            user.setUser(userName);

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
                userName = doc.select("strong").text();
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
                new LruCache<>(12);

        private Typeface mTypeface;

        /**
         * Load the {@link Typeface} and apply to a {@link Spannable}.
         */
        private TypefaceSpan(Context context, String typefaceName) {
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
