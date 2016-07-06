package epsi.lolteams.api;

/**
 * Created by HaDriel on 05/07/2016.
 */
public class Player {

    public static final String NAME = "name";
    public static final String ID = "id";
    public static final String SUMMONER_LEVEL = "summonerLevel";
    public static final String TIER = "tier";
    public static final String DIVISION = "division";

    private String name;
    private int id;
    private int summonerLevel;
    private int tier;
    private int division;

    public Player(String name, int id, int summonerLevel, int tier, int division) {
        this.name = name;
        this.id = id;
        this.summonerLevel = summonerLevel;
        this.tier = tier;
        this.division = division;
    }

    public int getPlayerMMR() {
        return summonerLevel + Rank.getRankMMR(tier, division);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public int getSummonerLevel() {
        return summonerLevel;
    }

    public void setSummonerLevel(int summonerLevel) {
        this.summonerLevel = summonerLevel;
    }

    public int getTier() {
        return tier;
    }

    public void setTier(int tier) {
        this.tier = tier;
    }

    public int getDivision() {
        return division;
    }

    public void setDivision(int division) {
        this.division = division;
    }
}
