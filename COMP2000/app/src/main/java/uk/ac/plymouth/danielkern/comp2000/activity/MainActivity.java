package uk.ac.plymouth.danielkern.comp2000.activity;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
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

import java.util.Objects;

import uk.ac.plymouth.danielkern.comp2000.R;
import uk.ac.plymouth.danielkern.comp2000.data.DBObserver;
import uk.ac.plymouth.danielkern.comp2000.data.MenuDatabaseSingleton;
import uk.ac.plymouth.danielkern.comp2000.data.ReservationsDatabaseSingleton;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, DBObserver {

    private static final String CHANNEL_ID = "db_update_channel";
    private static final int POST_NOTIFICATIONS_REQUEST_CODE = 1001;

    private DrawerLayout drawerLayout;
    private NavController navController;
    private AppBarConfiguration appBarConfiguration;

    private NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ReservationsDatabaseSingleton.getInstance(this).addObserver(this);
        MenuDatabaseSingleton.getInstance(this).addObserver(this);
        ReservationsDatabaseSingleton.getInstance(this).openDB();
        MenuDatabaseSingleton.getInstance(this).openDB();
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

        toolbar.setNavigationOnClickListener(v -> {
            Integer currentId = navController.getCurrentDestination() != null ? navController.getCurrentDestination().getId() : null;
            if (currentId != null && appBarConfiguration.getTopLevelDestinations().contains(currentId)){
                if (drawerLayout.isDrawerOpen(GravityCompat.START))  {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            } else {
                onSupportNavigateUp();
            }
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

        ensureNotificationPermissionAndChannel();
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
                navMenu.findItem(R.id.nav_staff_management).setVisible(false);
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
                navMenu.findItem(R.id.nav_staff_management).setVisible(false);
            }
            case "MANAGER" -> {
                appBarConfiguration = new AppBarConfiguration.Builder(
                        R.id.menuFragment, R.id.staffAllResFragment, R.id.staffTodayResFragment,
                        R.id.editMyProfileFragment, R.id.preferencesFragment, R.id.staffManagementFragment)
                        .setOpenableLayout(drawerLayout)
                        .build();
                navMenu.findItem(R.id.nav_my_reservations).setVisible(false);
                navMenu.findItem(R.id.nav_new_reservation).setVisible(false);
                navMenu.findItem(R.id.nav_all_reservations).setVisible(true);
                navMenu.findItem(R.id.nav_todays_reservations).setVisible(true);
                navMenu.findItem(R.id.nav_staff_management).setVisible(true);
            }
            default -> appBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.menuFragment, R.id.guestReservationsFragment, R.id.guestNewReservationFragment,
                    R.id.editMyProfileFragment, R.id.preferencesFragment)
                    .setOpenableLayout(drawerLayout)
                    .build();
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
        } else if (id == R.id.nav_all_reservations) {
            navController.navigate(R.id.staffAllResFragment);
        } else if (id == R.id.nav_todays_reservations) {
            navController.navigate(R.id.staffTodayResFragment);
        } else if (id == R.id.nav_staff_management) {
            navController.navigate(R.id.staffManagementFragment);
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ReservationsDatabaseSingleton.getInstance(this).closeDB();
        MenuDatabaseSingleton.getInstance(this).closeDB();
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }

    @Override
    public void onDatabaseChanged(String dbName, Operation operation) {
        runOnUiThread(() -> {
            String message = switch (operation) {
                case INSERT_MENU_ITEM -> "A new item has been added to the menu! Check it out!";
                case UPDATE_MENU_ITEM -> "A menu item has been updated";
                case DELETE_MENU_ITEM -> "A menu item has been removed :(";
                case INSERT_RESERVATION -> "A new reservation has been made!";
                case UPDATE_RESERVATION ->
                        "Changes have been made to one or more of your reservations";
                case DELETE_RESERVATION -> "A reservation has been cancelled :(";
            };
            showNotification(Objects.equals(dbName, "reservations.db") ? "Reservations" : "Menu", message);
        });
    }

    private void createNotificationChannel() {
        CharSequence name = "Reservations and Menu Updates";
        String description = "Receive notifications about menu changes and updates to your reservations";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void ensureNotificationPermissionAndChannel() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.POST_NOTIFICATIONS},
                    POST_NOTIFICATIONS_REQUEST_CODE);
        }
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (nm != null && !nm.areNotificationsEnabled()) {
            Toast.makeText(this, "Notifications are disabled for this app. Enable them in settings.", Toast.LENGTH_LONG).show();
        } else {
            createNotificationChannel();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == POST_NOTIFICATIONS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                createNotificationChannel();
            } else {
                Toast.makeText(this, "Notification permission denied. Notifications will not be shown.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void showNotification(String title, String message) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (nm != null && !nm.areNotificationsEnabled()) return;

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        int notificationId = (int) System.currentTimeMillis();
        if (nm != null) nm.notify(notificationId, builder.build());
    }
}