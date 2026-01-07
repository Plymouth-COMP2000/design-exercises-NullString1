package uk.ac.plymouth.danielkern.comp2000.adapter;

import static android.content.Context.MODE_PRIVATE;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Locale;

import uk.ac.plymouth.danielkern.comp2000.R;
import uk.ac.plymouth.danielkern.comp2000.data.MenuItem;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {

    private MenuItem[] menuItems;
    private final String category;

    private boolean isManager;

    public MenuAdapter(MenuItem[] menuItems){
        this.menuItems = menuItems;
        category = menuItems[0].getCategoryName();
    }

    public void setMenuItems(MenuItem[] menuItems) {
        this.menuItems = menuItems;
        notifyDataSetChanged();
    }

    public boolean updateItem(MenuItem item) {
        for (int i = 0; i < menuItems.length; i++) {
            if (menuItems[i].getId() == item.getId()) {
                menuItems[i] = item;
                notifyItemChanged(i);
                return true;
            }
        }
        return false;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item, parent, false);
        isManager = parent.getContext().getSharedPreferences("user_prefs", MODE_PRIVATE).getString("user_type", "").equals("MANAGER");
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MenuItem item = menuItems[position];
        holder.itemName.setText(item.getName());
        holder.itemDescription.setText(item.getDescription());
        holder.itemImage.setImageDrawable(item.getImage());
        holder.itemPrice.setText(String.format(Locale.getDefault(), "£%.2f", item.getPrice()));
        holder.layout.setOnClickListener(l -> {
            if (isManager) {
                Bundle args = new Bundle();
                args.putInt("itemId", item.getId());
                args.putString("category", category);
                Navigation.findNavController(holder.itemView).navigate(R.id.action_menuFragment_to_editMenuItemFragment, args);
            }
        });
    }

    @Override
    public int getItemCount() {
        return menuItems.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView itemName, itemDescription, itemPrice;
        final ImageView itemImage;
        final ConstraintLayout layout;

        public ViewHolder(android.view.View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            itemDescription = itemView.findViewById(R.id.itemDesc);
            itemPrice = itemView.findViewById(R.id.itemPrice);
            itemImage = itemView.findViewById(R.id.itemImage);
            layout = (ConstraintLayout) itemImage.getParent();
        }
    }
}
