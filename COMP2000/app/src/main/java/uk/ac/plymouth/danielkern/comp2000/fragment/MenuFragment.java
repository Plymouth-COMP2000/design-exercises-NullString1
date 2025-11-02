package uk.ac.plymouth.danielkern.comp2000.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.Arrays;

import uk.ac.plymouth.danielkern.comp2000.R;
import uk.ac.plymouth.danielkern.comp2000.adapter.MenuPagerAdapter;
import uk.ac.plymouth.danielkern.comp2000.data.MenuItem;
import uk.ac.plymouth.danielkern.comp2000.data.MenuSingleton;

public class MenuFragment extends Fragment {

    final MenuSingleton menuSingleton = MenuSingleton.getInstance();
    private static final String TAG = "MenuFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }

    public void testMenu() {
        if (!menuSingleton.isEmpty()){
            return;
        }
        menuSingleton.addCategory("Starters");
        menuSingleton.addCategory("Mains");
        menuSingleton.addCategory("Desserts");
        ArrayList<MenuItem> starters = new ArrayList<>(Arrays.asList(new MenuItem("Bruschetta", "Grilled bread topped with diced tomatoes, garlic, basil, and olive oil.", 5.00F, ResourcesCompat.getDrawable(getResources(), R.drawable.bruschetta, null)),
                new MenuItem("Stuffed Mushrooms", "Mushroom caps filled with a savory mixture of cheese, herbs, and breadcrumbs.", 6.50F, ResourcesCompat.getDrawable(getResources(), R.drawable.stuffed_mushrooms, null))));
        ArrayList<MenuItem> mains = new ArrayList<>(Arrays.asList(
                new MenuItem("Margherita Pizza", "Classic pizza with tomato sauce, mozzarella, and fresh basil.", 10.50F, ResourcesCompat.getDrawable(getResources(), R.drawable.margherita, null)),
                new MenuItem("Spaghetti Bolognese", "Traditional Italian pasta with rich meat sauce.", 12.75F, ResourcesCompat.getDrawable(getResources(), R.drawable.spaghetti_bolognese, null))
        ));
        ArrayList<MenuItem> desserts = new ArrayList<>(Arrays.asList(
                new MenuItem("Tiramisu", "Layered Italian dessert with coffee-soaked ladyfingers, mascarpone cheese, and cocoa powder.", 6.00F, ResourcesCompat.getDrawable(getResources(), R.drawable.tiramisu, null)),
                new MenuItem("Panna Cotta", "Creamy Italian dessert topped with fresh berries or fruit sauce.", 5.50F, ResourcesCompat.getDrawable(getResources(), R.drawable.panna_cotta, null))
        ));
        menuSingleton.addItemsToCategory("Starters", starters);
        menuSingleton.addItemsToCategory("Mains", mains);
        menuSingleton.addItemsToCategory("Desserts", desserts);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        testMenu();
        TabLayout menuTabber = view.findViewById(R.id.menuTabber);
        ViewPager2 viewPager = view.findViewById(R.id.menuViewPager);

        ArrayList<String> categories = new ArrayList<>(menuSingleton.getCategories());
        Log.d(TAG, "categories=" + categories);

        MenuPagerAdapter pagerAdapter = new MenuPagerAdapter(requireActivity(), categories);
        viewPager.setAdapter(pagerAdapter);

        if (!categories.isEmpty()) viewPager.setCurrentItem(0, false);

        viewPager.setUserInputEnabled(true);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Log.d(TAG, "pageSelected=" + position + " -> " + (position < categories.size() ? categories.get(position) : ""));
            }
        });

        new TabLayoutMediator(menuTabber, viewPager, (tab, position) -> tab.setText(categories.get(position))).attach();
    }
}
