package com.cibuddy.lights.configuration;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 *
 * @author mirkojahn
 */
public class AppTest {

    public static void main(String args[]) throws Exception {
        int i;
        byte[] buf = new byte[256];
        DatagramSocket socket;
        DatagramPacket packet;
        InetAddress address = InetAddress.getByName("localhost");
        socket = new DatagramSocket(33848);
        packet = new DatagramPacket(buf, buf.length, address, 33848);
//        socket.send(packet);
    }
}
