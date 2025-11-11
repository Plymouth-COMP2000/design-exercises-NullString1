package uk.ac.plymouth.danielkern.comp2000.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import uk.ac.plymouth.danielkern.comp2000.R;
import uk.ac.plymouth.danielkern.comp2000.adapter.MenuPagerAdapter;
import uk.ac.plymouth.danielkern.comp2000.data.MenuDatabaseSingleton;

public class MenuFragment extends Fragment {

    MenuDatabaseSingleton menuSingleton;
    private static final String TAG = "MenuFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        menuSingleton = MenuDatabaseSingleton.getInstance(requireContext());
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TabLayout menuTabber = view.findViewById(R.id.menuTabber);
        ViewPager2 viewPager = view.findViewById(R.id.menuViewPager);

        Button newItemB = view.findViewById(R.id.newItemB);
        if (requireContext().getSharedPreferences("user_prefs", MODE_PRIVATE).getString("user_type", "GUEST").equals("MANAGER")) {
            newItemB.setVisibility(View.VISIBLE);
        } else {
            newItemB.setVisibility(View.GONE);
        }
        newItemB.setOnClickListener(l -> {
            Navigation.findNavController(view).navigate(R.id.action_menuFragment_to_addMenuItemFragment);
        });

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
