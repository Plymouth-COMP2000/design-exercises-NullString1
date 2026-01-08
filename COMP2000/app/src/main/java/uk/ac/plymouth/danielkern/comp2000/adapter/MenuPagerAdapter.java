package uk.ac.plymouth.danielkern.comp2000.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import uk.ac.plymouth.danielkern.comp2000.fragment.MenuPageFragment;

public class MenuPagerAdapter extends FragmentStateAdapter {
    private final String[] categories;

    public MenuPagerAdapter(@NonNull FragmentActivity fragmentActivity, String[] categories) {
        super(fragmentActivity);
        this.categories = categories;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return MenuPageFragment.newInstance(categories[position]);
    }

    @Override
    public int getItemCount() {
        return categories.length;
    }
}

