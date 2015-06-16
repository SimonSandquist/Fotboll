import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;

import javax.swing.*;


public class Client extends JFrame{
	
	static ObjectOutputStream oos;
	public static Socket SOCKET;
	public static String username;
	
	
	public static void main(String[] args) throws Exception{
		final Client CLIENT = new Client();
		CLIENT.setSize(500, 500);
		CLIENT.getContentPane().setLayout(null);
		CLIENT.setVisible(true);
		JButton singleBtn = new JButton("Singleplayer");
		CLIENT.getContentPane().add(singleBtn);
		singleBtn.setBounds(0, 250,500, 100);
		final JTextField iptextfield = new JTextField("localhost");
		final JTextField usernamefield = new JTextField("User");
		JLabel iplabel = new JLabel("IP-Adress:");
		JLabel namelabel = new JLabel("Username:");
		iptextfield.setBounds(95, 90, 100, 15);
		usernamefield.setBounds(95, 140, 100, 15);
		iplabel.setBounds(30, 45, 100, 100);
		namelabel.setBounds(30, 95, 100, 100);
		CLIENT.getContentPane().add(iptextfield);
		CLIENT.getContentPane().add(usernamefield);
		CLIENT.getContentPane().add(iplabel);
		CLIENT.getContentPane().add(namelabel);
		CLIENT.repaint();
		
		singleBtn.addActionListener(new ActionListener() 
		{ 
			   public void actionPerformed(ActionEvent e) 
			   {
					try 
					{
						username = usernamefield.getText();
						SOCKET = new Socket(iptextfield.getText(),666);
						oos = new  ObjectOutputStream(SOCKET.getOutputStream());
						oos.writeObject(new startgameMessage(true, username));
					    new ClientgameRoom(new ObjectInputStream(SOCKET.getInputStream()), oos);
						oos.flush();
						CLIENT.getContentPane().removeAll();
						CLIENT.repaint();
						
					} 
					catch (Exception x) 
					{
						x.printStackTrace();
					} 
			   }
		});
		
			JButton multiBtn = new JButton("Multiplayer");
			CLIENT.getContentPane().add(multiBtn);
			multiBtn.setBounds(0, 350, 500, 100);
			multiBtn.addActionListener(new ActionListener(){
		
			public void actionPerformed(ActionEvent e){
				
				try{	
					username = usernamefield.getText();
					SOCKET = new Socket(iptextfield.getText(),666);
					oos = new  ObjectOutputStream(SOCKET.getOutputStream());
					oos.writeObject(new startgameMessage(false, username));
				    new ClientgameRoom(new ObjectInputStream(SOCKET.getInputStream()), oos);
					oos.flush();
					CLIENT.getContentPane().removeAll();
					CLIENT.repaint();
					
				}
				catch(Exception x){
					x.printStackTrace();
				}
				
			}
			
		});

	}	

}
