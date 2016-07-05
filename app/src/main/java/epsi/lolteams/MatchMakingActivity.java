package epsi.lolteams;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class MatchMakingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_making);

        Intent intent = getIntent();
        ArrayList<String> summonerList = intent.getStringArrayListExtra("summonerList");

        for (int i = 0; i < summonerList.size(); i++) {
            System.out.println("MatchMakingActivity: summoner [" + summonerList.get(i) + "]");
        }
    }
}
