package epsi.lolteams;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import epsi.lolteams.fragments.SearchPlayerDialogFragment;

public class MainActivity extends AppCompatActivity {

    private Context activity;
    private List ListItems = new ArrayList<>();
    private ArrayAdapter<String> listAdapter;

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
}
