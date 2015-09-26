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

import clientServer.packets.LoginPacket;
import clientServer.packets.Packet;
import clientServer.packets.Packet.PacketTypes;

public class GameClient extends Thread {

	private InetAddress ipAddress;
	private DatagramSocket socket;
	// testing change
	private Board board;

	public GameClient(Board board, String ipAddress) {
		this.board = board;
		try {
			this.socket = new DatagramSocket(9679);
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
			DatagramPacket packet = new DatagramPacket(data, data.length);
			try {
				this.socket.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
			parsePacket(packet.getData(), packet.getAddress(), packet.getPort());
			String msg = new String(packet.getData());
			System.out.println(msg);
			System.out.println("SERVER > " + msg);
		}
	}

	private void parsePacket(byte[] data, InetAddress address, int port) {
		String msg = new String(data).trim();
		Packet packet = null;
		PacketTypes packetTypes = Packet.getPacket(msg.substring(0, 2));
		switch (packetTypes) {
		default:
		case INVALID:
			break;
		case LOGIN:
			packet = new LoginPacket(data);
			System.out.println("[" + address.getHostAddress() + ":" + port
					+ "]" + ((LoginPacket) packet).getUserName()
					+ "has joined the game");
			Point point = new Point(1, 1);
			PlayerMulti pm = new PlayerMulti(
					((LoginPacket) packet).getUserName(), point, new Room(-1,
							-1, -1, -1, 11, new Point(4, 4)), address, port);
			board.getPlayers().add(pm);
			break;
		case DISCONNECT:
			break;
		}

	}

	public void sendData(byte[] data) {
		DatagramPacket packet = new DatagramPacket(data, data.length,
				ipAddress, 9679);
		try {
			this.socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
