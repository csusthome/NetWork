package com.test.network;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Date;

import javax.swing.*;

/**
 * ����ͨ�ŵĿͻ����࣬��Ҫ�������ṩͨ�ŵĽ���
 * 
 * @author U-anLA
 *
 */
public class Client extends JFrame implements ActionListener {

	// ��������ip
	private JTextField jtfIp = new JTextField();
	// ��������˿�
	private JTextField jtfPort = new JTextField();
	// ���ڲ�������
	private JButton jbTest = new JButton("��������");
	// �洢Ҫ���͵�����
	private JTextField jtfSend = new JTextField();
	// ������ʾ�Ի�����
	private JTextArea jtaMsg = new JTextArea();
	// ������
	private JScrollPane jsp = new JScrollPane(jtaMsg);
	// ���ڷ�������
	private JButton jbSend = new JButton("����");
	// �ͻ��˶˿�socket
	Socket socket;

	// ��������ܵ�
	private DataOutputStream dos;
	// ��������ܵ�
	private DataInputStream dis;
	// �ж��Ƿ��Ѿ����ӷ�����
	private boolean bConnect;
	// �ж��Ƿ�������ӳɹ�
	private boolean bUse = true;
	// ����IP��ַ��Ϊ�վ�Ĭ�ϱ���
	private String ip;
	// ���ն˿�
	private int port;
	// ����������顣
	private int index = 0;

	// ����һ����һ�޶������������ÿһ��client
	Date d = new Date();
	private String mark = String.valueOf(d.getTime());

	/**
	 * �ͻ��˵�main������������������
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Client c = new Client();
		c.setLocation(400, 100);
		c.setSize(600, 600);
		c.setTitle("�ͻ���");
		c.setResizable(false);
		c.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		c.setVisible(true);
	}

	/**
	 * �ͻ��˵Ĺ��췽�������ڹ���ͻ��ˡ�
	 */
	public Client() {
		Accept a = new Accept(this);
		initFrame();
		// ���߳�����֮ǰ���Ȱ�jbtest�ĵ���¼����
		jbTest.addActionListener(this);
		// �߳���������
		new Thread(a).start();
		// �������ĵ����Ϣ��ӽ�ȥ��
		addListener();

	}

	/*
	 * ��ʼ���˿�socket
	 */
	private void initSocket() {
		try {
			// if (jtfIp.getText().trim().equals("")) {
			// JOptionPane.showMessageDialog(this, "������IP��ַ", "����",
			// JOptionPane.ERROR_MESSAGE);
			// } else if (jtfPort.getText().trim().equals("")) {
			// JOptionPane.showMessageDialog(this, "������˿ں�", "����",
			// JOptionPane.ERROR_MESSAGE);
			// } else {
			// ip = jtfIp.getText().trim();
			// port = Integer.parseInt(jtfPort.getText().trim());
			// }
			// ���IP��ַ�Ͷ˿ںš�
			ip = jtfIp.getText().trim();
			port = Integer.parseInt(jtfPort.getText().trim());
			// ��ʼ��socket
			socket = new Socket(ip, port);
			// ��ʼ����������˿�
			dis = new DataInputStream(socket.getInputStream());
			dos = new DataOutputStream(socket.getOutputStream());
			System.out.println("�������ˡ���");
			// ������
			bConnect = true;
			// ��ʹ��
			bUse = true;


		} catch (NumberFormatException e) {
			bUse = false;
			System.out.println("--------------������δ����---------------");
			JOptionPane.showMessageDialog(this, "��������ȷ��ʽ", "����",
					JOptionPane.ERROR_MESSAGE);
			return;
		} catch (UnknownHostException e) {
			bUse = false;
			System.out.println("-------------�Ҳ�������-------------");
			return;
		} catch (ConnectException e) {
			bUse = false;
			System.out.println("--------------������δ����---------------");
			JOptionPane.showMessageDialog(this, "����������Ƿ�����˿ں��Ƿ���ȷ", "����ʧ��",
					JOptionPane.ERROR_MESSAGE);
			return;
		} catch (IOException e) {
			bUse = false;
			System.out.println("--------------�˿ڴ���---------------");
			JOptionPane.showMessageDialog(this, "�˿ڻ�IP��ַ���벻��ȷ", "����",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

	}

	/**
	 * ��ʼ���ͻ��˽���
	 */
	public void initFrame() {
		this.setLayout(null);
		JLabel jl1 = new JLabel("����������IP��ַ��");
		jl1.setLocation(10, 10);
		jl1.setSize(150, 30);
		this.add(jl1);

		jtfIp.setLocation(150, 10);
		jtfIp.setSize(100, 30);
		this.add(jtfIp);

		this.setLayout(null);
		JLabel jl2 = new JLabel("���������Ӷ˿ںţ�");
		jl2.setLocation(10, 50);
		jl2.setSize(150, 30);
		this.add(jl2);

		jtfPort.setLocation(150, 50);
		jtfPort.setSize(100, 30);
		this.add(jtfPort);

		jbTest.setLocation(280, 30);
		jbTest.setSize(100, 30);
		this.add(jbTest);

		jtaMsg.setEnabled(false);

		jsp.setLocation(10, 100);
		jsp.setSize(550, 400);
		this.add(jsp);

		jtfSend.setLocation(10, 530);
		jtfSend.setSize(400, 30);
		this.add(jtfSend);
		jtfSend.setEditable(false);

		jbSend.setLocation(440, 530);
		jbSend.setSize(100, 30);
		this.add(jbSend);
		jbSend.setEnabled(false);

	}

	/**
	 * �Ͽ����ӣ�����������ûʹ�ã������⵽�ѶϿ��ģ��������˻Ὣ���clients�������
	 * 
	 * @param s
	 * @param dos
	 * @param dis
	 */
	public void disConnect(Socket s, DataOutputStream dos, DataInputStream dis) {
		try {
			dis.close();
			s.close();
			dos.close();
		} catch (IOException e) {
			System.out.println("-------�Ͽ�����ʧ��---------");
		}
	}

	/**
	 * �ͻ��˶��߳��࣬��������������Ƶ�ʵĽ��շ�������������Ϣ��
	 * 
	 * @author U-anLA
	 *
	 */
	private class Accept implements Runnable {
		// ��ȡ�ͻ��˵ľ�������ڸ�����ʾ��Ϣ
		JFrame j;

		/**
		 * �߳���Ĺ��췽��
		 * 
		 * @param j
		 *            ������
		 */
		public Accept(JFrame j) {
			this.j = j;
		}

		// �߳������������
		public void run() {

			System.out.println("rrrrrrrrrrrrrrr");
			// ��ѭ������Ҫ���ڽ�������Ϊ��д
			while (true) {

				try {

					if (bConnect) {
						if (bUse == true) {
							// ����Ѿ���¼�ɹ��������б�Ϊ�β������򲻿ɲ�����
							jtfSend.setEditable(true);
							jbSend.setEnabled(true);
							jtfIp.setEnabled(false);
							jtfPort.setEditable(false);
							jbTest.setEnabled(false);
							// �����ʵ��Ϣ��
							if (index == 0) {
								JOptionPane.showMessageDialog(j, "���ӳɹ�", "��ϲ",
										JOptionPane.YES_OPTION);
								System.out.println("************");
							}

							index++;
						}
						System.out.println("&&&&&&&&&&&&&&&&&7");
						// ���뷢����������
						String str = dis.readUTF();
						// ������ʾ��textarea��
						jtaMsg.append(str + "\n");
					}

				} catch (SocketException e) {
					System.out.println("------�������ѶϿ�--------");
					JOptionPane.showMessageDialog(j, "�������ѶϿ�", "����",
							JOptionPane.ERROR_MESSAGE);
					System.exit(0);
				} catch (IOException e) {
					System.out.println("------��ȡ��Ϣʧ��--------");
					e.printStackTrace();
				}

			}
		}

	}

	@Override
	/**
	 * ���ڱ�д�����¼��Ķ�������
	 * @param e ���ڻ�ȡʱ�����
	 */
	public void actionPerformed(ActionEvent e) {
		// ��������Ƿ��Ϳ���߷��Ͱ�ť
		if (e.getSource() == jtfSend || e.getSource() == jbSend) {
			StringBuffer strSend = new StringBuffer();
			strSend.append(jtfSend.getText());
			// ����־Ƕ�����С�
			strSend.append(mark);

			try {
				dos.writeUTF(strSend.toString());
				dos.flush();
			} catch (IOException e1) {
				System.out.println("-------д��ʧ�ܣ�������δ����-------");
				e1.printStackTrace();
			}
			jtfSend.setText("");
		} else if (e.getSource() == jbTest) {
			// ��������ǲ��԰�ť���ͳ�ʼ��socket
			initSocket();
		}

	}

	public void addListener() {
		jtfSend.addActionListener(this);
		jbSend.addActionListener(this);
		jtfIp.addActionListener(this);
		jtfPort.addActionListener(this);

	}

}
