package is.ru.tgra.network;

import is.ru.tgra.Point3D;
import is.ru.tgra.Vector3D;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class GameState {
	
	private static GameState instance;
	private HashMap<String, Player> players;
	public String clientNickName;
	
	public static GameState instance(){
		if(instance == null)
			instance = new GameState();
		return instance;
	}
	
	private GameState() {
		this.players = new HashMap<String, Player>();
	}
	
	public synchronized void removePlayer(String name){
		this.players.remove(name);
	}
	
	public synchronized void addPlayer(String name) {
		this.players.put(name, new Player());
	}

	public void updatePlayer(String name, Point3D pos, Vector3D forward, Vector3D up) {
		if(this.players.containsKey(name))
			this.players.get(name).update(pos, forward, up);
	}
	
	public synchronized List<Player> getPlayers(){
		LinkedList<Player> players = new LinkedList<Player>();
		for(Player b : this.players.values())
            players.add(b);
		return players;
	}
}