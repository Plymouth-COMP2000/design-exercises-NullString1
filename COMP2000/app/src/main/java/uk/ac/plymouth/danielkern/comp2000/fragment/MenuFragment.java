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

import uk.ac.plymouth.danielkern.comp2000.R;
import uk.ac.plymouth.danielkern.comp2000.adapter.MenuPagerAdapter;
import uk.ac.plymouth.danielkern.comp2000.data.MenuDatabaseSingleton;
import uk.ac.plymouth.danielkern.comp2000.data.MenuItem;

public class MenuFragment extends Fragment {

    MenuDatabaseSingleton menuSingleton;
    private static final String TAG = "MenuFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        menuSingleton = MenuDatabaseSingleton.getInstance(requireContext());
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }

    public void testMenu() {
        if (menuSingleton.db.countItems() > 0){
            return;
        }
        menuSingleton.db.insertCategory("Starters");
        menuSingleton.db.insertCategory("Mains");
        menuSingleton.db.insertCategory("Desserts");
        MenuItem[] starters = new MenuItem[]{
                new MenuItem("Bruschetta", "Grilled bread topped with diced tomatoes, garlic, basil, and olive oil.", 5.00F, ResourcesCompat.getDrawable(getResources(), R.drawable.bruschetta, null), "Starters"),
                new MenuItem("Stuffed Mushrooms", "Mushroom caps filled with a savory mixture of cheese, herbs, and breadcrumbs.", 6.50F, ResourcesCompat.getDrawable(getResources(), R.drawable.stuffed_mushrooms, null), "Starters")};
        MenuItem[] mains = new MenuItem[]{
                new MenuItem("Margherita Pizza", "Classic pizza with tomato sauce, mozzarella, and fresh basil.", 10.50F, ResourcesCompat.getDrawable(getResources(), R.drawable.margherita, null), "Mains"),
                new MenuItem("Spaghetti Bolognese", "Traditional Italian pasta with rich meat sauce.", 12.75F, ResourcesCompat.getDrawable(getResources(), R.drawable.spaghetti_bolognese, null), "Mains")
        };
        MenuItem[] desserts = new MenuItem[]{
                new MenuItem("Tiramisu", "Layered Italian dessert with coffee-soaked ladyfingers, mascarpone cheese, and cocoa powder.", 6.00F, ResourcesCompat.getDrawable(getResources(), R.drawable.tiramisu, null), "Desserts"),
                new MenuItem("Panna Cotta", "Creamy Italian dessert topped with fresh berries or fruit sauce.", 5.50F, ResourcesCompat.getDrawable(getResources(), R.drawable.panna_cotta, null), "Desserts")
        };
        menuSingleton.db.insertItems(starters);
        menuSingleton.db.insertItems(mains);
        menuSingleton.db.insertItems(desserts);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        testMenu();
        TabLayout menuTabber = view.findViewById(R.id.menuTabber);
        ViewPager2 viewPager = view.findViewById(R.id.menuViewPager);

        String[] categories = menuSingleton.db.getCategories();

        MenuPagerAdapter pagerAdapter = new MenuPagerAdapter(requireActivity(), categories);
        viewPager.setAdapter(pagerAdapter);

        if (categories.length > 0) viewPager.setCurrentItem(0, false);

        viewPager.setUserInputEnabled(true);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Log.d(TAG, "pageSelected=" + position + " -> " + (position < categories.length ? categories[position] : ""));
            }
        });

        new TabLayoutMediator(menuTabber, viewPager, (tab, position) -> tab.setText(categories[position])).attach();
    }
}
