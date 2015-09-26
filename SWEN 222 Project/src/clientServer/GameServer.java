package clientServer;

import game.Board;
import game.PlayerMulti;
import game.Room;

import java.util.*;
import java.awt.Point;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import clientServer.packets.*;
import clientServer.packets.Packet.PacketTypes;

public class GameServer extends Thread {
	private DatagramSocket socket;
	private Board board;
	private List<PlayerMulti> connectedPlayers = new ArrayList<PlayerMulti>();

	public GameServer(Board board) {
		this.board = board;
		try {
			this.socket = new DatagramSocket(9772);
		} catch (SocketException e) {
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
			this.parsePacket(packet.getData(), packet.getAddress(),
					packet.getPort());
//			String msg = new String(packet.getData());
//			System.out.println(msg);
//			System.out.println("CLIENT [ "
//					+ packet.getAddress().getHostAddress() + ":"
//					+ packet.getPort() + "] >" + msg);
//			if (msg.trim().equalsIgnoreCase("ping")) {
//				sendData("pong".getBytes(), packet.getAddress(),
//						packet.getPort());
//			}
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
					+ " has connected");
			Point point = new Point(1, 1);
			PlayerMulti pm = new PlayerMulti(
					((LoginPacket) packet).getUserName(), point, new Room(-1,
							-1, -1, -1, 11, new Point(4, 4)), address, port);
			this.addConnection(pm, (LoginPacket) packet);
			break;
		case DISCONNECT:
			break;
		}

	}

	public void addConnection(PlayerMulti pm, LoginPacket packet) {
		boolean alreadyConnected = false;
		for (PlayerMulti player : this.connectedPlayers) {
			if (pm.getName().equalsIgnoreCase(player.getName())) {
				if (player.getIP() == null) {
					player.setIP(pm.getIP());
				}
				if (player.getPort() == -1) {
					player.setPort(pm.getPort());
				}
				alreadyConnected = true;
			} else {
				sendData(packet.getData(), player.getIP(), player.getPort());
				packet = new LoginPacket(player.getName());
				sendData(packet.getData(),pm.getIP(),pm.getPort());
			}
			if (!alreadyConnected) {
				this.connectedPlayers.add(pm);
			}

		}

	}

	public void sendData(byte[] data, InetAddress ipAddress, int port) {
		DatagramPacket packet = new DatagramPacket(data, data.length,
				ipAddress, 9772);
		try {
			this.socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendDataToAllClients(byte[] data) {
		for (PlayerMulti pm : connectedPlayers) {
			sendData(data, pm.getIP(), pm.getPort());
		}
	}
}
