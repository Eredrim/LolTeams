package epsi.lolteams;

import epsi.lolteams.api.PlayerManager;

/**
 * Created by Gilles on 06/07/2016.
 */
public class LoLTeams {
    private static LoLTeams instance = new LoLTeams();
    public static LoLTeams getInstance() {
        return instance;
    }
    /* SINGLETON */


    public final PlayerManager playerManager;

    private LoLTeams() {
        playerManager = new PlayerManager();
    }
}
