package uk.ac.plymouth.danielkern.comp2000.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;

import uk.ac.plymouth.danielkern.comp2000.R;
import uk.ac.plymouth.danielkern.comp2000.data.Menu;
import uk.ac.plymouth.danielkern.comp2000.data.MenuItem;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {

    private MenuItem[] menuItems;

    public MenuAdapter(MenuItem[] menuItems){
        this.menuItems = menuItems;
    }

    public void setMenuItems(MenuItem[] menuItems) {
        this.menuItems = menuItems;
        notifyDataSetChanged();
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
    }

    @Override
    public int getItemCount() {
        return menuItems.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemName, itemDescription;
        public ViewHolder(android.view.View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            itemDescription = itemView.findViewById(R.id.itemDesc);
        }
    }
}
