package uk.ac.plymouth.danielkern.comp2000.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import uk.ac.plymouth.danielkern.comp2000.R;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavController navController;
    private AppBarConfiguration appBarConfiguration;

    private NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);

        if (navHostFragment == null) {
            throw new IllegalStateException("NavHostFragment not found");
        }
        navController = navHostFragment.getNavController();

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            int destId = destination.getId();
            if (destId != R.id.loginFragment && destId != R.id.registerFragment) {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                if (getSupportActionBar() != null) {
                    getSupportActionBar().show();
                }
            } else {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                if (getSupportActionBar() != null) {
                    getSupportActionBar().hide();
                }
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        updateNavBarPerUserType();

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    if (!navController.popBackStack()) {
                        finish();
                    }
                }
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    public void updateNavBarPerUserType() {
        Menu navMenu = navigationView.getMenu();
        switch (getSharedPreferences("user_prefs", MODE_PRIVATE).getString("user_type", "")) {
            case "GUEST" -> {
                appBarConfiguration = new AppBarConfiguration.Builder(
                        R.id.menuFragment, R.id.guestReservationsFragment, R.id.guestNewReservationFragment,
                        R.id.editMyProfileFragment, R.id.preferencesFragment)
                        .setOpenableLayout(drawerLayout)
                        .build();
                navMenu.findItem(R.id.nav_my_reservations).setVisible(true);
                navMenu.findItem(R.id.nav_new_reservation).setVisible(true);
                navMenu.findItem(R.id.nav_all_reservations).setVisible(false);
                navMenu.findItem(R.id.nav_todays_reservations).setVisible(false);
            }
            case "STAFF" -> {
                appBarConfiguration = new AppBarConfiguration.Builder(
                        R.id.menuFragment, R.id.staffAllResFragment, R.id.staffTodayResFragment,
                        R.id.editMyProfileFragment, R.id.preferencesFragment)
                        .setOpenableLayout(drawerLayout)
                        .build();
                navMenu.findItem(R.id.nav_my_reservations).setVisible(false);
                navMenu.findItem(R.id.nav_new_reservation).setVisible(false);
                navMenu.findItem(R.id.nav_all_reservations).setVisible(true);
                navMenu.findItem(R.id.nav_todays_reservations).setVisible(true);
            }
            default -> {
                appBarConfiguration = new AppBarConfiguration.Builder(
                        R.id.menuFragment, R.id.guestReservationsFragment, R.id.guestNewReservationFragment,
                        R.id.editMyProfileFragment, R.id.preferencesFragment)
                        .setOpenableLayout(drawerLayout)
                        .build();
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_menu) {
            navController.navigate(R.id.menuFragment);
        } else if (id == R.id.nav_my_reservations) {
            navController.navigate(R.id.guestReservationsFragment);
        } else if (id == R.id.nav_new_reservation) {
            navController.navigate(R.id.guestNewReservationFragment);
        } else if (id == R.id.nav_preferences) {
            navController.navigate(R.id.preferencesFragment);
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }
}