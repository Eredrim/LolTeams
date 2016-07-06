package epsi.lolteams;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.Switch;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import epsi.lolteams.api.Player;
import epsi.lolteams.fragments.SearchPlayerDialogFragment;

public class MainActivity extends AppCompatActivity {

    public static final String CACHE_FILE_NAME = "players.json";
    private File cacheFile;

    private Context activity;
    private List listItems = new ArrayList<>();
    private ArrayAdapter<String> listAdapter;
    private ListView listSummoner;
    private ArrayList<String> listMatchmaking = new ArrayList<>();
    private Button matchMakingButton;
    private Switch teamSizeSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cacheFile = new File(getFilesDir(), CACHE_FILE_NAME);
        //Manager initialization
        try {
            if (!cacheFile.exists())
                cacheFile.createNewFile();
            //Load Cache
            LoLTeams.getInstance().playerManager.load(new FileInputStream(cacheFile));
        } catch (IOException ignore) {}

        listItems = new ArrayList<String>();
        List<Player> players = LoLTeams.getInstance().playerManager.getPlayers();
        for(Player p : players) {
            listItems.add(p.getName());
        }

        teamSizeSwitch = (Switch) findViewById(R.id.switch1);
        listSummoner = (ListView) findViewById(R.id.listView);
        matchMakingButton = (Button) findViewById(R.id.buttonMatchmaking);

        listAdapter =new
                ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                listItems);

        listSummoner.setAdapter(listAdapter);
        listSummoner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectSummoner(adapterView, view, i, l);
            }
        });

        matchMakingButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if(listMatchmaking.size() == 0)
                    return;
                Intent intent = new Intent(MainActivity.this, MatchMakingActivity.class);
                intent.putStringArrayListExtra("players", listMatchmaking);
                intent.putExtra("mode", teamSizeSwitch.isChecked());
                MainActivity.this.startActivity(intent);
            }
        });


    }

    protected void onStop() {
        super.onStop();
        try {
            if (!cacheFile.exists())
                cacheFile.createNewFile();
            LoLTeams.getInstance().playerManager.save(new FileOutputStream(cacheFile));
        } catch (IOException ignore) {}
    }

    protected void onDestroy() {
        super.onDestroy();
        try {
            if (!cacheFile.exists())
                cacheFile.createNewFile();
            LoLTeams.getInstance().playerManager.save(new FileOutputStream(cacheFile));
        } catch (IOException ignore) {}
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
        return listItems;
    }

    public void setListItems(List listItems) {
        this.listItems = (List<String>) listItems;
        listAdapter.notifyDataSetChanged();
    }

    public void selectSummoner(AdapterView<?> adapterView, View view, int i, long l) {
        String summoner = (String) adapterView.getItemAtPosition(i);
        if (!isSummonerInListMatchmaking(summoner)) {
            listMatchmaking.add(summoner);
            listSummoner.getChildAt(i).setBackgroundColor(Color.GREEN);
        }
        else {
            listMatchmaking.remove(summoner);
            listSummoner.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
        }
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
