package game;

import java.awt.Point;
import java.net.InetAddress;

public class PlayerMulti extends Player {
	
	public InetAddress ipAddress;
	public int port;
	
	public PlayerMulti(String name, Point coords, Room currentRoom, InetAddress ipAddress, int port){
		super(name,coords,currentRoom);	
		this.ipAddress = ipAddress;
		this.port = port;
	}
	
	public void tick(){
		
	}
	
	public InetAddress getIP(){
		return ipAddress;
	}
	
	public int getPort(){
		return port;
	}

	public void setPort(int newPort) {
		port = newPort;
	}

	public void setIP(InetAddress ip) {
		ipAddress = ip;
		
	}

}
