package com.test.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * 这是通信的服务器，主要功能是接收并且发送消息
 * 
 * @author U-anLA
 */
public class Server extends JFrame {

	// 定义服务器socket
	private ServerSocket serverSocket;
	// 来判断服务器是否已开启
	private boolean bStart;
	// 用来存放clients的list，主要是攻服务器逐个发送以及最终供移除的功能。
	List<MyClient> clients = new ArrayList<MyClient>();

	/**
	 * 程序启动的main方法
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Server s = new Server();
		s.setLocation(200, 200);
		s.setSize(500, 500);
		s.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		s.setVisible(true);
		s.setResizable(false);
	}

	/**
	 * Server的构造方法
	 */
	public Server() {
		serverStart();
	}

	/**
	 * server的启动方法
	 */
	private void serverStart() {
		try {
			// 定义个服务器端口
			serverSocket = new ServerSocket(1234);
			// 判断服务器的端口状态，并输入对应的服务信息
			if (serverSocket.isBound() == true) {
				System.out.println("服务器已启动！！");
			}
			// 将开始标志设为true
			bStart = true;
			/**
			 * 服务器需要一直开着。
			 */
			while (bStart) {
				// 将serverSocket接收到的每一个socket都存现来。
				Socket s = serverSocket.accept();
				// 定义并初始化一个多线程类
				MyClient mc = new MyClient(s);
				new Thread(mc).start(); // 线程启动，调用器run方法。
				// 将新获取的socket放入
				clients.add(mc);
			}
		} catch (IOException e) {

			// 如果想创建两个服务，且为同样的服务端口，就会提示错误，并且自动退出。
			System.out.println("----------创建服务器socket失败,端口被占用----------");
			JOptionPane.showMessageDialog(this, "创建服务器socket失败,端口被占用", "错误",
					JOptionPane.ERROR_MESSAGE);

			System.exit(0);
		}

	}

	/**
	 * 创建一个多线程类，用来接收并各个发送客户端的信息
	 * 
	 * @author U-anLA
	 * 
	 */
	private class MyClient implements Runnable {
		// 定义输入管道
		DataInputStream dis;
		// 定义输出管道
		DataOutputStream dos;
		// 定义一个socket，来接收主服务器放接收的端口
		Socket s;
		String str;
		// 定义是否可以开始读的标志。
		boolean bRead;

		/**
		 * MyClient的构造方法
		 * 
		 * @param s
		 */
		public MyClient(Socket s) {

			try {
				// 初始化socket
				this.s = s;
				// 分别接收并初始化两个管道
				dis = new DataInputStream(s.getInputStream());
				dos = new DataOutputStream(s.getOutputStream());
				bRead = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		/**
		 * 封装发送的方法
		 * 
		 * @param str
		 *            要发送的字符串
		 * @param drop
		 *            要从clients中移除的list编号。
		 */
		public void send(String str, int drop) {
			try {
				// 想端口中输出。
				dos.writeUTF(str);
			} catch (SocketException e) {
				// 如果捕捉到一个端口关闭的错误，就从list移除当前端口
				System.out.println("qqqqqqqqqq");
				clients.remove(drop);
			} catch (IOException e) {
				System.out.println("eeeeeee");
			}
		}

		@Override
		/**
		 * 多线程的run方法。
		 * 用于线程的启动方法
		 */
		public void run() {

			try {
				while (bRead) {
					// 从各个端口中读入数据。
					str = dis.readUTF();
					// 逐个发送数据。
					for (int i = 0; i < clients.size(); i++) {
						MyClient c = clients.get(i);
						if (c != null) {
							c.send(str, i);
						}

					}
				}
			} catch (EOFException e) {
				bRead = false;
				System.out.println("----------文件已读完----------");

			} catch (SocketException e) {
				System.out.println("------一方已断开连接------");

			} catch (IOException e) {

				e.printStackTrace();
			}

		}

	}

}
