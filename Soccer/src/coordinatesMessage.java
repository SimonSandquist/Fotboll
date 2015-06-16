
public class coordinatesMessage extends Message{
	public int x;
	public int y;
	public int currentx;
	public int currenty;
	public String ID;
	public boolean turn;
	
	public boolean[] avButtons = new boolean[8];
	
	public coordinatesMessage(int x, int y, String playerID){
		this.x = x;
		this.y = y;
		ID = playerID;
		turn = true;
	}
	
}
