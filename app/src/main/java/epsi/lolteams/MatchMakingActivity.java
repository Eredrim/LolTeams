package epsi.lolteams;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ListMenuItemView;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import epsi.lolteams.api.MatchMaker;
import epsi.lolteams.api.Player;
import epsi.lolteams.api.Team;

public class MatchMakingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_making);

        Intent intent = getIntent();
        ArrayList<String> playerNames = intent.getStringArrayListExtra("players");
        boolean mode = intent.getBooleanExtra("mode", true);
        int teamSize = mode ? 5 : 3; // 5v5 or 3v3

        List<Player> players = new ArrayList<>();
        for(String name : playerNames) {
            Player player = LoLTeams.getInstance().playerManager.getPlayerByName(name);
            if(player == null) {
                Log.w("MatchMaking", "WTF ?! (null player)");
                continue; // WTF ?!
            }
            players.add(player);
        }

        List<Team> teams = MatchMaker.generate(players, teamSize);

        TextView tv = (TextView) this.findViewById(R.id.textView);
        if(tv != null) {
            for (int i = 0; i < teams.size(); i++) {
                Team team = teams.get(i);
                tv.append("TEAM " + i + "\n");
                for (Player p : team.getPlayers()) {
                    tv.append("\t" + p.getName() + "\n");
                }
            }
        }
    }
}
