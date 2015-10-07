package clientServer;

import game.Board;
import game.Player;
import game.Room;

import java.awt.Point;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import clientServer.packets.DisconnectPacket;
import clientServer.packets.LoginPacket;
import clientServer.packets.MovePacket;
import clientServer.packets.Packet;
import clientServer.packets.Packet.PacketTypes;

public class GameServer extends Thread {
	private DatagramSocket socket;
	private Board board;
	private List<Player> connectedPlayers = new ArrayList<Player>();
	private Multiplayer multiplayer;

	public GameServer(Board board, Multiplayer multiplayer) {
		this.board = board;
		this.multiplayer = multiplayer;
		this.connectedPlayers.add(multiplayer.getCurrentPlayer());
		try {
			this.socket = new DatagramSocket(9772);
			System.out.println(InetAddress.getLocalHost().getHostAddress());
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	public List<Player> getConnectedPlayers() {
		return connectedPlayers;
	}

	public void run() {
		while (true) {
			// System.out.println("Test");
			byte[] data = new byte[1024];
			DatagramPacket packet = new DatagramPacket(data, data.length);
			try {
				// System.out.println("Server receiving packet");
				this.socket.receive(packet);
				// System.out.println("Server has received packet");
			} catch (IOException e) {
				e.printStackTrace();
			}
			this.parsePacket(packet.getData(), packet.getAddress(),
					packet.getPort());
			// System.out.println("Packet parsed");
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
			Point point = new Point(500, 300);
			Player pm = new Player(
					((LoginPacket) packet).getUserName(), point,
					board.getRoomFromId(0), address, port);
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
			// System.out.println(((MovePacket) packet).getUserName()
			// + " has moved to " + ((MovePacket) packet).getX() + ","
			// + ((MovePacket) packet).getY());
			this.handleMove((MovePacket) packet);
		}
	}

	public void handleMove(MovePacket packet) {
		try {
			if (packet.getUserName() != null) {
				// int index = getPlayerIndex(packet.getUserName());
				Player player = this.getPlayer(packet.getUserName());
				int playerX = packet.getX();
				int playerY = packet.getY();
				player.setCoords(playerX, playerY);
				Room newRoom = board.getRoomFromId(packet.getRoomID());
				player.setRoom(newRoom);
				packet.writeData(this);
			}
		} catch (IndexOutOfBoundsException e) {
			// System.out.println("Whoops");
		}
	}

	public void removeConnection(DisconnectPacket packet) {
		Player player = getPlayer(packet.getUserName());
		this.connectedPlayers.remove(player);
		board.getPlayers().remove(player);
		packet.writeData(this);

	}

	public int getPlayerIndex(String username) {
		int index = 0;
		for (Player player : this.connectedPlayers) {
			if (player.getName().equals(username)) {
				break;
			}
			index++;
		}
		return index;
	}

	public Player getPlayer(String name) {
		for (Player pm : this.connectedPlayers) {
			if (pm.getName().equals(name)) {
				return pm;
			}
		}
		return null;
	}

	public void addConnection(Player player, LoginPacket packet) {
		boolean alreadyConnected = false;
		System.out.println("Packet username is: " + packet.getUserName());
		System.out.println("Packet type is: " + packet.packetID);
		for (Player pm : this.connectedPlayers) {
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
				System.out.println("Sending player data to new player");
			}

		}
		if (alreadyConnected == false) {
			if (!player.equals(multiplayer.getCurrentPlayer())) {
				// System.out.println("player added");
				this.connectedPlayers.add(player);
				System.out.println("Adding: " + player.getName());
				board.addPlayers(player);
				// System.out.println("player added");
			}
		}

	}

	public void sendData(byte[] data, InetAddress ipAddress, int port) {
		DatagramPacket packet = new DatagramPacket(data, data.length,
				ipAddress, port);
		try {
			this.socket.send(packet);
		} catch (IOException e) {
			//e.printStackTrace();
		}
	}

	public void sendDataToAllClients(byte[] data) {
		for (Player pm : connectedPlayers) {
			sendData(data, pm.getIP(), pm.getPort());
		}
	}
}
