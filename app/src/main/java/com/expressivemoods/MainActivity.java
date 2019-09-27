package com.expressivemoods;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String ANONYMOUS = "anonymous";
    final int RC_SIGN_IN = 1;

    public FirebaseAuth myFirebaseAuth;
    public FirebaseAuth.AuthStateListener myFirebaseAuthListener;
    String mUsername,mUserID;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);









        FirebaseDatabase database = FirebaseDatabase.getInstance();
       // DatabaseReference myRef = database.getReference("message");

       // myRef.setValue("Hello, World!");

        Toast.makeText(this, "Ref "+database.getReference()
                , Toast.LENGTH_SHORT).show();


        myFirebaseAuth = FirebaseAuth.getInstance();

        myFirebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                if(firebaseUser != null) {

                    Toast.makeText(getApplicationContext(), "You are signed in", Toast.LENGTH_SHORT).show();

                    onSignedInInitialise(firebaseUser.getDisplayName());

                    mUserID = firebaseUser.getUid();

                }else{
                    onSignedOutCleanUp();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.GoogleBuilder().build(),
                                            new AuthUI.IdpConfig.PhoneBuilder().build(),
                                            new AuthUI.IdpConfig.EmailBuilder().build()))
                                    .build(),
                            RC_SIGN_IN);


                }
            }
        };

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



        if(requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(), "Signed in Successful", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "Signed in notSuccessful", Toast.LENGTH_SHORT).show();
                finish();
            }
        }





    }





    private void onSignedInInitialise(String userName){
        mUsername = userName;

        //attachDatabaseReadListener();



    }




    private void onSignedOutCleanUp() {



        mUsername = ANONYMOUS;
        //mMessageAdapter.clear();
        //detachDatabaseReadListener();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(myFirebaseAuthListener != null) {
            myFirebaseAuth.removeAuthStateListener(myFirebaseAuthListener);
        }
        //mMessageAdapter.clear();
        //detachDatabaseReadListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        myFirebaseAuth.addAuthStateListener(myFirebaseAuthListener);
    }






    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        switch(id){
            case R.id.nav_angry:{
                Toast.makeText(getApplicationContext(),"Angry",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(),UploadActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("CAT", "angry");
                bundle.putString("USR",mUserID);
                i.putExtras(bundle);
                startActivity(i);


            }break;

            case R.id.nav_happy:{

                Toast.makeText(getApplicationContext(),"Happy",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(),UploadActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("CAT", "happy");
                bundle.putString("USR",mUserID);
                i.putExtras(bundle);
                startActivity(i);


            }break;

            case R.id.nav_neutral:{
                Toast.makeText(getApplicationContext(),"Neutral",Toast.LENGTH_SHORT).show();

                Intent i = new Intent(getApplicationContext(),UploadActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("CAT", "neutral");
                bundle.putString("USR",mUserID);
                i.putExtras(bundle);
                startActivity(i);


            }break;

            case R.id.nav_sad:{
                Toast.makeText(getApplicationContext(),"Sad",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(),UploadActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("CAT", "sad");
                bundle.putString("USR",mUserID);
                i.putExtras(bundle);
                startActivity(i);


            }break;

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void playHappySongs(View view) {

        Intent i = new Intent(getApplicationContext(),SongActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("CAT", "happy");
        bundle.putString("USR",mUserID);
        i.putExtras(bundle);
        startActivity(i);
    }

    public void playAngrySongs(View view) {

        Intent i = new Intent(getApplicationContext(),SongActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("CAT", "angry");
        bundle.putString("USR",mUserID);
        i.putExtras(bundle);
        startActivity(i);



    }

    public void playSadSongs(View view) {

        Intent i = new Intent(getApplicationContext(),SongActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("CAT", "sad");
        bundle.putString("USR",mUserID);
        i.putExtras(bundle);
        startActivity(i);
    }

    public void playNeutralSongs(View view) {

        Intent i = new Intent(getApplicationContext(),SongActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("CAT", "neutral");
        bundle.putString("USR",mUserID);
        i.putExtras(bundle);
        startActivity(i);
    }





}
