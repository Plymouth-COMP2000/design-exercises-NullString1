package uk.ac.plymouth.danielkern.comp2000.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import uk.ac.plymouth.danielkern.comp2000.R;
import uk.ac.plymouth.danielkern.comp2000.adapter.MenuAdapter;
import uk.ac.plymouth.danielkern.comp2000.data.MenuDatabaseSingleton;
import uk.ac.plymouth.danielkern.comp2000.data.MenuItem;
import uk.ac.plymouth.danielkern.comp2000.decoration.DividerItemDecoration;

public class MenuPageFragment extends Fragment {
    private static final String ARG_CATEGORY = "arg_category";

    public static MenuPageFragment newInstance(String category) {
        MenuPageFragment f = new MenuPageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CATEGORY, category);
        f.setArguments(args);
        return f;
    }

    private MenuDatabaseSingleton menuSingleton;
    private MenuAdapter adapter;
    private String categoryName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_menu_page, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        menuSingleton = MenuDatabaseSingleton.getInstance(requireContext());
        Log.d("MenuPageFragment", "onCreate: Setting up fragment result listener for itemChanged");
        if (getArguments() == null) {
            Log.e("MenuPageFragment", "onCreate: No arguments provided to MenuPageFragment");
            requireActivity().getOnBackPressedDispatcher().onBackPressed();
            return;
        }
        String resultKey = "itemChanged_"+getArguments().getString(ARG_CATEGORY);
        requireActivity().getSupportFragmentManager().setFragmentResultListener(resultKey, this, (requestKey, result) -> {
            Log.d("MenuPageFragment", "onCreate: itemChanged received for itemId " + result.getInt("changedItemId"));
            MenuItem item = menuSingleton.db.getItem(result.getInt("changedItemId"));
            if (item == null || !adapter.updateItem(item)){
                adapter.setMenuItems(menuSingleton.db.getItemsByCategory(categoryName));
            }
        });
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.menuPageRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        if (getArguments() != null) categoryName = getArguments().getString(ARG_CATEGORY);
        MenuItem[] items = menuSingleton.db.getItemsByCategory(categoryName);
        adapter = new MenuAdapter(items);
        recyclerView.setAdapter(adapter);
        DividerItemDecoration decoration = new DividerItemDecoration(requireContext(), R.drawable.divider);
        recyclerView.addItemDecoration(decoration);
    }
}
