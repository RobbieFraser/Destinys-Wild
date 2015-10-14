package clientServer;

import game.Board;
import game.DestinysWild;
import game.Player;
import game.Room;
import game.items.Item;
import game.npcs.EnemyWalker;
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
import java.util.ArrayList;
import java.util.List;

import clientServer.packets.DisconnectPacket;
import clientServer.packets.EnemyPacket;
import clientServer.packets.HealthPacket;
import clientServer.packets.LoginPacket;
import clientServer.packets.MovePacket;
import clientServer.packets.Packet;
import clientServer.packets.RemoveItemPacket;
import clientServer.packets.Packet.PacketTypes;
import clientServer.packets.TimePacket;
import clientServer.packets.TorchPacket;

public class GameServer extends Thread {
	private DatagramSocket socket;
	private Board board;
	private List<Player> connectedPlayers = new ArrayList<Player>();
	private Multiplayer multiplayer;

	/**
	 * Constructor for a GameServer which takes in a Board and a Multiplayer
	 * object
	 *
	 * @param board
	 * @param multiplayer
	 */
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
			Player pm = new Player(((LoginPacket) packet).getUserName(), point,
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
			this.handleMove((MovePacket) packet);
			break;
		case REMOVEITEM:
			packet = new RemoveItemPacket(data);
			this.handleRemoveItemPacket((RemoveItemPacket) packet);
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
		Player player = this.getPlayer(packet.getUserName());
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
			if (npc instanceof EnemyWalker && npc.getId() == packet.getID()) {
				Point point = new Point(packet.getRealCoordsX(),
						packet.getRealCoordsY());
				npc.setRealCoords(point);
				Point roomPoint = new Point(packet.getRoomCoordsX(),packet.getRoomCoordsY());
				npc.setRoomCoords(roomPoint);
				npc.setHealth(packet.getHealth());
				npc.setCurrentTile(room.calcTile(point));
			}
		}
	}

	/**
	 * This method takes a TorchPacket and sets whether or not the player has a
	 * Torch or not
	 *
	 * @param packet
	 */
	public void handleTorchPacket(TorchPacket packet) {
		Player player = getPlayer(packet.getUserName());
		player.setHasTorch(intToBool(packet.getHasTorch()));
		packet.writeData(this);
	}

	/**
	 * This method takes a TimePacket and obtains the time from it and sets the
	 * current time to it
	 * @param packet
	 */
	public void handleTimePacket(TimePacket packet) {
		//System.out.println(packet.getNewTime());
		if (packet.getNewTime() > 63) {
			DestinysWild.getGameInterface().getGameImagePanel().setTime(63);
		} else {
			DestinysWild.getGameInterface().getGameImagePanel()
					.setTime(packet.getNewTime());
		}
		packet.writeData(this);
	}

	/**
	 * This method takes a RemoveItemPacket and obtains the Room and Item
	 * objects from the data stored inside it. It then removes the item from the
	 * room.
	 *
	 * @param packet
	 */
	public void handleRemoveItemPacket(RemoveItemPacket packet) {
		Room itemRoom = board.getRoomFromId(packet.getRoomID());
		Item itemToRemove = itemRoom.getItemFromId(packet.getItemID());
		if (itemToRemove != null) {
			itemRoom.removeItems(itemToRemove);
		}
		packet.writeData(this);
	}

	/**
	 * This method takes a MovePacket and obtains the Player, their health and X
	 * and Y positions. It then sets their room, co-ordinates and what
	 * direction(s) they are moving in. Once it has done this, it updates the
	 * player and sets their current tile.
	 *
	 * @param packet
	 */
	public void handleMove(MovePacket packet) {
		try {
			if (packet.getUserName() != null) {
				// int index = getPlayerIndex(packet.getUserName());
				Player player = this.getPlayer(packet.getUserName());
				player.setHealth(packet.getHealth());
				int playerX = packet.getX();
				int playerY = packet.getY();
				player.setCoords(playerX, playerY);
				Room newRoom = board.getRoomFromId(packet.getRoomID());
				player.setRoom(newRoom);
				player.setNorth(intToBool(packet.getNorth()));
				player.setEast(intToBool(packet.getEast()));
				player.setWest(intToBool(packet.getWest()));
				player.setSouth(intToBool(packet.getSouth()));
				if(packet.getAllowGate()==1){
					for(Player p : board.getPlayers()){
						p.setAllowGate(true);
					}
				}
				player.setCurrentTile(player.getCurrentRoom().calcTile(
						player.getCoords()));
				// player.updatePlayer();
				packet.writeData(this);
			}
		} catch (IndexOutOfBoundsException e) {
			// System.out.println("Whoops");
		}
	}

	/**
	 * Removes a player from the game by sending a DisconnectPacket with the
	 * player's information
	 *
	 * @param packet
	 */
	public void removeConnection(DisconnectPacket packet) {
		Player player = getPlayer(packet.getUserName());
		this.connectedPlayers.remove(player);
		board.getPlayers().remove(player);
		packet.writeData(this);

	}

	/**
	 * Helper method that gets the index of a player in the list
	 *
	 * @param username
	 * @return
	 */
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

	/**
	 * Gets a player from the set of connectedPlayers by searching for their
	 * name
	 *
	 * @param name
	 * @return
	 */
	public Player getPlayer(String name) {
		for (Player pm : this.connectedPlayers) {
			if (pm.getName().equals(name)) {
				return pm;
			}
		}
		return null;
	}

	/**
	 * Takes a Player and a LoginPacket amd checks if the Player is already in
	 * the list of connected players. If it is, it is marked as connected and
	 * its port and IP are set to to the matching player's port and IP. If it is
	 * not in the list, a LoginPacket is sent out and it is added to the list of
	 * connected players on the server and the list of players on the Board.
	 *
	 * @param player
	 * @param packet
	 */
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
				this.connectedPlayers.add(player);
				System.out.println("Adding: " + player.getName());
				board.addPlayers(player);
			}
		}

	}

	/**
	 * Sends data in the form of a byte array to the desired InetAddress and
	 * port
	 *
	 * @param data
	 * @param ipAddress
	 * @param port
	 */
	public void sendData(byte[] data, InetAddress ipAddress, int port) {
		DatagramPacket packet = new DatagramPacket(data, data.length,
				ipAddress, port);
		try {
			this.socket.send(packet);
		} catch (IOException e) {
			// e.printStackTrace();
		}
	}

	/**
	 * Sends data in the form of a byte array to all players in the
	 * connectedPlayers list
	 *
	 * @param data
	 */
	public void sendDataToAllClients(byte[] data) {
		for (Player pm : connectedPlayers) {
			sendData(data, pm.getIP(), pm.getPort());
		}
	}

	public List<Player> getConnectedPlayers() {
		return connectedPlayers;
	}

	/**
	 * Helper method that represents a boolean as an int
	 *
	 * @param bool
	 * @return
	 */
	public int boolToInt(boolean bool) {
		int boolInt = bool ? 1 : 0;
		return boolInt;
	}

	/**
	 * Helper method that represents an int as a boolean
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
