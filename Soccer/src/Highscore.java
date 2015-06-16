import java.io.Serializable;
import java.util.ArrayList;


public class Highscore implements Serializable{
	
	public Player player1;
	public Player player2;
	private ArrayList<Player> PlayerList = new ArrayList<Player>();
	
	public Highscore(Player player1, Player player2){
		this.player1 = player1;
		this.player2 = player2;
		
		if(!playerExists(player1)){
			PlayerList.add(player1);
		}
		if(!playerExists(player2)){
			PlayerList.add(player2);
		}
	}

	public boolean playerExists(Player player){
		for(int i = 0; i < PlayerList.size(); i++){
			if(PlayerList.get(i).username == player.username)
				return false;
			}
		return true;
	}
	
}
