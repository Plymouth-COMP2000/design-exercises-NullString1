package uk.ac.plymouth.danielkern.comp2000.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import uk.ac.plymouth.danielkern.comp2000.R;
import uk.ac.plymouth.danielkern.comp2000.adapter.MenuAdapter;
import uk.ac.plymouth.danielkern.comp2000.data.Menu;
import uk.ac.plymouth.danielkern.comp2000.data.MenuItem;

public class MenuFragment extends Fragment {

    RecyclerView menuRecyclerView;

    Menu menu = new Menu();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }

    public void testMenu() {
        if (!menu.isEmpty()){
            return;
        }
        menu.addCategory("Starters");
        menu.addCategory("Mains");
        menu.addCategory("Desserts");
        ArrayList<MenuItem> starters = new ArrayList<>(Arrays.asList(new MenuItem("Bruschetta", "Grilled bread topped with diced tomatoes, garlic, basil, and olive oil.", 5.00F),
                new MenuItem("Stuffed Mushrooms", "Mushroom caps filled with a savory mixture of cheese, herbs, and breadcrumbs.", 6.50F)));
        ArrayList<MenuItem> mains = new ArrayList<>(Arrays.asList(
                new MenuItem("Margherita Pizza", "Classic pizza with tomato sauce, mozzarella, and fresh basil.", 10.50F),
                new MenuItem("Spaghetti Bolognese", "Traditional Italian pasta with rich meat sauce.", 12.75F)
        ));
        ArrayList<MenuItem> desserts = new ArrayList<>(Arrays.asList(
                new MenuItem("Tiramisu", "Layered Italian dessert with coffee-soaked ladyfingers, mascarpone cheese, and cocoa powder.", 6.00F),
                new MenuItem("Panna Cotta", "Creamy Italian dessert topped with fresh berries or fruit sauce.", 5.50F)
        ));
        menu.addItemsToCategory("Starters", starters);
        menu.addItemsToCategory("Mains", mains);
        menu.addItemsToCategory("Desserts", desserts);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        testMenu();
        menuRecyclerView = view.findViewById(R.id.menuRecycler);
        TabLayout menuTabber = view.findViewById(R.id.menuTabber);

        menu.getCategories().forEach((category) -> {
            TabLayout.Tab tab = menuTabber.newTab();
            tab.setText(category);
            menuTabber.addTab(tab);
        });
        TabLayout.Tab currentTab = menuTabber.getTabAt(menuTabber.getSelectedTabPosition());
        MenuAdapter adapter = new MenuAdapter(menu.getItemsByCategory(currentTab.getText().toString()).toArray(new MenuItem[0]));

        menuRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        menuRecyclerView.setAdapter(adapter);

        menuTabber.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String category = tab.getText().toString();
                ArrayList<MenuItem> items = menu.getItemsByCategory(category);
                adapter.setMenuItems(items.toArray(new MenuItem[0]));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }
}
