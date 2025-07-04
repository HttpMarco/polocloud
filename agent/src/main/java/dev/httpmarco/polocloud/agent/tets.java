package dev.httpmarco.polocloud.agent;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class tets {

    public static void main(String[] args) throws IOException {
        long time = System.currentTimeMillis();
        String host = "localhost";
        int port = 25565;

        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(host, port), 3000);

        OutputStream out = socket.getOutputStream();
        InputStream in = socket.getInputStream();

        ByteArrayOutputStream handshake = new ByteArrayOutputStream();
        handshake.write(0x00); // packet id for handshake
        handshake.write(0x00); // protocol version
        writeVarInt(handshake, host.length());
        handshake.write(host.getBytes(StandardCharsets.UTF_8));
        handshake.write((port >> 8) & 0xFF);
        handshake.write(port & 0xFF);
        handshake.write(0x01); // next state: status

        writeVarInt(out, handshake.size());
        out.write(handshake.toByteArray());

        out.write(0x01); // request packet id
        out.write(0x00); // no payload

        int packetLength = readVarInt(in);
        int packetId = readVarInt(in);

        int jsonLength = readVarInt(in);
        byte[] jsonData = new byte[jsonLength];
        in.read(jsonData);

        String json = new String(jsonData, StandardCharsets.UTF_8);
        System.out.println("Antwort vom Server:");
        System.out.println(json);

        socket.close();
        System.out.println(System.currentTimeMillis() - time);
    }

    private static void writeVarInt(OutputStream out, int value) throws IOException {
        while ((value & -128) != 0) {
            out.write((value & 127) | 128);
            value >>>= 7;
        }
        out.write(value);
    }

    private static int readVarInt(InputStream in) throws IOException {
        int numRead = 0, result = 0;
        byte read;
        do {
            read = (byte) in.read();
            int value = (read & 0b01111111);
            result |= (value << (7 * numRead));

            numRead++;
            if (numRead > 5) throw new IOException("VarInt zu lang");
        } while ((read & 0b10000000) != 0);
        return result;
    }
}
