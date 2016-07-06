package epsi.lolteams.api;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by HaDriel on 05/07/2016.
 */
public class PlayerManager {

    private List<Player> players;
    private Lock lock = new ReentrantLock();

    public PlayerManager(FileInputStream cacheFileInputStream) {
        this.players = new ArrayList<>();
        try {
            load(cacheFileInputStream);
        } catch (IOException e) {
            Log.w("PlayerManager", e);
        }
    }

    /**
     *
     * @param name the name of the player
     * @return the Player object related or null
     */
    public Player getPlayerByName(String name) {
        lock.lock();
        Player result = null;
        for(Player p : players) {
            if(name.equalsIgnoreCase(p.getName())) {
                result = p;
                break;
            }
        }
        lock.unlock();
        return result;
    }

    /**
     *
     * @param id the player id
     * @return the Player object related to this id or null
     */
    public Player getPlayerById(int id) {
        lock.lock();
        Player result = null;
        for(Player p : players) {
            if(id == p.getId()) {
                result = p;
                break;
            }
        }
        lock.unlock();
        return result;
    }

    /**
     * Adds the player to the Manager.<br>
     * player is not added if another Player object with the same id is found.
     * @param player the Player object that is added
     */
    public void addPlayer(Player player) {
        Player p = getPlayerById(player.getId());
        if(p == null) {
            lock.lock();
            players.add(player);
            lock.unlock();
        }
    }

    /**
     * Removes the Player object if it's in the players list.
     * @param player the player to remove
     */
    public void removePlayer(Player player) {
        lock.lock();
        players.remove(player);
        lock.unlock();
    }

    /**
     * Removes the Player object if it's in the players list.
     * @param name the player's name
     */
    public void removePlayerByName(String name) {
        Player p = getPlayerByName(name);
        if(p != null)
            removePlayer(p);
    }

    /**
     * Removes the Player object if it's in the players list.
     * @param id the player's id
     */
    public void removePlayerById(int id) {
        Player p = getPlayerById(id);
        if(p != null)
            removePlayer(p);
    }

    private void load(FileInputStream in) throws IOException {
        Log.i("PlayerManager", "Loading cache file");
        JsonParser parser = new JsonParser();
        JsonElement root = parser.parse(new InputStreamReader(in));
        in.close();
        if(root instanceof JsonArray) {
            JsonArray list = (JsonArray) root;
            Log.i("PlayerManager", "Extracting " + list.size() + " Players");
            for(JsonElement entry : list) {
                if(!(entry instanceof JsonObject))
                    continue;
                JsonObject object = (JsonObject) entry;
                //resolve each member
                JsonElement member;
                JsonPrimitive value;
                member = object.get(Player.NAME);
                value = member instanceof JsonPrimitive ? (JsonPrimitive) member : null;
                String name = value == null || !value.isString() ? null : value.getAsString();
                member = object.get(Player.ID);
                value = member instanceof JsonPrimitive ? (JsonPrimitive) member : null;
                int id = value == null || !value.isNumber() ? -1 : value.getAsInt();
                member = object.get(Player.SUMMONER_LEVEL);
                value = member instanceof JsonPrimitive ? (JsonPrimitive) member : null;
                int summonerLevel = value == null || !value.isNumber() ? 0 : value.getAsInt();
                member = object.get(Player.TIER);
                value = member instanceof JsonPrimitive ? (JsonPrimitive) member : null;
                int tier = value == null || !value.isNumber() ? -1 : value.getAsInt();
                member = object.get(Player.DIVISION);
                value = member instanceof JsonPrimitive ? (JsonPrimitive) member : null;
                int division =  value == null || !value.isNumber() ? -1 : value.getAsInt();

                if(name == null || id == -1) {
                    Log.w("PlayerManager", "Warning, incoherent Player registered. Skipping this Entry");
                    continue;
                }
                Log.i("PlayerManager", "Loaded Player " + name);
                addPlayer(new Player(name, id, summonerLevel, tier, division));
            }
        }
        Log.i("PlayerManager", "Done loading cache file");
    }

    private void save(FileOutputStream out) throws IOException {
        JsonArray list = new JsonArray();
        //Serialize each Player to the list
        for(Player p : players) {
            //Try to filter any corrupted Players
            if(p.getName() == null || p.getId() < 0) {
                Log.w("PlayerManager", "Weird Player Entry (null name or invalid id) : Not saved");
                continue;
            }
            JsonObject obj = new JsonObject();
            obj.addProperty(Player.NAME, p.getName());
            obj.addProperty(Player.ID, p.getId());
            obj.addProperty(Player.SUMMONER_LEVEL, p.getSummonerLevel());
            obj.addProperty(Player.TIER, p.getTier());
            obj.addProperty(Player.DIVISION, p.getDivision());
            list.add(obj);
            Log.i("PlayerManager", "Serialized Player " + p.getName());
        }
        String serial = list.toString();
        out.write(serial.getBytes());
        out.flush();
        out.close();
    }
}
