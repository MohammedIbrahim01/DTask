package com.example.abdelazim.globaltask.main;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.abdelazim.globaltask.PublishTaskActivity;
import com.example.abdelazim.globaltask.R;
import com.example.abdelazim.globaltask.achievements.AchievementsFragment;
import com.example.abdelazim.globaltask.add_task.AddTaskFragment;
import com.example.abdelazim.globaltask.repository.model.Task;
import com.example.abdelazim.globaltask.settings.SettingsActivity;
import com.example.abdelazim.globaltask.tasks.TasksFragment;
import com.example.abdelazim.globaltask.utils.AppConstants;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements MainViewModel.MainActivityView, SharedPreferences.OnSharedPreferenceChangeListener, GoogleApiClient.OnConnectionFailedListener {

    public static final int RC_GOOGLE_SIGN_IN = 1009;
    // ViewModel
    MainViewModel mainViewModel;
    // FragmentManager instance variable
    private FragmentManager fragmentManager;
    // SharedPreferences
    private SharedPreferences sharedPreferences;
    // Firebase
    private FirebaseAuth firebaseAuth;
    private GoogleApiClient googleApiClient;
    private Menu menu;
    private boolean signedInWithGoogle;
    // RealTime database
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference usersNode;
    // Ads
    AdView adView;
    InterstitialAd interstitialAd;
    private int interstitialCount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences(AppConstants.DTASK_SHARED, MODE_PRIVATE);
        // Get fragmentManager
        fragmentManager = getSupportFragmentManager();
        // Get ViewModel
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        // Start ViewModel
        mainViewModel.start(getApplicationContext(), this);
        // Display TasksFragment
        mainViewModel.setScreen(1);
        // Observe
        mainViewModel.observe(this);

        // Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        usersNode = firebaseDatabase.getReference().child("users");

        // Determine if the user is admin

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(R.string.default_web_client_id))
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // Ads
        adView = findViewById(R.id.banner_adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getString(R.string.interstitial_ad_id));
        loadInterstitialAd();
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                Log.i("WWW", "onAdLoaded: loaded");
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the interstitial ad is closed.
                loadInterstitialAd();
            }
        });
    }

    private void loadInterstitialAd() {
        Log.i("WWW", "loadInterstitialAd");
        interstitialAd.loadAd(new AdRequest.Builder().build());
    }


    private void showInterstitialAd() {
        Log.i("WWW", "InterstitialAd: " + interstitialCount);
        if (interstitialCount / 2 == 0) {
            interstitialCount++;
            return;
        }
        if (interstitialAd.isLoaded())
            interstitialAd.show();

        interstitialCount = 0;
    }


    @Override
    protected void onPause() {
        if (adView != null)
            adView.pause();

        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adView != null)
            adView.resume();

        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    protected void onDestroy() {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
        if (adView != null)
            adView.destroy();
        super.onDestroy();
    }

    /**
     * Create menu to display settings option
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        this.menu = menu;
        updateUi();
        return true;
    }


    /**
     * Handle menu items selection
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // Display SettingsActivity
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                return true;
            case R.id.action_log:
                if (!signedInWithGoogle) {
                    Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                    startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN);
                    return true;
                } else {
                    Auth.GoogleSignInApi.signOut(googleApiClient);
                    signedInWithGoogle = false;
                    Toast.makeText(this, "Signed out !", Toast.LENGTH_SHORT).show();
                    updateUi();
                    updateMenuForNormal();
                    return true;
                }

            case R.id.action_publish_task:
                startActivity(new Intent(MainActivity.this, PublishTaskActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_GOOGLE_SIGN_IN) {

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {

                GoogleSignInAccount account = result.getSignInAccount();
                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            signedInWithGoogle = true;
                            updateUi();
                            final FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                            final DatabaseReference currentUserNode = usersNode.child(currentUser.getUid());
                            currentUserNode.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (!dataSnapshot.hasChild("admin")) {
                                        currentUserNode.child("admin").setValue(false);
                                        currentUserNode.child("email").setValue(currentUser.getEmail());
                                    } else if (dataSnapshot.child("admin").getValue(Boolean.class)) {
                                        updateMenuForAdmin();
                                        Log.i("WWW", "user is admin");
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            Toast.makeText(MainActivity.this, "hello, " + currentUser.getDisplayName(), Toast.LENGTH_SHORT).show();
                        } else {
                            Log.i("WWW", "onComplete: failed: " + task.getResult().toString());
                            Toast.makeText(MainActivity.this, "google sign in failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }

    private void updateUi() {

        if (signedInWithGoogle)
            menu.findItem(R.id.action_log).setTitle("log out");
        else
            menu.findItem(R.id.action_log).setTitle("Sign in with google");
    }

    private void updateMenuForAdmin() {
        menu.findItem(R.id.action_publish_task).setVisible(true);
    }

    private void updateMenuForNormal() {
        menu.findItem(R.id.action_publish_task).setVisible(false);
    }

    /**
     * Display the appropriate fragment
     *
     * @param screen
     */
    @Override
    public void display(int screen) {
        switch (screen) {
            case 0:
                // Display AchievementsFragment
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                        .replace(R.id.main_frag_container, AchievementsFragment.newInstance())
                        .commit();
                break;
            case 1:
                // Display TasksFragment
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                        .replace(R.id.main_frag_container, TasksFragment.newInstance())
                        .commit();
                break;
            case 2:
                // Display AddTaskFragment
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                        .addToBackStack(null)
                        .replace(R.id.main_frag_container, AddTaskFragment.newInstance())
                        .commit();
                break;

            case 3:
                showInterstitialAd();
                fragmentManager.popBackStack();
                break;

        }
    }


    @Override
    public void display(int screen, Task task) {
        if (screen == 4) {
            AddTaskFragment editTaskFragment = AddTaskFragment.newInstance();
            Bundle args = new Bundle();
            args.putString("title", task.getTitle());
            args.putString("description", task.getDescription());
            args.putInt("taskId", task.getId());
            args.putLong("time", task.getTime());
            editTaskFragment.setArguments(args);
            fragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                    .addToBackStack(null)
                    .replace(R.id.main_frag_container, editTaskFragment)
                    .commit();
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        Log.i("WWW", "reschedule wakeup notification");

        long wakeupTime = sharedPreferences.getLong(AppConstants.KEY_WAKEUP_TIME, 0);
        mainViewModel.rescheduleWakeupNotification(wakeupTime);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i("WWW", "onConnectionFailed: " + connectionResult.getErrorMessage());
    }
}
