
public class startgameMessage extends Message{

	public boolean singleplayer;
	public String username;

	/**
	 * 
	 * @param trueOrFalse True if single player or false if multiplayer
	 * @param username The username for a player
	 */
	public startgameMessage(boolean trueOrFalse, String username)
	{
		singleplayer = trueOrFalse;
		this.username = username;
	}
}
