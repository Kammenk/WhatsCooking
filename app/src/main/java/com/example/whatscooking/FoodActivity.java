package com.example.whatscooking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.app.FragmentManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class FoodActivity extends AppCompatActivity {

    public static SQLiteDatabase mainDB;
    public static BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        mainDB = this.openOrCreateDatabase("Recipes",MODE_PRIVATE,null);
        mainDB.execSQL("CREATE TABLE IF NOT EXISTS recipee (id INTEGER PRIMARY KEY, image VARCHAR, title VARCHAR, cookTime INT(3), quantity INT(3), calories INT(5), dietLabel VARCHAR, healthLabel VARCHAR, ingredients VARCHAR)");

        bottomNavigationView = findViewById(R.id.bot_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        setHomeFragment();

    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            Log.i("MainActivity", "popping backstack");
            fm.popBackStack();
        } else {
            Log.i("MainActivity", "nothing on backstack, calling super");
            super.onBackPressed();
        }
    }

    private void setHomeFragment(){
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new HomeFragment()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = menuItem -> {
        Fragment selectedFragment = null;

        switch (menuItem.getItemId()){
            case R.id.nav_home:
                selectedFragment = new HomeFragment();
                break;
            case R.id.nav_search:
                selectedFragment = new SearchFragment();
                break;
            case R.id.nav_favorites:
                selectedFragment = new FavoritesFragment();
                break;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                selectedFragment).addToBackStack(selectedFragment.getTag()).commit();
        return true;
    };

}
