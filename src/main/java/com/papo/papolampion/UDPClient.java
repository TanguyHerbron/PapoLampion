package com.papo.papolampion;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UDPClient {
	private DatagramSocket socket;
    private InetAddress address;
 
    private byte[] buf;
 
    public UDPClient(String ip, int port) throws SocketException, UnknownHostException {
        socket = new DatagramSocket(port);
        address = InetAddress.getByName(ip);
    }
 
    public void setFill(Byte r, Byte g, Byte b) throws IOException {
        buf = new byte[] {r, g, b};
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 4445);
        socket.send(packet);
    }
 
    public void close() {
        socket.close();
    }
}
