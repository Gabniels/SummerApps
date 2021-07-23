package com.example.summer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.accounts.Account;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.summer.fragment.AccountFragment;
import com.example.summer.fragment.HomeFragment;
import com.example.summer.fragment.PostFragment;
import com.example.summer.fragment.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView botnav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        botnav = findViewById(R.id.botNav);
        botnav.setOnNavigationItemSelectedListener(this);

//        Bundle intent = getIntent().getExtras();
//        if (intent != null) {
//            String publisher = intent.getString("publisherid");
//
//            SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
//            editor.putString("profileid", publisher);
//            editor.apply();
//
//            getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, new AccountFragment()).commit();
//        } else {
//            getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, new HomeFragment()).commit();
//        }

        loadFragment(new HomeFragment());


    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.flContainer, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.nav_dashboard:
                fragment = new HomeFragment();
                break;
            case R.id.nav_search:
                fragment = new SearchFragment();
                break;
            case R.id.nav_add:
                startActivity(new Intent(MainActivity.this, PostActivity.class));
                break;
            case R.id.nav_chat:
                Toast.makeText(this, "Fitur belum tersedia", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_profile:
                SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
                editor.putString("profileid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                editor.apply();
                fragment = new AccountFragment();
                break;
        }

        return loadFragment(fragment);
    }


}