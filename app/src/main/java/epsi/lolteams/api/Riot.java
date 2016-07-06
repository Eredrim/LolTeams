package epsi.lolteams.api;

import android.os.AsyncTask;

import net.rithms.riot.api.RiotApi;
import net.rithms.riot.api.RiotApiException;
import net.rithms.riot.constant.Region;
import net.rithms.riot.dto.League.League;
import net.rithms.riot.dto.League.LeagueEntry;
import net.rithms.riot.dto.Summoner.Summoner;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by HaDriel on 03/06/2016.
 */
public class Riot {
    private static Riot instance = new Riot();
    public static Riot getInstance() {
        return instance;
    }

    private final class GetPlayerByName extends AsyncTask<String, Integer, Summoner> {
        protected Summoner doInBackground(String... params) {
            try {
                return api.getSummonerByName(params[0]);
            } catch (RiotApiException ignore) {
                ignore.printStackTrace();
                return null;
            }
        }
    }

    private final class GetPlayerById extends AsyncTask<Long, Integer, Summoner> {
        protected Summoner doInBackground(Long... params) {
            try {
                return api.getSummonerById(params[0]);
            } catch (RiotApiException ignore) {
                ignore.printStackTrace();
                return null;
            }
        }
    }

    private final class GetPlayerBySummoner extends AsyncTask<Summoner, Integer, List<League>> {
        protected List<League> doInBackground(Summoner... params) {
            try {
                return api.getLeagueBySummoner(params[0].getId());
            } catch (RiotApiException ignore) {
                ignore.printStackTrace();
                return null;
            }
        }
    }


    private Timer timer = new Timer();
    private Lock lock = new ReentrantLock();
    private RiotApi api = new RiotApi("1d750187-fe64-45a5-8242-3412e1877e19");


    private Riot() {
        api.setRegion(Region.EUW);
    }

    private void ensureAPItiming() {
        boolean waiting = true;
        while(waiting) { // less than 1 query every 1 seconds

            //lock for only 1 check at the same time
            lock.lock();
            waiting = timer.elapsed() < 1000;
            if(!waiting) {
                timer.reset(); // reset timer and exit while loop.
            }
            lock.unlock();
            if(waiting) { // wait 10 ms to avoid useless CPU burning
                try {
                    Thread.sleep(10);
                } catch (InterruptedException ignore) {}
            }
        }
    }

    public Player getPlayer(long summonerId) {
        try {
            ensureAPItiming();
            GetPlayerById task = new GetPlayerById();
            Summoner summoner = task.execute(summonerId).get();
            return getPlayer(summoner);
        } catch (Exception ignore) {
            return null;
        }
    }

    public Player getPlayer(String name) {
        try {
            ensureAPItiming();
            GetPlayerByName task = new GetPlayerByName();
            Summoner summoner = task.execute(name).get();
            return getPlayer(summoner);
        } catch (Exception ignore) {
            return null;
        }
    }

    public Player getPlayer(Summoner summoner) {
        if(summoner == null)
            return null;
        League soloQueue = null;
        LeagueEntry playerEntry = null;
        try {
            ensureAPItiming();
            GetPlayerBySummoner task = new GetPlayerBySummoner();
            List<League> leagues = task.execute(summoner).get();
            for(League league : leagues) {
                if(league.getQueue().equals("RANKED_SOLO_5x5")) {
                    soloQueue = league;
                    break;
                }
            }

            if(soloQueue != null) {
                for (LeagueEntry entry : soloQueue.getEntries()) {
                    if (entry.getPlayerOrTeamName().equals(summoner.getName()))
                        playerEntry = entry;
                }
            }
        } catch (Exception e) {
            soloQueue = null;
            playerEntry = null;
        }

        int tier = Rank.getTier(soloQueue == null ? null : soloQueue.getTier());
        int division = Rank.getDivision(tier, playerEntry == null ? null : playerEntry.getDivision());

        return new Player(summoner.getName(), (int) summoner.getId(), (int) summoner.getSummonerLevel(), tier, division);
    }
}