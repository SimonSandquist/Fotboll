import java.awt.Button;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.ObjectOutputStream;

import javax.swing.JRadioButton;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.plaf.basic.BasicBorders.RadioButtonBorder;



public class Buttons extends JRadioButton{
	
	public ClientgameRoom gameRoom;
	public ObjectOutputStream oos;
	public int y;
	public int x;

	public Buttons(ObjectOutputStream oos, int x, int y, ClientgameRoom room){
		this.oos = oos;			
		this.x = x;
		this.y = y;
		gameRoom = room;
	}
	
public void PressButton(){

	
	this.addMouseListener(new MouseAdapter(){
	
		@Override
		public void mousePressed(MouseEvent e){
			System.out.println("Click");
			
			new Thread(new Runnable(){
				public void run(){
					try{
					oos.writeObject(new coordinatesMessage(x, y, gameRoom.myPlayerID));
					oos.flush();
					}
					catch(Exception e){
						e.printStackTrace();
					}
				}
			}).start();
			//skicka medelande till servern med coordinaterna för att se om det är ett giltligt drag.
		}
	});

}
}
