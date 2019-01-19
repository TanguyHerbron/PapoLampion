package com.papo.papolampion;

import java.io.IOException;

public class MainUDP {

	
	public static void main(String[] args) throws NumberFormatException, IOException {

		UDPClient client = new UDPClient("mpd.lan", 6969);
		client.setFill((byte)0xff, (byte)0x14, (byte)0x93);
	}

}
