package clientServer;

import game.Board;
import game.PlayerMulti;

import java.awt.Point;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import clientServer.packets.LoginPacket;
import clientServer.packets.Packet;
import clientServer.packets.Packet.PacketTypes;

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
			this.parsePacket(packet.getData(), packet.getAddress(), packet.getPort());
//			String msg = new String(packet.getData());
//			System.out.println(msg);
//			System.out.println("SERVER > " + msg);
		}
	}

	private void parsePacket(byte[] data, InetAddress address, int port) {
		String msg = new String(data).trim();
		PacketTypes packetTypes = Packet.getPacket(msg.substring(0, 2));
		LoginPacket packet = null;
		switch (packetTypes) {
		default:
		case INVALID:
			break;
		case LOGIN:
			System.out.println("[" + address.getHostAddress() + ":" + port
					+ "]" + ((LoginPacket) packet).getUserName() + "has entered the game");
			Point point = new Point(1, 1);
			PlayerMulti pm = new PlayerMulti(packet.getUserName(), point, 1, address,
					port);
			//Placeholder line to add player to board or whatever
			board.addPlayer(pm);
			break;
		case DISCONNECT:
			break;
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
