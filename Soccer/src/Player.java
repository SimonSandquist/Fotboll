import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class Player implements Runnable {
	
	public ObjectInputStream ois;
	public ObjectOutputStream oos;
	public boolean trueorfalse;
	public gameRoom gameroom;
	public boolean turn = false;
	public boolean winner = false;
	public String username;
	
	public Player() {/*Empty*/}
	
	public Player(boolean startgamemessage, ObjectInputStream ois, ObjectOutputStream oos, String username){
		this.ois = ois;
		this.oos = oos;
		this.trueorfalse = startgamemessage;
		this.username = username;
		System.out.println("My username: " + username + "AnySpaces?");
	}
	
	
	public void sendMessage(Object msg)
	{
		try
		{
			oos.writeObject(msg);
			oos.flush();			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	@Override
	public void run() {
		while(true){
			try{
				Object object = ois.readObject();
				Message message = (Message)object;
				gameroom.quelist.add(message);
				
			}//end of try
			catch(Exception e){
				e.printStackTrace();
			}//end of catch
		}
		
	}
}
