import java.awt.Color;
import java.awt.Graphics;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.plaf.basic.BasicBorders.RadioButtonBorder;


public class ClientgameRoom extends JFrame  implements Runnable {
	
	public Message servermessage;
	public ObjectInputStream ois;
	public ObjectOutputStream oos;
	
	public Buttons[][] board;
	public String myPlayerID;
	public int width, height;
	
	private ArrayList<previousMoves> LineList = new ArrayList<previousMoves>();
	
	public ClientgameRoom(ObjectInputStream ois, ObjectOutputStream oos){
		this.ois = ois;
		this.oos = oos;
		new Thread(this).start();
	}
	
	/**
	 * This functions draws the game playing area.
	 * @param fielddrawmsg a message that contains the width and height
	 */
	public void drawfield(fielddrawMessage fielddrawmsg){
		this.setSize(750, 750);
		this.getContentPane().setLayout(null);
		this.setVisible(true);
		ButtonGroup group = new ButtonGroup();
		width = fielddrawmsg.Width;
		height = fielddrawmsg.Height;
		
		board = new Buttons[fielddrawmsg.Width][fielddrawmsg.Height];
		for(int x = 0; x < fielddrawmsg.Width; x++){
			for(int y = 0; y < fielddrawmsg.Height; y++){
				board[x][y] = new Buttons(oos, x, y, this);
				this.getContentPane().add(board[x][y]);
				board[x][y].setBounds(10+50 * x, 10 + 50 * y, 25, 25);
				group.add(board[x][y]);
				if((x < width/2 -1 || x > width/2 +1) && (y == 0 || y == height-1)){
					board[x][y].setVisible(false);
				}
				if((y == height-2 || y == 1)  && x == width/2 ){
					board[x][y].setVisible(false);
				}
				if(y == height/2 && x == width / 2){
					board[x][y].setSelected(true);
				}
				
				board[x][y].PressButton();
			}
		}
	}
	
	/**
	 * This function disables the Radiobuttons to the player who waits for his turn
	 * @param trueOrFalse a boolean that check if its a player turn or not
	 */
	public void freezeField(boolean trueOrFalse){
		for(int x = 0; x < width; x++){
			for(int y = 0; y < height; y++){
				board[x][y].setEnabled(trueOrFalse);
				
			}
		}		
	}
	
	/**
	 * A function that sets the Radiobuttons around the player so he knows  where he can go
	 * @param msg a coordinateMessage that contains a boolean so he knows witch are should be enabled
	 */
	public void setAvailableButtons(coordinatesMessage msg)
	{
		if(msg.avButtons[0]) //Upper Left
		{
			board[msg.x-1][msg.y-1].setEnabled(true);
		}
		if(msg.avButtons[1]) //Upper Mid
		{
			board[msg.x][msg.y-1].setEnabled(true);
		}		
		if(msg.avButtons[2]) //Upper Right
		{
			board[msg.x+1][msg.y-1].setEnabled(true);
		}		
		if(msg.avButtons[3]) //Right
		{
			board[msg.x+1][msg.y].setEnabled(true);
		}
		if(msg.avButtons[4]) //Left
		{
			board[msg.x-1][msg.y].setEnabled(true);
		}
		if(msg.avButtons[5]) //Bottom Left
		{
			board[msg.x-1][msg.y+1].setEnabled(true);
		}
		if(msg.avButtons[6]) //Bottom Mid
		{
			board[msg.x][msg.y+1].setEnabled(true);
		}			
		if(msg.avButtons[7]) //Bottom Right
		{
			board[msg.x+1][msg.y+1].setEnabled(true);
		}

	}
	
	
	
	/**
	 * This functions draws the lines between the Radiobutton when a move is made
	 */
	public void paint(Graphics g) {
	    super.paint(g);
	    g.setColor(Color.red);
	    for(int i = 0; i < LineList.size(); i++)
	    {
	    	g.drawLine(LineList.get(i).ballX * 50 + 28,LineList.get(i).ballY * 50 + 53,LineList.get(i).destX * 50 + 28,LineList.get(i).destY * 50 + 53);
	    }
	   

	}
    
	@Override
	public void run() {
		while(true){
			try{		
				Object obj = ois.readObject();
				if(obj instanceof fielddrawMessage){
					fielddrawMessage fielddrawmsg = (fielddrawMessage)obj;
					drawfield(fielddrawmsg);
				}
				if(obj instanceof String)
				{
					myPlayerID = (String) obj;
					System.out.println("I got my played ID: " + myPlayerID);

				}
				if(obj instanceof coordinatesMessage){
					coordinatesMessage cordmsg = (coordinatesMessage)obj;
					LineList.add(new previousMoves(cordmsg.x, cordmsg.y, cordmsg.currentx, cordmsg.currenty));
					board[cordmsg.currentx][cordmsg.currenty].setSelected(false);
					board[cordmsg.x][cordmsg.y].setSelected(true);
					if((cordmsg.turn  && !myPlayerID.equals(cordmsg.ID)) || (!cordmsg.turn  && myPlayerID.equals(cordmsg.ID))){
						freezeField(false);
					}
					else
					{
						freezeField(false);
						setAvailableButtons(cordmsg);
					}
					this.repaint();
				}
				if(obj instanceof goalMessage){
					goalMessage goalmsg = (goalMessage)obj;
					if(goalmsg.winner){
						JOptionPane.showMessageDialog(null, "You WON!", "CONGRATULATIONS!!", JOptionPane.WARNING_MESSAGE);
						this.removeAll();
						this.repaint();
					}
					else{
						JOptionPane.showMessageDialog(null, "You lose!", "try again!!", JOptionPane.WARNING_MESSAGE);
						this.removeAll();
						this.repaint();
					}
				}
				
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}//End of while
		
	}
}
