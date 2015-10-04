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
	private Multiplayer multiplayer;

	public GameServer(Board board, Multiplayer multiplayer) {
		this.board = board;
		this.multiplayer = multiplayer;
		try {
			this.socket = new DatagramSocket(9772);
			System.out.println(InetAddress.getLocalHost().getHostAddress());
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	public List<PlayerMulti> getConnectedPlayers() {
		return connectedPlayers;
	}

	public void run() {
		while (true) {
			System.out.println("Test");
			byte[] data = new byte[1024];
			DatagramPacket packet = new DatagramPacket(data, data.length);
			try {
				System.out.println("Server receiving packet");
				this.socket.receive(packet);
				System.out.println("Server has received packet");
			} catch (IOException e) {
				e.printStackTrace();
			}
			this.parsePacket(packet.getData(), packet.getAddress(),
					packet.getPort());
			System.out.println("Packet parsed");
			// String msg = new String(packet.getData());
			// System.out.println(msg);
			// System.out.println("CLIENT [ "
			// + packet.getAddress().getHostAddress() + ":"
			// + packet.getPort() + "] >" + msg);
			// if (msg.trim().equalsIgnoreCase("ping")) {
			// sendData("pong".getBytes(), packet.getAddress(),
			// packet.getPort());
			// }
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
			// System.out.println("player added in theory");
			break;
		case DISCONNECT:
			packet = new DisconnectPacket(data);
			System.out.println("[" + address.getHostAddress() + ":" + port
					+ "]" + ((DisconnectPacket) packet).getUserName()
					+ " has disconnected");
			this.removeConnection((DisconnectPacket) packet);
			break;
		case MOVE:
			packet = new MovePacket(data);
			System.out.println(((MovePacket) packet).getUserName()
					+ " has moved to " + ((MovePacket) packet).getX() + ","
					+ ((MovePacket) packet).getY());
			this.handleMove((MovePacket) packet);
		}
	}

	public void handleMove(MovePacket packet) {
		if (packet.getUserName() != null) {
			int index = getPlayerIndex(packet.getUserName());
			PlayerMulti player = this.connectedPlayers.get(index);
			int playerX = packet.getX();
			int playerY = packet.getY();
			player.setCoords(playerX, playerY);
			packet.writeData(this);
		}
	}

	public void removeConnection(DisconnectPacket packet) {
		PlayerMulti player = getPlayer(packet.getUserName());
		packet.writeData(this);

	}

	public int getPlayerIndex(String username) {
		int index = 0;
		for (PlayerMulti player : this.connectedPlayers) {
			if (player.getName().equals(username)) {
				break;
			}
			index++;
		}
		return index;
	}

	public PlayerMulti getPlayer(String name) {
		for (PlayerMulti pm : this.connectedPlayers) {
			if (pm.getName().equals(name)) {
				return pm;
			}
		}
		return null;
	}

	public void addConnection(PlayerMulti player, LoginPacket packet) {
		boolean alreadyConnected = false;
		System.out.println("Packet username is: " + packet.getUserName());
		System.out.println("Packet type is: " + packet.packetID);
		for (PlayerMulti pm : this.connectedPlayers) {
			if (pm.getName().equalsIgnoreCase(player.getName())) {
				if (pm.getIP() == null) {
					pm.setIP(player.getIP());
					System.out.println(pm.getIP());
				}
				if (pm.getPort() == -1) {
					pm.setPort(player.getPort());
					System.out.println(pm.getPort());
				}
				alreadyConnected = true;
				System.out.println("Set connected to true");
			} else {
				sendData(packet.getData(), pm.getIP(), pm.getPort());
				packet = new LoginPacket(pm.getName());
				sendData(packet.getData(), player.getIP(), player.getPort());
				System.out.println("New player entered");
			}

		}
		if (alreadyConnected == false) {
			if (!player.getName().equals(
					multiplayer.getCurrentPlayer().getName())) {
				System.out.println("player added");
				this.connectedPlayers.add(player);
				board.addPlayers(player);
				System.out.println("player added");
			}
		}

	}

	public void sendData(byte[] data, InetAddress ipAddress, int port) {
		DatagramPacket packet = new DatagramPacket(data, data.length,
				ipAddress, port);
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
