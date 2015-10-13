package clientServer;

import game.Board;
import game.DestinysWild;
import game.Player;
import game.Room;
import game.items.Item;
import game.npcs.NPC;

import java.awt.Point;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import clientServer.packets.DisconnectPacket;
import clientServer.packets.EnemyPacket;
import clientServer.packets.HealthPacket;
import clientServer.packets.LoginPacket;
import clientServer.packets.MovePacket;
import clientServer.packets.Packet;
import clientServer.packets.TimePacket;
import clientServer.packets.TorchPacket;
import clientServer.packets.Packet.PacketTypes;
import clientServer.packets.RemoveItemPacket;

public class GameClient extends Thread {

	private InetAddress ipAddress;
	private DatagramSocket socket;
	private Board board;
	private Multiplayer multiplayer;

	/**
	 * Constructor for GameClient which takes in a Board, IP Address in the form
	 * of a String and a Multiplayer object
	 * 
	 * @param board
	 * @param ipName
	 * @param multiplayer
	 */
	public GameClient(Board board, String ipName, Multiplayer multiplayer) {
		this.board = board;
		this.multiplayer = multiplayer;
		try {
			this.socket = new DatagramSocket();
			this.ipAddress = InetAddress.getByName(ipName);
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
			try {
				this.socket.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (packet != null) {
				this.parsePacket(packet.getData(), packet.getAddress(),
						packet.getPort());
			}
		}
	}

	/**
	 * This method receives an array of bytes, an InetAddress and a port number
	 * and processes the array of bytes into the relevant packet. It then
	 * performs a handle method based off the type of packet it is.
	 * 
	 * @param data
	 * @param address
	 * @param port
	 */
	public void parsePacket(byte[] data, InetAddress address, int port) {
		String msg = new String(data).trim();
		PacketTypes packetTypes = Packet.getPacket(msg.substring(0, 2));
		Packet packet = null;
		switch (packetTypes) {
		default:
		case INVALID:
			break;
		case LOGIN:
			packet = new LoginPacket(data);
			handleLogin((LoginPacket) packet, address, port);
			break;
		case DISCONNECT:
			packet = new DisconnectPacket(data);
			System.out.println("[" + address.getHostAddress() + ":" + port
					+ "] " + ((DisconnectPacket) packet).getUserName()
					+ " has left the Wild...");
			board.removePlayers(board.getPlayer(((DisconnectPacket) packet)
					.getUserName()));
			break;
		case MOVE:
			packet = new MovePacket(data);
			handleMovePacket((MovePacket) packet);
			break;
		case REMOVEITEM:
			packet = new RemoveItemPacket(data);
			handleRemoveItemPacket((RemoveItemPacket) packet);
			break;
		case TIME:
			packet = new TimePacket(data);
			//System.out.println(packet);
			this.handleTimePacket((TimePacket) packet);
			break;
		case TORCH:
			packet = new TorchPacket(data);
			this.handleTorchPacket((TorchPacket) packet);
			break;
		case ENEMY:
			packet = new EnemyPacket(data);
			this.handleEnemyPacket((EnemyPacket) packet);
			break;
		case HEALTH:
			packet = new HealthPacket(data);
			this.handleHealthPacket((HealthPacket) packet);
			break;
		}
	}

	/**
	 * This method takes a HealthPacket, obtains the relevant Player and sets
	 * their health to the value in the packet
	 * 
	 * @param packet
	 */
	public void handleHealthPacket(HealthPacket packet) {
		Player player = board.getPlayer(packet.getUserName());
		player.setHealth(packet.getHealth());

	}

	/**
	 * This method takes an EnemyPacket, finds the relevant NPC by using the ID
	 * in the packet and adjusts their health and co-ordinates to those in the
	 * packet.
	 * 
	 * @param packet
	 */
	public void handleEnemyPacket(EnemyPacket packet) {
		Room room = board.getRoomFromId(packet.getCurrentRoomID());
		for (NPC npc : room.getNpcs()) {
			if (npc.getId() == packet.getID()) {
				Point point = new Point(packet.getRealCoordsX(),
						packet.getRealCoordsY());
				npc.setRealCoords(point);
				Point roomPoint = new Point(packet.getRoomCoordsX(),packet.getRoomCoordsY());
				npc.setHealth(packet.getHealth());
				npc.setCurrentTile(room.calcTile(point));
			}
		}
	}

	/**
	 * This method takes a TimePacket and obtains the time from it and sets the
	 * current time to it
	 * 
	 * @param packet
	 */
	public void handleTimePacket(TimePacket packet) {
	}

	/**
	 * This method takes a TorchPacket and sets whether or not the player has a
	 * Torch or not
	 * @param packet
	 */
	public void handleTorchPacket(TorchPacket packet) {
		Player player = board.getPlayer(packet.getUserName());
		player.setHasTorch(intToBool(packet.getHasTorch()));
	}

	/**
	 * This method takes a RemoveItemPacket and obtains the Room and Item
	 * objects from the data stored inside it. It then removes the item from the
	 * room.
	 * 
	 * @param packet
	 */
	public void handleRemoveItemPacket(RemoveItemPacket packet) {
//		Room itemRoom = board.getRoomFromId(packet.getRoomID());
//		Item itemToRemove = itemRoom.getItemFromId(packet.getItemID());
//		itemRoom.removeItems(itemToRemove);
	}

	/**
	 * This method takes a MovePacket and obtains the Player, their health and X
	 * and Y positions. It then sets their room, co-ordinates and what
	 * direction(s) they are moving in. Once it has done this, it updates the
	 * player and sets their current tile.
	 * 
	 * @param packet
	 */
	public void handleMovePacket(MovePacket packet) {
		Player player = board.getPlayer(packet.getUserName());
		player.setHealth(packet.getHealth());
		int playerX = packet.getX();
		int playerY = packet.getY();
		int roomID = packet.getRoomID();
		Room room = board.getRoomFromId(roomID);
		player.setRoom(room);
		player.setCoords(playerX, playerY);
		player.setNorth(intToBool(packet.getNorth()));
		player.setEast(intToBool(packet.getEast()));
		player.setWest(intToBool(packet.getWest()));
		player.setSouth(intToBool(packet.getSouth()));
		player.setCurrentTile(player.getCurrentRoom().calcTile(
				player.getCoords()));
		// player.updatePlayer();
	}

	/**
	 * This method deals with logging in and joining the server. It takes a
	 * LoginPacket, an InetAddress and a port number. It creates a new Player
	 * object and adds it to the Set of Players in Board and initialises their
	 * location to the home location.
	 * 
	 * @param packet
	 * @param address
	 * @param port
	 */
	private void handleLogin(LoginPacket packet, InetAddress address, int port) {
		System.out.println("[" + address.getHostAddress() + ":" + port + "]"
				+ ((LoginPacket) packet).getUserName()
				+ " has entered the Wild");
		Point point = new Point(500, 300);
		Player pm = new Player(((LoginPacket) packet).getUserName(), point,
				board.getRoomFromId(0), address, port);
		if (!pm.getName().equals(multiplayer.getCurrentPlayer().getName())) {
			board.addPlayers(pm);
			System.out.println("Added: " + pm.getName());
		}
	}

	/**
	 * This method sends data in the form of an array of bytes to the server.
	 * 
	 * @param data
	 */
	public void sendData(byte[] data) {
		DatagramPacket packet = new DatagramPacket(data, data.length,
				ipAddress, 9772);
		try {
			this.socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Helper method that represents a boolean as an int. (1 for true, 0 for
	 * false)
	 * 
	 * @param bool
	 * @return
	 */
	public int boolToInt(boolean bool) {
		int boolInt = bool ? 1 : 0;
		return boolInt;
	}

	/**
	 * This is another helper method, that represents an int as a boolean
	 * 
	 * @param i
	 * @return
	 */
	public boolean intToBool(int i) {
		if (i == 1) {
			return true;
		} else {
			return false;
		}
	}

}
