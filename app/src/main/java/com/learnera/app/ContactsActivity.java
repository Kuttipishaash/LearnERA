package com.learnera.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.learnera.app.data.Contacts;
import com.learnera.app.data.ContactsAdapter;
import com.learnera.app.data.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prejith on 8/11/2016.
 */

public class ContactsActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private List<Contacts> contactsList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_contacts);
        mRecyclerView = (RecyclerView)findViewById(R.id.recycler_view_contacts);

        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        contactsList = new ArrayList<>();
        mAdapter = new ContactsAdapter(this, contactsList);
        mRecyclerView.setAdapter(mAdapter);

        prepareData();

        try {
            Glide.with(this).load(R.drawable.placeholder).into((ImageView)findViewById(R.id.card_contact_image));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_contacts, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.action_about):
                return true;
            case (R.id.action_logout):
                User.logout(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void prepareData() {
        Contacts c = new Contacts("Hoo", "bla", "95679", R.drawable.placeholder);
        contactsList.add(c);

        c = new Contacts("Hogro", "bntnla", "985679", R.drawable.placeholder);
        contactsList.add(c);

        c = new Contacts("Hntnoo", "blntda", "9215679", R.drawable.placeholder);
        contactsList.add(c);
    }
}
