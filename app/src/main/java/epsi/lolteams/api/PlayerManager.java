package epsi.lolteams.api;

import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import fr.hadriel.serial.struct.StInt;
import fr.hadriel.serial.struct.StList;
import fr.hadriel.serial.struct.StObject;
import fr.hadriel.serial.struct.StString;
import fr.hadriel.serial.struct.StructEntry;

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
        StructEntry root = StructEntry.deserialize(in);
        if(root instanceof StList) {
            StList list = (StList) root;
            Log.i("PlayerManager", "Extracting " + list.count() + " Players");
            for(StructEntry entry : list) {
                if(!(entry instanceof StObject))
                    continue;
                StObject object = (StObject) entry;
                //resolve each member
                StructEntry member;
                member = object.get(Player.NAME);
                String name = member instanceof StString ? ((StString) member).get() : null;
                member = object.get(Player.ID);
                int id = member instanceof StInt ? ((StInt) member).value : -1;
                member = object.get(Player.SUMMONER_LEVEL);
                int summonerLevel = member instanceof StInt ? ((StInt) member).value : 0;
                member = object.get(Player.TIER);
                int tier = member instanceof StInt ? ((StInt) member).value : 0;
                member = object.get(Player.DIVISION);
                int division = member instanceof StInt ? ((StInt) member).value : 0;
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
        StList list = new StList();
        //Serialize each Player to the list
        for(Player p : players) {
            //Try to filter any corrupted Players
            if(p.getName() == null || p.getId() < 0) {
                Log.w("PlayerManager", "Weird Player Entry (null name or invalid id) : Not saved");
                continue;
            }
            StObject obj = new StObject();
            obj.put(Player.NAME, p.getName());
            obj.put(Player.ID, p.getId());
            obj.put(Player.SUMMONER_LEVEL, p.getSummonerLevel());
            obj.put(Player.TIER, p.getTier());
            obj.put(Player.DIVISION, p.getDivision());
            list.add(obj);
            Log.i("PlayerManager", "Serialized Player " + p.getName());
        }
        list.serialize(out); // write to the file.
    }
}
