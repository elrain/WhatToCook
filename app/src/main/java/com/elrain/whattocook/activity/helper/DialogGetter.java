package com.elrain.whattocook.activity.helper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.elrain.whattocook.R;
import com.elrain.whattocook.dal.helper.AmountTypeHelper;
import com.elrain.whattocook.dao.NamedEntity;

import java.util.List;

/**
 * Created by Denys.Husher on 03.06.2015.
 */
public class DialogGetter {
    private static final DialogInterface.OnClickListener CANCEL_LISTENER = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    };

    public static AlertDialog insertQuantityDialog(Context context, long ingridientId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Введите количество");
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_quantity, null);
        builder.setView(view);
        final EditText etQuantity = (EditText) view.findViewById(R.id.etQuantity);
        final Spinner spType = (Spinner) view.findViewById(R.id.spAmountType);

        AmountTypeHelper amountTypeHelper = new AmountTypeHelper(context);
        final List<NamedEntity> types = amountTypeHelper.getTypesForGroup(ingridientId);
        spType.setAdapter(getAdapter(context, types));

        builder.setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int quantity = 0;
                try {
                    quantity = Integer.parseInt(etQuantity.getText().toString());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                long typeId = types.get(spType.getSelectedItemPosition()).getId();
            }
        });

        builder.setNegativeButton("Отмена", CANCEL_LISTENER);
        return builder.create();
    }

    private static ArrayAdapter getAdapter(Context context, List<NamedEntity> types) {
        String[] names = new String[types.size()];
        for (int index = 0; index < types.size(); ++index)
            names[index] = types.get(index).getName();

        return new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, names);
    }

    public static AlertDialog initDataNeededDialog(Context context, DialogInterface.OnClickListener listener){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setTitle(context.getString(R.string.dialog_title_recipe));
        builder.setMessage(context.getString(R.string.dialog_message_recipes_not_found));
        builder.setPositiveButton(context.getString(R.string.dialog_button_positive_yes), listener);
        builder.setNegativeButton(context.getString(R.string.dialog_button_negative_no), CANCEL_LISTENER);
        return builder.create();
    }

    public static AlertDialog NoInternetDialog(Context context, DialogInterface.OnClickListener listener){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setTitle(context.getString(R.string.dialog_title_internet));
        builder.setMessage(context.getString(R.string.dialog_message_no_internet));
        builder.setPositiveButton(context.getString(R.string.dialog_button_positive_yes), listener);
        builder.setNegativeButton(context.getString(R.string.dialog_button_negative_no), CANCEL_LISTENER);
        return builder.create();
    }
}
