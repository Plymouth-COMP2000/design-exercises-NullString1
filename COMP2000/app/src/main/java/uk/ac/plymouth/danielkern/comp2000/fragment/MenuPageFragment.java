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

import java.util.ArrayList;

import uk.ac.plymouth.danielkern.comp2000.R;
import uk.ac.plymouth.danielkern.comp2000.adapter.MenuAdapter;
import uk.ac.plymouth.danielkern.comp2000.data.MenuItem;
import uk.ac.plymouth.danielkern.comp2000.data.MenuSingleton;
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

    private final MenuSingleton menuSingleton = MenuSingleton.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_menu_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.menuPageRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        String category = null;
        if (getArguments() != null) category = getArguments().getString(ARG_CATEGORY);
        ArrayList<MenuItem> items = menuSingleton.getItemsByCategory(category);
        MenuAdapter adapter = new MenuAdapter(items.toArray(new MenuItem[0]));
        recyclerView.setAdapter(adapter);
        DividerItemDecoration decoration = new DividerItemDecoration(requireContext(), R.drawable.divider);
        recyclerView.addItemDecoration(decoration);
    }
}
