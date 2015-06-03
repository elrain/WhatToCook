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
import com.elrain.whattocook.dao.Ingridient;
import com.elrain.whattocook.dao.NamedObject;

import java.util.List;

/**
 * Created by Denys.Husher on 03.06.2015.
 */
public class DialogGetter {

    public static AlertDialog insertQuantityDialog(Context context, long ingridientId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Введите количество");
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_quantity, null);
        builder.setView(view);
        final EditText etQuantity = (EditText) view.findViewById(R.id.etQuantity);
        final Spinner spType = (Spinner) view.findViewById(R.id.spAmountType);

        AmountTypeHelper amountTypeHelper = new AmountTypeHelper(context);
        final List<NamedObject> types = amountTypeHelper.getTypes();
        spType.setAdapter(getAdapter(context, types));

        builder.setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int quantity = Integer.parseInt(etQuantity.getText().toString());
                long typeId = types.get(which).getId();
            }
        });
        return builder.create();
    }

    private static ArrayAdapter getAdapter(Context context, List<NamedObject> types) {
        String[] names = new String[types.size()];
        for (int index = 0; index < types.size(); ++index)
            names[index] = types.get(index).getName();

        return new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, names);
    }
}
