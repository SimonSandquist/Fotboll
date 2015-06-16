import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;



public class Server extends JFrame {
	
	public static Socket SOCKET;
	public ConcurrentLinkedQueue<Player> quelist =  new ConcurrentLinkedQueue<Player>();
	public int Width;
	public int Height;
	
	public Server()
	{
		this.setSize(500, 500);
		this.getContentPane().setLayout(null);
		this.setVisible(true);
		JButton servbutton = new JButton("Start Server");
		this.getContentPane().add(servbutton);
		servbutton.setBounds(300, 350,150, 50);
		final JTextField width = new JTextField("8");
		final JTextField heigth = new JTextField("12");
		JLabel widthlabel = new JLabel("Width");
		JLabel heigthlabel = new JLabel("Heigth");
		heigthlabel.setBounds(75, 140, 100, 100);
		widthlabel.setBounds(75, 90, 100, 100);
		width.setBounds(160, 135, 40, 15);
		heigth.setBounds(160, 185, 40, 15);
		this.getContentPane().add(width);
		this.getContentPane().add(heigth);
		this.getContentPane().add(heigthlabel);
		this.getContentPane().add(widthlabel);
		this.repaint();
		servbutton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				try{
					Width = Integer.parseInt(width.getText());
					Height = Integer.parseInt(heigth.getText());
					
					if((Width % 2) == 0 && (Height % 2) == 0 ){
						Width += 1; Height += 1;
						run();
					}
				}
				catch(Exception x){
					x.printStackTrace();
				}
			}


		});		
	}
	
	
	public static void main(String[] args) throws Exception{
		new Server();
	}
	
	public void run() throws Exception{
		ServerSocket SERVERSOCKET =  new ServerSocket(666);
		
		while(true){
		SOCKET = SERVERSOCKET.accept();
		
		 new Thread(new Runnable() 
		 { 
		     @Override
		     public void run() 
		     {
		    	 try
		    	 {
				    ObjectInputStream ois;
					ois = new ObjectInputStream(SOCKET.getInputStream()); 
					ObjectOutputStream oos;
					oos = new ObjectOutputStream(SOCKET.getOutputStream());
				    Object obj = ois.readObject(); 
				    if(obj instanceof startgameMessage)
				    {
				    	startgameMessage startgamemessage = (startgameMessage) obj;
				    	Player spelare = new Player(startgamemessage.singleplayer, ois, oos, startgamemessage.username);
				    	fielddrawMessage fielddrawmsg = new fielddrawMessage(Width, Height);
				    	if(startgamemessage.singleplayer != true){
				    		System.out.println("Multiplayer");
				    		if(quelist.isEmpty()){
				    			quelist.add(spelare);
				    		}//End of quelist if
				    		else{
				    			Player spelare2 = quelist.poll();
				    			spelare.turn = true;
				    			gameRoom gameroom = new gameRoom(spelare, spelare2, Width, Height);
				    			spelare.oos.writeObject(fielddrawmsg);
				    			spelare.oos.flush();
				    			spelare2.oos.writeObject(fielddrawmsg);
				    			spelare2.oos.flush();
				    		}//End of quelist else
				    	}
				    	else{
				    		System.out.println("Singleplayer");
				    		spelare.turn = true;
				    		AI ai = new AI();	
				    		gameRoom gameroom = new gameRoom(spelare, ai, Width, Height);
				    		spelare.oos.writeObject(fielddrawmsg);
			    			spelare.oos.flush();
				    	}
				    } 
		    	}
		     
		    	catch (Exception e) 
		    	{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		     }
		 }).start();
		 
		}
	}

}


