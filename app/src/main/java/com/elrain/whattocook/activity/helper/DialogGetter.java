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
import com.elrain.whattocook.dal.DbHelper;
import com.elrain.whattocook.dal.helper.AmountTypeHelper;
import com.elrain.whattocook.dal.helper.CurrentSelectedHelper;
import com.elrain.whattocook.dao.IngridientsEntity;
import com.elrain.whattocook.dao.NamedEntity;
import com.elrain.whattocook.dao.SelectedIngridientsEntity;
import com.elrain.whattocook.message.CommonMessage;

import java.util.List;

import de.greenrobot.event.EventBus;

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

    public static AlertDialog insertQuantityDialog(Context context, final SelectedIngridientsEntity ingridient) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setTitle(context.getString(R.string.dialog_title_input_quantity));
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_quantity, null);
        builder.setView(view);
        final EditText etQuantity = (EditText) view.findViewById(R.id.etQuantity);
        final Spinner spType = (Spinner) view.findViewById(R.id.spAmountType);
        etQuantity.setText(String.valueOf(ingridient.getQuantity()));
        final List<NamedEntity> types = AmountTypeHelper.getTypesForGroup(
                DbHelper.getInstance(context).getReadableDatabase(),ingridient.getIngridientsEntity().getIdGroup());
        spType.setAdapter(getAdapter(context, types));
        spType.setSelection((int)ingridient.getIdAmountType());
        builder.setPositiveButton(context.getString(R.string.dialog_button_add), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int quantity = 0;
                try {
                    quantity = Integer.parseInt(etQuantity.getText().toString());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                long typeId = types.get(spType.getSelectedItemPosition()).getId();

                EventBus.getDefault().post(new CommonMessage(CommonMessage.MessageEvent.INGRIDIENT_ADDED, new SelectedIngridientsEntity(0, typeId, quantity, ingridient.getIngridientsEntity())));
            }
        });

        builder.setNegativeButton(context.getString(R.string.dialog_button_cancel), CANCEL_LISTENER);
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

    public static AlertDialog noInternetDialog(Context context, DialogInterface.OnClickListener listener){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setTitle(context.getString(R.string.dialog_title_internet));
        builder.setMessage(context.getString(R.string.dialog_message_no_internet));
        builder.setPositiveButton(context.getString(R.string.dialog_button_positive_yes), listener);
        builder.setNegativeButton(context.getString(R.string.dialog_button_negative_no), CANCEL_LISTENER);
        return builder.create();
    }

    public static AlertDialog noInternetDialogSecond(Context context, DialogInterface.OnClickListener listener){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setTitle(context.getString(R.string.dialog_title_internet));
        builder.setMessage(context.getString(R.string.dialog_message_no_internet));
        builder.setPositiveButton(context.getString(R.string.dialog_button_positive_yes), listener);
        builder.setNegativeButton(context.getString(R.string.dialog_button_negative_no), listener);
        return builder.create();
    }

    public static AlertDialog incorrectCredentials(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setTitle(context.getString(R.string.dialog_title_login));
        builder.setMessage(context.getString(R.string.dialog_message_incorrect_credentials));
        builder.setNegativeButton(context.getString(R.string.dialog_button_cancel), CANCEL_LISTENER);
        return builder.create();
    }

    public static AlertDialog noServerConnection(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setTitle(context.getString(R.string.dialog_title_login));
        builder.setMessage(context.getString(R.string.dialog_message_server_unavailable));
        builder.setNegativeButton(context.getString(R.string.dialog_button_close), CANCEL_LISTENER);
        return builder.create();
    }

    public static AlertDialog userRegistered(Context context, DialogInterface.OnClickListener listener){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setTitle(context.getString(R.string.dialog_title_registration));
        builder.setMessage(context.getString(R.string.dialog_message_user_registered));
        builder.setNegativeButton(context.getString(R.string.dialog_button_close), listener);
        return builder.create();
    }

    public static AlertDialog userAlreadyExists(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setTitle(context.getString(R.string.dialog_title_registration));
        builder.setMessage(context.getString(R.string.dialog_message_user_already_exists));
        builder.setNegativeButton(context.getString(R.string.dialog_button_close), CANCEL_LISTENER);
        return builder.create();
    }

    public static AlertDialog logoutDilog(Context context, DialogInterface.OnClickListener listener){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setTitle(context.getString(R.string.dialog_title_logout));
        builder.setMessage(context.getString(R.string.dialog_message_logout));
        builder.setPositiveButton(context.getString(R.string.dialog_button_positive_yes), listener);
        builder.setNegativeButton(context.getString(R.string.dialog_button_negative_no), CANCEL_LISTENER);
        return builder.create();
    }
}
