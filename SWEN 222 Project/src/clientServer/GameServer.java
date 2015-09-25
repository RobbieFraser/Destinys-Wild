package clientServer;

import game.Board;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class GameServer {
	private DatagramSocket socket;
	private Board board;
	
	public GameServer(Board board,String ipAddress){
		this.board = board;
		try {
			this.socket = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	public void run(){
		while(true){
			byte[] data = new byte[1024];
			DatagramPacket packet = new DatagramPacket(data,data.length);
			try {
				socket.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
			String msg = new String(packet.getData());
			if(msg.equalsIgnoreCase("ping")){
				System.out.println("CLIENT > " + new String(packet.getData()));
				sendData("pong".getBytes(),packet.getAddress(),packet.getPort());
			}
		}
	}
	
	public void sendData(byte[] data,InetAddress ipAddress, int port){
		DatagramPacket packet = new DatagramPacket(data,data.length,ipAddress,1331);
		try{
			socket.send(packet);
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
}
