package clientServer;

import game.Board;
import game.PlayerMulti;
import game.Room;

import java.awt.Point;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import clientServer.packets.DisconnectPacket;
import clientServer.packets.LoginPacket;
import clientServer.packets.Packet;
import clientServer.packets.Packet.PacketTypes;

public class GameClient extends Thread {

	private InetAddress ipAddress;
	private DatagramSocket socket;
	// testing change
	private Board board;

	public GameClient(Board board) {
		this.board = board;
		try {
			this.socket = new DatagramSocket();
			this.ipAddress =  InetAddress.getLocalHost();
			System.out.println("Client initialisng");
		} catch (SocketException e) {
			System.out.println("Socket exception.");
			e.printStackTrace();
		} catch (UnknownHostException e) {
			System.out.println("Unknown host exception.");
			e.printStackTrace();
		}
	}

	public void run() {
		while (true) {
			byte[] data = new byte[1024];
			DatagramPacket packet = new DatagramPacket(data, data.length);
			System.out.println("Packet created");
			try {
				System.out.println("Client attempting to receive packet");
				this.socket.receive(packet);
				System.out.println("Client received packet");
			} catch (IOException e) {
				System.out.println("Something went wrong");
				e.printStackTrace();
			}
			System.out.println("Client getting ready to parse packet");
			this.parsePacket(packet.getData(), packet.getAddress(), packet.getPort());
			//String msg = new String(packet.getData());
			//System.out.println(msg);
		//	System.out.println("SERVER > " + msg);
		}
	}

	private void parsePacket(byte[] data, InetAddress address, int port) {
		String msg = new String(data).trim();
		PacketTypes packetTypes = Packet.getPacket(msg.substring(0, 2));
		Packet packet = null;
		switch (packetTypes) {
		default:
		case INVALID:
			break;
		case LOGIN:
			System.out.println("Login Packet found");
			packet = new LoginPacket(data);
			handleLogin((LoginPacket)packet,address,port);
			break;
		case DISCONNECT:
			System.out.println("Disconnect Packet found");
			packet = new DisconnectPacket(data);
			System.out.println("[" + address.getHostAddress() + ":" + port
					+ "]" + ((DisconnectPacket) packet).getUserName()
					+ " has left the Wild");
			board.removePlayer(((DisconnectPacket) packet).getUserName());
			break;
		}

	}

	private void handleLogin(LoginPacket packet, InetAddress address, int port) {
		System.out.println("[" + address.getHostAddress() + ":" + port
				+ "]" + ((LoginPacket) packet).getUserName()
				+ " has entered the Wild");
		Point point = new Point(1, 1);
		PlayerMulti pm = new PlayerMulti(
				((LoginPacket) packet).getUserName(), point, new Room(-1,
						-1, -1, -1, 11, new Point(4, 4)), address, port);
		//System.out.println("Handling login of: "+ pm.getName());
		//board.getPlayers().add(pm);
		//System.out.println("Current players on board are: " + board.getPlayers());
		board.addPlayer(pm);
	}

	public void sendData(byte[] data) {
		DatagramPacket packet = new DatagramPacket(data, data.length,
				ipAddress, 9772);
		try {
			this.socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
