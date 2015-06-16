import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.ConcurrentLinkedQueue;


public class AI extends Player
{
	
	public AI()
	{
		super.turn = false;
	}
	
	@Override
	public void sendMessage(Object msg)
	{
		if(msg instanceof coordinatesMessage){
			coordinatesMessage cordmsg = (coordinatesMessage)msg;
			if(gameroom.checkMoves(cordmsg.x, cordmsg.y, cordmsg.x, cordmsg.y+1)){
				super.gameroom.quelist.add(new coordinatesMessage(cordmsg.x, cordmsg.y+1, "Player2"));
			}
			else if(gameroom.checkMoves(cordmsg.x, cordmsg.y, cordmsg.x+1, cordmsg.y+1)){
				super.gameroom.quelist.add(new coordinatesMessage(cordmsg.x+1, cordmsg.y+1, "Player2"));
			}
			else if(gameroom.checkMoves(cordmsg.x, cordmsg.y, cordmsg.x-1, cordmsg.y+1)){
				super.gameroom.quelist.add(new coordinatesMessage(cordmsg.x-1, cordmsg.y+1, "Player2"));
			}
			else if(gameroom.checkMoves(cordmsg.x, cordmsg.y, cordmsg.x+1, cordmsg.y)){
				super.gameroom.quelist.add(new coordinatesMessage(cordmsg.x+1, cordmsg.y, "Player2"));
			}
			else if(gameroom.checkMoves(cordmsg.x, cordmsg.y, cordmsg.x-1, cordmsg.y)){
				super.gameroom.quelist.add(new coordinatesMessage(cordmsg.x-1, cordmsg.y, "Player2"));
			}
			else if(gameroom.checkMoves(cordmsg.x, cordmsg.y, cordmsg.x-1, cordmsg.y-1)){
				super.gameroom.quelist.add(new coordinatesMessage(cordmsg.x-1, cordmsg.y-1, "Player2"));
			}
			else if(gameroom.checkMoves(cordmsg.x, cordmsg.y, cordmsg.x, cordmsg.y-1)){
				super.gameroom.quelist.add(new coordinatesMessage(cordmsg.x, cordmsg.y-1, "Player2"));
			}
			else if(gameroom.checkMoves(cordmsg.x, cordmsg.y, cordmsg.x+1, cordmsg.y-1)){
				super.gameroom.quelist.add(new coordinatesMessage(cordmsg.x+1, cordmsg.y-1, "Player2"));
			}
			
		}
	}
	
	@Override
	public void run()
	{
	
	}
	
}
