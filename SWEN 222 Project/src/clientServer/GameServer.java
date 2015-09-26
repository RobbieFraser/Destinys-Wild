package clientServer;

import game.Board;
import game.PlayerMulti;

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
			this.socket = new DatagramSocket(9999);
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
			this.parsePacket(packet.getData(), packet.getAddress(), packet.getPort());
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
		PacketTypes packetTypes = Packet.getPacket(msg.substring(0, 2));
		LoginPacket packet = null;
		switch (packetTypes) {
		default:
		case INVALID:
			break;
		case LOGIN:
			System.out.println("[" + address.getHostAddress() + ":" + port
					+ "]" + ((LoginPacket) packet).getUserName() + "has connected");
			Point point = new Point(1, 1);
			PlayerMulti pm = new PlayerMulti(packet.getUserName(), point, 1, address,
					port);
			addConnection(pm,(LoginPacket)packet);
			if (pm != null) {
				this.connectedPlayers.add(pm);
			}
			break;
		case DISCONNECT:
			break;
		}

	}

	private void addConnection(PlayerMulti player, LoginPacket packet) {
		boolean alreadyConnected = false;
		for(PlayerMulti pm : this.connectedPlayers){
			if(player.getUserName().equalsIgnoreCase(pm.getUserName())){
				if(pm.getIP()==null){
					pm.setIP(player.getIP());
				}
				if(pm.getPort()==-1){
					pm.setPort(player.getPort());
				}
				alreadyConnected = true;
			}
			else{
				sendData(packet.getData(),pm.getIP(),pm.getPort());
			}
		}
		if(!alreadyConnected){
			this.connectedPlayers.add(player);
			packet.writeData(this);
		}
	}

	public void sendData(byte[] data, InetAddress ipAddress, int port) {
		DatagramPacket packet = new DatagramPacket(data, data.length,
				ipAddress, 1331);
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
