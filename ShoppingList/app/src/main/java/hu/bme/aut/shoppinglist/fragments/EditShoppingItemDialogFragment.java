package hu.bme.aut.shoppinglist.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import hu.bme.aut.shoppinglist.R;
import hu.bme.aut.shoppinglist.data.ShoppingItem;

public class EditShoppingItemDialogFragment extends AppCompatDialogFragment {
    private EditText nameEditText;
    private EditText etPaymentItemDesc;
    private EditText etPrice;
    private Spinner categorySpinner;

    public ShoppingItem modItem;
    public int modPos;

    public static final String TAG = "EditShoppingItemDialogFragment";

    public interface IEditShoppingItemDialogListener {
        void onShoppingItemEdited(ShoppingItem editItem);
    }

    private IEditShoppingItemDialogListener  listener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentActivity activity = getActivity();
        if (activity instanceof IEditShoppingItemDialogListener ) {
            listener = (IEditShoppingItemDialogListener ) activity;
        } else {
            throw new RuntimeException("Activity must implement the IEditPaymentItemDialogListener interface!");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getContext())
                .setTitle(R.string.edit_payment_item)
                .setView(getContentView())
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (isValid()) {
                            listener.onShoppingItemEdited(getShoppingItem());
                        }
                    }

                    private boolean isValid() {
                        return nameEditText.getText().length() > 0;
                    }

                    private ShoppingItem getShoppingItem() {
                        ShoppingItem shoppingItem = new ShoppingItem();
                        shoppingItem.name = nameEditText.getText().toString();
                        shoppingItem.description = etPaymentItemDesc.getText().toString();
                        try {
                            shoppingItem.sum = Integer.parseInt(etPrice.getText().toString());
                        } catch (NumberFormatException e) {
                            shoppingItem.sum = 0;
                        }
                        shoppingItem.category = ShoppingItem.Category.getByOrdinal(categorySpinner.getSelectedItemPosition());

                       return shoppingItem;
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.onShoppingItemEdited(modItem);
                    }
                })
                .create();
    }

    private View getContentView() {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_new_shopping_item, null);
        nameEditText = contentView.findViewById(R.id.ShoppingItemNameEditText);
        nameEditText.setText(modItem.name);
        etPaymentItemDesc = contentView.findViewById(R.id.ShoppingItemDescriptionEditText);
        etPaymentItemDesc.setText(modItem.description);
        etPrice = contentView.findViewById(R.id.ShoppingItemEstimatedPriceEditText);
        etPrice.setText("" + modItem.sum);
        categorySpinner = contentView.findViewById(R.id.ShoppingItemCategorySpinner);
        categorySpinner.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.category_items)));
        return contentView;
    }
}
