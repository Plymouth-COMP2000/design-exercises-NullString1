package uk.ac.plymouth.danielkern.comp2000.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import uk.ac.plymouth.danielkern.comp2000.R;
import uk.ac.plymouth.danielkern.comp2000.adapter.MenuAdapter;
import uk.ac.plymouth.danielkern.comp2000.data.MenuItem;

public class MenuFragment extends Fragment {

    RecyclerView menuRecyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MenuItem[] menuItems = {
                new MenuItem("Margherita Pizza", "Classic pizza with tomato sauce, mozzarella, and fresh basil."),
                new MenuItem("Caesar Salad", "Crisp romaine lettuce with Caesar dressing, croutons, and Parmesan cheese."),
                new MenuItem("Spaghetti Bolognese", "Traditional Italian pasta with rich meat sauce."),
                new MenuItem("Grilled Salmon", "Fresh salmon fillet grilled to perfection, served with vegetables."),
                new MenuItem("Chocolate Lava Cake", "Warm chocolate cake with a gooey molten center, served with vanilla ice cream.")
        };
        menuRecyclerView = view.findViewById(R.id.menuRecycler);
        menuRecyclerView.setAdapter(new MenuAdapter(menuItems));

    }
}
