import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;


public class gameRoom implements Runnable{

	public Player player1;
	public Player player2;
	public int Width;
	public int Height;
	public ConcurrentLinkedQueue<Message> quelist =  new ConcurrentLinkedQueue<Message>();
	public serverButtons serverboard[][];
	ArrayList<previousMoves> movesList = new ArrayList<previousMoves>();
	public int ballX, ballY;
	public boolean spelare1turn;
	public boolean spelare2turn;
	public ObjectOutputStream oos;
	public Highscore highscore;

	public gameRoom(Player player1, Player player2, int Width, int Height){
		
		this.player1 = player1;
		this.player2 = player2;
		highscore = new Highscore(player1, player2);
		try
		{
			String temp = "Player1";
			player1.sendMessage(temp);

			
			temp = "Player2";
			player2.sendMessage(temp);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		this.player1.gameroom = this;
		this.player2.gameroom = this;
		this.Width = Width;
		this.Height = Height;
		new Thread(player1).start();
		new Thread(player2).start();
		new Thread(this).start();
		drawseverField();
	}

	
	/**
	 * This function draws the field for the server so that the server can make the rules for the game
	 */
	public void drawseverField(){
		
		serverboard = new serverButtons[Width][Height];
		for(int x = 0; x < Width; x++){
			for(int y = 0; y < Height; y++){
				serverboard[x][y] = new serverButtons();
				if((x < Width/2 -1 || x > Width/2 +1) && (y == 0 || y == Height-1)){
					serverboard[x][y].invisible = true;
				}
				if((y == Height-2 || y == 1)  && x == Width/2 ){
					serverboard[x][y].invisible = true;
				}
				if(y == Height/2 && x == Width / 2){
					serverboard[x][y].hasBall = true;
					serverboard[x][y].visited = true;
					
					ballX = x;
					ballY = y;
					System.out.println("CREATINGBOARD: Ball X: " + ballX + " ball Y: " + ballY);
				}
				if((x == 0 || x == Width -1)){
					serverboard[x][y].widthWalls = true;
					serverboard[x][y].visited = true;
				}
				if((y == Height -2 || y == 1))
				{
					serverboard[x][y].heigthWalls = true;
					serverboard[x][y].visited = true;				
				}
				if((x == Width/2 || x == Width/2 -1 || x == Width/2 +1) && (y == 0 || y == Height-1)){
					serverboard[x][y].goal = true;
				}
			}//End of for x
				}//End of for y
	}
	
	/**
	 * This function check so the move that a player are trying to make is around the current position of the ball
	 * @param destX a int witch contains the destination X value  
	 * @param destY a int witch contains the destination Y value  
	 * @param ballX a integear that contains the X value for the current position of the ball
	 * @param ballY a integear that contains the Y value for the current position of the ball
	 * @return
	 */
	public boolean okMove(int destX, int destY, int ballX, int ballY){
	
		if(destX >= 0 && destX < Width && destY >= 0 && destY < Height)
		{
				if(destX >= ballX -1 && destX <= ballX +1){
					if(destY >= ballY -1 && destY <= ballY +1){
						if(!(destX == ballX && destY == ballY)){
							return true;
						}
					}
				}
			
		}
		return false;
	}
	
	/**
	 * Checks so that its the players turn and right ID for the player who sent the message
	 * @param msg contains the string to se that its the ringht player ID
	 * @return
	 */
	private boolean Turn(coordinatesMessage msg)
	{
		if(msg.ID.equals("Player1") && player1.turn)
		{
			return true;
		}
		if(msg.ID.equals("Player2") && player2.turn)
		{
			return true;
		}	
		return false;
	}
	
	/**
	 * Change the turn of player if the move is to an unvisited radiobutton
	 * @param destX integear with the X value, so we can check if the button the player are going to is visited or not
	 * @param destY integear with the Y value, so we can check if the button the player are going to is visited or not
	 * @param cordmsg a boolean that we change so the its the other players turn
	 */
	public void changeStates(int destX, int destY, coordinatesMessage cordmsg){
		if(!serverboard[destX][destY].visited){
			player1.turn = !player1.turn;
			player2.turn = !player2.turn;
			cordmsg.turn = !cordmsg.turn;
		}
	
		serverboard[destX][destY].visited = true;
	}
	
	/**
	 * This function sets a boolean to to true if the buttons around the next position is a valid move.
	 * @param DestX a int with the value of the next position of X
	 * @param DestY a int with the y value witch should be the new position
	 * @param cordmsg the boolean that we sets to true if its a valid move
	 */
	public void AddToMessageAndSend(int DestX, int DestY, coordinatesMessage cordmsg)
	{
		
		if(okMove(DestX-1,DestY-1,DestX,DestY)) //Upper Left
		{
			cordmsg.avButtons[0] = true;
		}
		if(okMove(DestX,DestY-1,DestX,DestY)) //Upper Mid
		{
			cordmsg.avButtons[1] = true;
		}		
		if(okMove(DestX+1,DestY-1,DestX,DestY)) //Upper Right
		{
			cordmsg.avButtons[2] = true;
		}		
		if(okMove(DestX+1,DestY,DestX,DestY)) //Right
		{
			cordmsg.avButtons[3] = true;
		}
		if(okMove(DestX-1,DestY,DestX,DestY)) //Left
		{
			cordmsg.avButtons[4] = true;
		}
		if(okMove(DestX-1,DestY+1,DestX,DestY)) //Bottom Left
		{
			cordmsg.avButtons[5] = true;
		}
		if(okMove(DestX,DestY+1,DestX,DestY)) //Bottom Mid
		{
			cordmsg.avButtons[6] = true;
		}			
		if(okMove(DestX+1,DestY+1,DestX,DestY)) //Bottom Right
		{
			cordmsg.avButtons[7] = true;
		}		
		try
		{
			cordmsg.currentx = ballX;
			cordmsg.currenty = ballY;
			player1.sendMessage(cordmsg);
			player2.sendMessage(cordmsg);
		}
		catch(Exception e){
			e.printStackTrace();
		}			
	}
	
	/**
	 * A function that check so the move a player are about to make don't exists from before
	 * @param destX an int with the X value of the move the player are about to make
	 * @param destY an int with the Y value of the move the player are about to make
	 * @return
	 */
	public boolean notPastMove(int destX, int destY)
	{
		for(int i = 0; i < movesList.size(); i++){
			if((movesList.get(i).previousMoveExists(ballX, ballY, destX, destY))){
				return false;
			}
		}//End of for-loop
		return true;
	}
	
	/**
	 * Checks if the button a player wants to go to is a goal
	 * @param destX destinations x value
	 * @param destY destinations y value
	 */
	public void ifGoal(int destX, int destY){
		 if((serverboard[destX][destY].goal && destY == 0)){
			 player1.winner = true;
			 try{
				 player1.sendMessage(new goalMessage(player1.winner));
				 player2.sendMessage(new goalMessage(player2.winner));
			 }
			 catch(Exception e){
				 e.printStackTrace();
			 }
		 }
		 else if((serverboard[destX][destY].goal && destY == Height -1)){
			 player2.winner = true;
			 try{
				 player1.sendMessage(new goalMessage(player1.winner));
				 player2.sendMessage(new goalMessage(player2.winner));

			 }
			 catch(Exception e){
				 e.printStackTrace();
			 }
		 }
	}
	
	/**
	 * Checks if the move a player wants to make is against a wall
	 * @param destX the destinations X value
	 * @param destY the destinations Y value
	 * @param ballX the balls current X value
	 * @param ballY the balls cueent YH value
	 * @return
	 */
	public boolean WallMove(int destX, int destY, int ballX, int ballY){
		if((serverboard[ballX][ballY].widthWalls && serverboard[destX][destY].widthWalls)){
			return false;
		}
		if((serverboard[ballX][ballY].heigthWalls && serverboard[destX][destY].heigthWalls)){
			return false;
		}
		return true;
	}
	
	/**
	 * checks so that the ball isnt stuck
	 * @param ballX the balls x value
	 * @param ballY the balls y value
	 * @return
	 */
	public boolean stuckBall(int ballX, int ballY){
		
		if(notPastMove(ballX-1,ballY-1))
		{
			return false;
		}
		else if(notPastMove(ballX,ballY-1)){
			return false;
		}
		else if(notPastMove(ballX+1,ballY-1)){
			return false;
		}
		else if(notPastMove(ballX+1,ballY)){
			return false;
		}
		else if(notPastMove(ballX-1,ballY)){
			return false;
		}
		else if(notPastMove(ballX-1,ballY+1)){
			return false;
		}
		else if(notPastMove(ballX,ballY+1)){
			return false;
		}
		else if(notPastMove(ballX+1,ballY+1)){
			return false;
		}

		return true;
		
	}
	
	/**
	 * Checks so that the move a player wants to make is a ok move
	 * @param ballX balls currrent x value
	 * @param ballY balls current y value
	 * @param destX the destinations x value
	 * @param destY the destinations y value
	 * @return
	 */
	public boolean checkMoves(int ballX, int ballY, int destX, int destY){
		if(okMove(destX, destY, ballX, ballY)){
			if(notPastMove(destX, destY)){
				if(WallMove(destX, destY, ballX, ballY)){
					return true;
				}
			}
		}
		return false;
	}
	
	public void writeToFile(){
			
		try{
			FileOutputStream fout = new FileOutputStream("Highscore.txt");
			ObjectOutputStream oos = new ObjectOutputStream(fout);   
			oos.writeObject(highscore);
			oos.close();
			System.out.println("Done");
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void run() {
		while(true){
			if(!quelist.isEmpty()){
				Message message = quelist.poll();
				if(message instanceof coordinatesMessage){
					
					coordinatesMessage mess = (coordinatesMessage) message;
					int destX = mess.x;
					int destY = mess.y;
					if(Turn(mess)){
					
						if(checkMoves(ballX, ballY, destX, destY))
						{
									changeStates(destX, destY, mess);
									AddToMessageAndSend(destX, destY, mess);
									ifGoal(destX, destY);
									serverboard[ballX][ballY].hasBall = false;
									serverboard[destX][destY].hasBall = true;
									movesList.add(new previousMoves(ballX, ballY, destX, destY));
									ballX = destX;
									ballY = destY;
									
									if(stuckBall(ballX, ballY)){
										if(player1.turn){
										
											player2.winner = true;
											player1.sendMessage(new goalMessage(player1.winner));
											player2.sendMessage(new goalMessage(player2.winner));
									
										}
										else{
										
											player1.winner = true;
											player1.sendMessage(new goalMessage(player1.winner));
											player2.sendMessage(new goalMessage(player2.winner));

									}
								}
								
								
								
			
							
						}//End of if okMove
					}
				}
			}
		}//End of while	
	}//End of run
	
}
