package epsi.lolteams.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import epsi.lolteams.MainActivity;
import epsi.lolteams.R;

public class SearchPlayerDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.search_player_dialog, null);
        builder.setView(dialogView);
        builder.setPositiveButton(R.string.action_add, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MainActivity mainAct = (MainActivity) getActivity();
                        List<String> listItems = mainAct.getListItems();
                        String playerName = ((EditText)dialogView.findViewById(R.id.userInputDialog)).getText().toString();
                        //Appel à l'api de GoatYeah pour savoir si le joueur existe.
                        boolean test = true;
                        if(test){
                            listItems.add(playerName);
                            mainAct.setListItems(listItems);
                        }
                        else
                        {
                            AlertDialog.Builder errDialogBuilder = new AlertDialog.Builder(getActivity());
                            errDialogBuilder.setTitle("Erreur");
                            errDialogBuilder.setMessage("Le joueur n'existe pas.");
                            AlertDialog alertDialog = errDialogBuilder.create();
                            alertDialog.show();
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}