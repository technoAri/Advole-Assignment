package com.example.hppc.mychatdemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.login.LoginManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by HP PC on 2/18/2017.
 */
public class HomePageActivity extends AppCompatActivity {
    Button logoutButton;
    private String[] mNavigationDrawerItemTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    Toolbar toolbar;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    android.support.v7.app.ActionBarDrawerToggle mDrawerToggle;
    ImageView imageView;
    private ListView listView;
    private ArrayList<DataModelJson> list;
    private ArrayAdapterJsonList adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        EditText editText = (EditText) findViewById(R.id.edt);
        editText.setFocusable(false);

        mTitle = mDrawerTitle = getTitle();
        mNavigationDrawerItemTitles = getResources().getStringArray(R.array.navigation_drawer_items_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        imageView = (ImageView) findViewById(R.id.profileimg);

//        logoutButton = (Button) findViewById(R.id.logout_button);
//        logoutButton.setVisibility(View.INVISIBLE);

        list = new ArrayList<>();
        adapter = new ArrayAdapterJsonList(this, list);
        listView = (ListView) findViewById(R.id.listViewJson);

        if (InternetConnection.checkConnection(getApplicationContext())) {
            new GetDataTask().execute();
        } else {
            Toast.makeText(getApplicationContext(), "Could not load conversation. Check your internet connection and try again", Toast.LENGTH_SHORT).show();
        }
        listView.setAdapter(adapter);

//        new GetDataTask().execute();
        DataModelDrawer[] drawerItem = new DataModelDrawer[5];

        drawerItem[0] = new DataModelDrawer(R.mipmap.ic_launcher, "Profile");
        drawerItem[1] = new DataModelDrawer(R.mipmap.ic_launcher, "Home");
        drawerItem[2] = new DataModelDrawer(R.mipmap.ic_launcher, "About");
        drawerItem[3] = new DataModelDrawer(R.mipmap.ic_launcher, "Share");
        drawerItem[4] = new DataModelDrawer(R.mipmap.ic_launcher, "Log Out");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);

        DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(this, R.layout.list_view_item, drawerItem);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        setupDrawerToggle();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDrawerLayout.isDrawerOpen(Gravity.LEFT))
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                else
                    mDrawerLayout.openDrawer(Gravity.LEFT);
            }
        });
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }

        private void selectItem(int position) {

            Fragment fragment = null;

            switch (position) {
                case 0:
                    Toast.makeText(getApplicationContext(), "This is a demo. Press Log Out to sign of.", Toast.LENGTH_SHORT).show();
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                    break;
                case 1:
                    Toast.makeText(getApplicationContext(), "This is a demo. Press Log Out to sign of.", Toast.LENGTH_SHORT).show();
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                    break;
                case 2:
                    Toast.makeText(getApplicationContext(), "This is a demo. Press Log Out to sign of.", Toast.LENGTH_SHORT).show();
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                    break;
                case 3:
                    Toast.makeText(getApplicationContext(), "This is a demo. Press Log Out to sign of.", Toast.LENGTH_SHORT).show();
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                    break;
                case 4:
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                    LoginManager.getInstance().logOut();
                    Intent intent = new Intent(HomePageActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;

                default:
                    break;
            }

            if (fragment != null) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

                mDrawerList.setItemChecked(position, true);
                mDrawerList.setSelection(position);
                setTitle(mNavigationDrawerItemTitles[position]);
                mDrawerLayout.closeDrawer(mDrawerList);

            } else {
                Log.e("MainActivity", "Error in creating fragment");
            }
        }

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    void setupDrawerToggle() {
        mDrawerToggle = new android.support.v7.app.ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name, R.string.app_name);
        //This is necessary to change the icon of the Drawer Toggle upon state change.
        mDrawerToggle.syncState();
    }

    private class GetDataTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /**
             * Progress Dialog for User Interaction
             */
            dialog = new ProgressDialog(HomePageActivity.this);
            dialog.setTitle("Please Wait...");
            dialog.setMessage("Loading chat history");
            dialog.show();
        }

        @Nullable
        @Override
        protected Void doInBackground(Void... params) {

            JSONObject jsonObject = JSONParser.getDataFromWeb();

            try {
                if (jsonObject != null) {
                    if (jsonObject.length() > 0) {
                        JSONArray array = null;
                        try {
                            array = jsonObject.getJSONArray(Keys.KEY_DATA);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        int lenArray = array.length();
                        if (lenArray > 0) {
                            for (int jIndex = 0; jIndex < lenArray; jIndex++) {
                                DataModelJson model = new DataModelJson();
                                JSONObject innerObject = array.getJSONObject(jIndex);
                                String name = innerObject.getString(Keys.KEY_NAME);
                                String message = innerObject.getString(Keys.KEY_MESSEGE);
                                String image = innerObject.getString(Keys.KEY_PROFILE_PIC);

                                model.setName(name);
                                model.setMessage(message);
                                model.setProfile_image(image);
                                list.add(model);
                            }
                        }
                    }
                } else {

                }
            } catch (JSONException je) {
                Log.i(JSONParser.TAG, "" + je.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();
            if (list.size() > 0) {
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(getApplicationContext(), "Data Not found", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public void onBackPressed() {
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish(); // finish activity

    }
}

