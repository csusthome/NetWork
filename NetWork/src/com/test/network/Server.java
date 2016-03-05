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
 * ����ͨ�ŵķ���������Ҫ�����ǽ��ղ��ҷ�����Ϣ
 * 
 * @author U-anLA
 */
public class Server extends JFrame {

	// ���������socket
	private ServerSocket serverSocket;
	// ���жϷ������Ƿ��ѿ���
	private boolean bStart;
	// �������clients��list����Ҫ�ǹ���������������Լ����չ��Ƴ��Ĺ��ܡ�
	List<MyClient> clients = new ArrayList<MyClient>();

	/**
	 * ����������main����
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
	 * Server�Ĺ��췽��
	 */
	public Server() {
		serverStart();
	}

	/**
	 * server����������
	 */
	private void serverStart() {
		try {
			// ������������˿�
			serverSocket = new ServerSocket(1234);
			// �жϷ������Ķ˿�״̬���������Ӧ�ķ�����Ϣ
			if (serverSocket.isBound() == true) {
				System.out.println("����������������");
			}
			// ����ʼ��־��Ϊtrue
			bStart = true;
			/**
			 * ��������Ҫһֱ���š�
			 */
			while (bStart) {
				// ��serverSocket���յ���ÿһ��socket����������
				Socket s = serverSocket.accept();
				// ���岢��ʼ��һ�����߳���
				MyClient mc = new MyClient(s);
				new Thread(mc).start(); // �߳�������������run������
				// ���»�ȡ��socket����
				clients.add(mc);
			}
		} catch (IOException e) {

			// ����봴������������Ϊͬ���ķ���˿ڣ��ͻ���ʾ���󣬲����Զ��˳���
			System.out.println("----------����������socketʧ��,�˿ڱ�ռ��----------");
			JOptionPane.showMessageDialog(this, "����������socketʧ��,�˿ڱ�ռ��", "����",
					JOptionPane.ERROR_MESSAGE);

			System.exit(0);
		}

	}

	/**
	 * ����һ�����߳��࣬�������ղ��������Ϳͻ��˵���Ϣ
	 * 
	 * @author U-anLA
	 * 
	 */
	private class MyClient implements Runnable {
		// ��������ܵ�
		DataInputStream dis;
		// ��������ܵ�
		DataOutputStream dos;
		// ����һ��socket�����������������Ž��յĶ˿�
		Socket s;
		String str;
		// �����Ƿ���Կ�ʼ���ı�־��
		boolean bRead;

		/**
		 * MyClient�Ĺ��췽��
		 * 
		 * @param s
		 */
		public MyClient(Socket s) {

			try {
				// ��ʼ��socket
				this.s = s;
				// �ֱ���ղ���ʼ�������ܵ�
				dis = new DataInputStream(s.getInputStream());
				dos = new DataOutputStream(s.getOutputStream());
				bRead = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		/**
		 * ��װ���͵ķ���
		 * 
		 * @param str
		 *            Ҫ���͵��ַ���
		 * @param drop
		 *            Ҫ��clients���Ƴ���list��š�
		 */
		public void send(String str, int drop) {
			try {
				// ��˿��������
				dos.writeUTF(str);
			} catch (SocketException e) {
				// �����׽��һ���˿ڹرյĴ��󣬾ʹ�list�Ƴ���ǰ�˿�
				System.out.println("qqqqqqqqqq");
				clients.remove(drop);
			} catch (IOException e) {
				System.out.println("eeeeeee");
			}
		}

		@Override
		/**
		 * ���̵߳�run������
		 * �����̵߳���������
		 */
		public void run() {

			try {
				while (bRead) {
					// �Ӹ����˿��ж������ݡ�
					str = dis.readUTF();
					// ����������ݡ�
					for (int i = 0; i < clients.size(); i++) {
						MyClient c = clients.get(i);
						if (c != null) {
							c.send(str, i);
						}

					}
				}
			} catch (EOFException e) {
				bRead = false;
				System.out.println("----------�ļ��Ѷ���----------");

			} catch (SocketException e) {
				System.out.println("------һ���ѶϿ�����------");

			} catch (IOException e) {

				e.printStackTrace();
			}

		}

	}

}
