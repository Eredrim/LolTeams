package epsi.lolteams.api;

/**
 * Created by HaDriel on 04/07/2016.
 */
public class Rank {

    public static final String S_BRONZE = "BRONZE";
    public static final String S_SILVER = "SILVER";
    public static final String S_GOLD = "GOLD";
    public static final String S_PLATINIUM = "PLATINIUM";
    public static final String S_DIAMOND = "DIAMOND";
    public static final String S_MASTER = "MASTER";
    public static final String S_CHALLENGER = "CHALLENGER";

    public static final int UNRANKED = 0;
    public static final int BRONZE = 1;
    public static final int SILVER = 2;
    public static final int GOLD = 3;
    public static final int PLATINIUM = 4;
    public static final int DIAMOND = 5;
    public static final int MASTER = 6;
    public static final int CHALLENGER = 7;

    public static final String S_DIVISION_V = "V";
    public static final String S_DIVISION_IV = "IV";
    public static final String S_DIVISION_III = "III";
    public static final String S_DIVISION_II = "II";
    public static final String S_DIVISION_I = "I";

    public static final int DIVISION_V = 5;
    public static final int DIVISION_IV = 4;
    public static final int DIVISION_III = 3;
    public static final int DIVISION_II = 2;
    public static final int DIVISION_I = 1;

    public static int getTier(String tierString) {
        if(S_BRONZE.equals(tierString)) return BRONZE;
        if(S_SILVER.equals(tierString)) return SILVER;
        if(S_GOLD.equals(tierString)) return GOLD;
        if(S_PLATINIUM.equals(tierString)) return PLATINIUM;
        if(S_DIAMOND.equals(tierString)) return DIAMOND;
        if(S_MASTER.equals(tierString)) return MASTER;
        if(S_CHALLENGER.equals(tierString)) return CHALLENGER;
        return UNRANKED;
    }

    public static int getDivision(int tier, String division) {
        //Default division
        if(division == null) return DIVISION_V;
        if(tier == UNRANKED) return DIVISION_V;
        if(tier == CHALLENGER) return DIVISION_V;
        if(tier == MASTER) return DIVISION_V;

        if(S_DIVISION_I.equals(division)) return DIVISION_I;
        if(S_DIVISION_II.equals(division)) return DIVISION_II;
        if(S_DIVISION_III.equals(division)) return DIVISION_III;
        if(S_DIVISION_IV.equals(division)) return DIVISION_IV;
        if(S_DIVISION_V.equals(division)) return DIVISION_V;
        return DIVISION_V;
    }

    public static int getRankMMR(int tier, int division) {
        return tier * 5 + (5 - division);
    }
}