package epsi.lolteams.api;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HaDriel on 05/07/2016.
 */
public class Team {

    private int maxSize;

    private List<Player> players;

    public Team(int maxSize) {
        this.maxSize = maxSize;
        this.players = new ArrayList<>();
    }

    public boolean isFull() {
        return players.size() >= maxSize;
    }

    public int getTeamMMR() {
        int mmr = 0;
        for(Player p : players) {
            mmr += p.getPlayerMMR();
        }
        return mmr;
    }

    public void addPlayer(Player player) {
        if(players.contains(player)) {
            Log.w("Team", "Tried to add a Player twice to a team. Bug ? ");
            return;
        }

        if(!isFull())
            players.add(player);
        else
            Log.w("Team", "Tried to add a Player to a full team. Bug ?");
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }

    public List<Player> getPlayers() {
        return players;
    }
}
