package epsi.lolteams;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import net.rithms.riot.dto.Static.ItemList;

import java.util.ArrayList;
import java.util.List;

import epsi.lolteams.fragments.SearchPlayerDialogFragment;

public class MainActivity extends AppCompatActivity {

    private Context activity;
    private List ListItems = new ArrayList<>();
    private ArrayAdapter<String> listAdapter;
    private ListView listSummoner;
    private ArrayList<String> listMatchmaking = new ArrayList<>();
    private Button matchMakingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListItems = new ArrayList<String>() {{ add("A"); add("B"); add("C"); }};
        listAdapter =new
                ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                ListItems);
        ListView myList=
                (ListView) findViewById(R.id.listView);
        myList.setAdapter(listAdapter);

        listSummoner = (ListView) findViewById(R.id.listView);
        listSummoner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectSummoner(adapterView, view, i, l);
            }
        });

        matchMakingButton = (Button) findViewById(R.id.buttonMatchmaking);
        matchMakingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("intent avant");
                Intent intent = new Intent(MainActivity.this, MatchMakingActivity.class);
                intent.putStringArrayListExtra("summonerList", listMatchmaking);
                MainActivity.this.startActivity(intent);
                System.out.println("intent après");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                DialogFragment newFragment = new SearchPlayerDialogFragment();
                newFragment.show(this.getFragmentManager(), "playerSearch");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public Context getActivity() {
        return this.getBaseContext();
    }

    public List<String> getListItems() {
        return ListItems;
    }

    public void setListItems(List listItems) {
        this.ListItems = (List<String>) listItems;
        listAdapter.notifyDataSetChanged();
    }

    public void selectSummoner(AdapterView<?> adapterView, View view, int i, long l) {
        String summoner = (String) adapterView.getItemAtPosition(i);
        if (!isSummonerInListMatchmaking(summoner)) {
            listMatchmaking.add(summoner);
            listSummoner.getChildAt(i).setBackgroundColor(Color.GREEN);
            afficherToast("joueur séléctionné: " + (CharSequence) adapterView.getItemAtPosition(i));
        }
        else {
            listMatchmaking.remove(summoner);
            listSummoner.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
        }
    }

    public void afficherToast(CharSequence text) {
        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.show();
    }

    public boolean isSummonerInListMatchmaking(String summoner) {
        for (int i = 0; i < listMatchmaking.size(); i++) {
            if (listMatchmaking.get(i).equals(summoner)) {
                return true;
            }
        }

        return false;
    }
}
