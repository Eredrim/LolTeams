package epsi.lolteams.api;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HaDriel on 05/07/2016.
 */
public class MatchMaker {

    public static Player selectStrongestPlayer(List<Player> list) {
        if(list.size() == 0)
            return null;
        Player player = list.get(0);
        int mmr = player.getPlayerMMR();
        for(Player p : list) {
            int pmmr = p.getPlayerMMR();
            if(pmmr > mmr) {
                mmr = pmmr;
                player = p;
            }
        }
        return player;
    }

    public static Team selectWeakestTeam(List<Team> list) {
        if(list.size() == 0)
            throw new IllegalArgumentException("Player list is null");

        Team team = null;
        int mmr = 0;

        boolean allFull = true;
        //Check if every team is full
        for(Team t : list) {
            allFull = t.isFull();
            if(!allFull) {
                team = t;
                mmr = team.getTeamMMR();
                break;
            }
        }
        if(allFull)
            return null;

        for(Team t : list) {
            if(t.isFull()) // ignore full teams
                continue;

            int tmmr = t.getTeamMMR();
            if(tmmr < mmr) {
                mmr = tmmr;
                team = t;
            }
        }
        return team;
    }

    public static int findTeamCount(int playerCount, int teamSize) {
        return (int) Math.ceil((float) playerCount / (float) teamSize);
    }

    public static List<Team> generate(List<Player> players, int teamSize) {
        //Generate the Team list
        int teamCount = findTeamCount(players.size(), teamSize);
        List<Team> teams = new ArrayList<>();
        for(int i = 0; i < teamCount; i++)
            teams.add(new Team(teamSize));

        boolean blocking = false;
        while (!blocking) {
            Player player = selectStrongestPlayer(players);
            Team team = selectWeakestTeam(teams);
            //safe exit
            if(player == null || team == null) {
                blocking = true;
                continue;
            }
            players.remove(player);
            team.addPlayer(player);
        }
        return teams;
    }
}
