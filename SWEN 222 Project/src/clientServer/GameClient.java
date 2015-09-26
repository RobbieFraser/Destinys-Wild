package clientServer;

import game.Board;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class GameClient extends Thread{

	private InetAddress ipAddress;
	private DatagramSocket socket;
	private Board board;

	public GameClient(Board board,String ipAddress){
		this.board = board;
		try {
			this.socket = new DatagramSocket();
			this.ipAddress = InetAddress.getByName(ipAddress);
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		while (true) {
			byte[] data = new byte[1024];
			DatagramPacket packet = new DatagramPacket(data,data.length);
			try {
				this.socket.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
			String msg = new String(packet.getData());
			System.out.println(msg);
			System.out.println("SERVER > " + msg);
		}
	}

	public void sendData(byte[] data){
		DatagramPacket packet = new DatagramPacket(data,data.length,ipAddress,9999);
		try{
			this.socket.send(packet);
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}

}
