package game;

import java.awt.Point;
import java.net.InetAddress;

public class PlayerMulti extends Player {

	public InetAddress ipAddress;
	public int port;

	public PlayerMulti(String name, Point coords, int currentRoom, InetAddress ipAddress, int port){
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

	public void setIP(InetAddress newIP){
		ipAddress = newIP;
	}

	public void setPort(int newPort){
		port = newPort;
	}

}
