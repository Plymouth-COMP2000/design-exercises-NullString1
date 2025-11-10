package uk.ac.plymouth.danielkern.comp2000.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Locale;

import uk.ac.plymouth.danielkern.comp2000.R;
import uk.ac.plymouth.danielkern.comp2000.data.MenuDatabaseSingleton;
import uk.ac.plymouth.danielkern.comp2000.data.MenuItem;

public class EditMenuItemFragment extends Fragment {

    ImageView itemImageView;
    private ActivityResultLauncher<Intent> imagePickLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result ->{
        if (result.getResultCode() == Activity.RESULT_OK){
            Intent data = result.getData();
            if (data != null && data.getData() != null) {
                Uri imageUri = data.getData();
                itemImageView.setImageURI(imageUri);
                Toast.makeText(requireContext(), "Image selected", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "No image selected", Toast.LENGTH_SHORT).show();
            }
        }
    });

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_menu_item, container, false);
        if (getArguments() == null) {
            requireActivity().getOnBackPressedDispatcher().onBackPressed();
            return view;
        }
        MenuItem item = MenuDatabaseSingleton.getInstance(requireContext()).db.getItem(getArguments().getInt("itemId"));
        if (item == null) {
            requireActivity().getOnBackPressedDispatcher().onBackPressed();
            return view;
        }
        TextView itemNameTextView = view.findViewById(R.id.itemNameView);
        TextInputEditText itemNameEditText = view.findViewById(R.id.itemNameET);
        TextInputEditText itemPriceEditText = view.findViewById(R.id.itemPriceET);
        TextInputEditText itemDescriptionEditText = view.findViewById(R.id.itemDescET);
        itemImageView = view.findViewById(R.id.itemImageV);
        Button saveChangesB = view.findViewById(R.id.saveChangeB);
        Button deleteItemB = view.findViewById(R.id.deleteItemB);
        MaterialAutoCompleteTextView itemCategoryChooser = view.findViewById(R.id.itemCategory);

        itemNameTextView.setText(String.format(Locale.getDefault(), "Editing Item - %s", item.getName()));
        itemNameEditText.setText(item.getName());
        itemPriceEditText.setText(String.valueOf(item.getPrice()));
        itemDescriptionEditText.setText(item.getDescription());
        itemImageView.setImageDrawable(item.getImage());

        deleteItemB.setOnClickListener(l -> {
            MenuDatabaseSingleton.getInstance(getContext()).db.deleteItem(item.getId());
            Bundle bundle = new Bundle();
            bundle.putInt("changedItemId", item.getId());
            String resultKey = "itemChanged_" + item.getCategoryName();
            requireActivity().getSupportFragmentManager().setFragmentResult(resultKey, bundle);
            getParentFragmentManager().popBackStack();
        });

        String[] categories = MenuDatabaseSingleton.getInstance(requireContext()).db.getCategories();
        itemCategoryChooser.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, categories));
        itemCategoryChooser.setOnFocusChangeListener((l, hasFocus) -> {
            if (hasFocus)
                itemCategoryChooser.showDropDown();
        });
        itemCategoryChooser.setText(item.getCategoryName(), false);

        itemNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                itemNameTextView.setText(String.format(Locale.getDefault(), "New Item - %s", editable.toString()));
            }
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        });

        itemImageView.setOnClickListener(l -> {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK);
            galleryIntent.setType("image/*");
            imagePickLauncher.launch(galleryIntent);
        });

        saveChangesB.setOnClickListener(l -> {
            String selectedCategory = itemCategoryChooser.getText() == null ? "" : itemCategoryChooser.getText().toString();
            if (selectedCategory.isEmpty()) {
                Toast.makeText(requireContext(), "Please select a category", Toast.LENGTH_SHORT).show();
                return;
            }
            String itemName = itemNameEditText.getText() == null ? "" : itemNameEditText.getText().toString();
            String itemPrice = itemPriceEditText.getText() == null ? "" : itemPriceEditText.getText().toString();
            String itemDescription = itemDescriptionEditText.getText() == null ? "" : itemDescriptionEditText.getText().toString();
            if (itemName.isEmpty() || itemPrice.isEmpty() || itemDescription.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill out all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            String oldCategory = item.getCategoryName();
            item.setName(itemName);
            item.setDescription(itemDescription);
            item.setPrice(Float.parseFloat(itemPrice));
            item.setCategoryName(selectedCategory);
            item.setImage(itemImageView.getDrawable());
            MenuDatabaseSingleton.getInstance(requireContext()).db.updateItem(item);
            Toast.makeText(requireContext(), "Changes saved", Toast.LENGTH_SHORT).show();

            Bundle bundle = new Bundle();
            bundle.putInt("changedItemId", item.getId());
            String resultKey = "itemChanged_" + selectedCategory;
            requireActivity().getSupportFragmentManager().setFragmentResult(resultKey, bundle);

            if (!oldCategory.equals(selectedCategory)) {
                String oldResultKey = "itemChanged_" + oldCategory;
                requireActivity().getSupportFragmentManager().setFragmentResult(oldResultKey, bundle);
            }

            getParentFragmentManager().popBackStack();
        });

        return view;
    }
}