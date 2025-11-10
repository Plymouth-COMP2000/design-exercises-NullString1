package uk.ac.plymouth.danielkern.comp2000.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

import uk.ac.plymouth.danielkern.comp2000.R;
import uk.ac.plymouth.danielkern.comp2000.data.MenuDatabaseSingleton;
import uk.ac.plymouth.danielkern.comp2000.data.MenuItem;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {

    private MenuItem[] menuItems;
    private String category;

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
            Bundle args = new Bundle();
            args.putInt("itemId", item.getId());
            args.putString("category", category);
            Navigation.findNavController(holder.itemView).navigate(R.id.action_menuFragment_to_editMenuItemFragment, args);
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
