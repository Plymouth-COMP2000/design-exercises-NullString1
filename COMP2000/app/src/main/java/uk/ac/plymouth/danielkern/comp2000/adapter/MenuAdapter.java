package uk.ac.plymouth.danielkern.comp2000.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import uk.ac.plymouth.danielkern.comp2000.R;
import uk.ac.plymouth.danielkern.comp2000.data.MenuItem;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {

    private MenuItem[] menuItems;

    private TextView itemName, itemDescription;

    public MenuAdapter(MenuItem[] menuItems) {
        this.menuItems = menuItems;
    }

    public void setMenuItems(MenuItem[] menuItems) {
        this.menuItems = menuItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        itemName = holder.itemView.findViewById(R.id.itemName);
        itemDescription = holder.itemView.findViewById(R.id.itemDesc);

        itemName.setText(menuItems[position].getName());
        itemDescription.setText(menuItems[position].getDescription());
    }

    @Override
    public int getItemCount() {
        return menuItems.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(android.view.View itemView) {
            super(itemView);
        }
    }
}
