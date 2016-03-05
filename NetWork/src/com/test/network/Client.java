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
 * 这是通信的客户端类，主要功能是提供通信的界面
 * 
 * @author U-anLA
 *
 */
public class Client extends JFrame implements ActionListener {

	// 用于输入ip
	private JTextField jtfIp = new JTextField();
	// 用于输入端口
	private JTextField jtfPort = new JTextField();
	// 用于测试连接
	private JButton jbTest = new JButton("测试连接");
	// 存储要发送的内容
	private JTextField jtfSend = new JTextField();
	// 用来显示对话内容
	private JTextArea jtaMsg = new JTextArea();
	// 下拉框
	private JScrollPane jsp = new JScrollPane(jtaMsg);
	// 用于发送内容
	private JButton jbSend = new JButton("发送");
	// 客户端端口socket
	Socket socket;

	// 定义输出管道
	private DataOutputStream dos;
	// 定义输入管道
	private DataInputStream dis;
	// 判断是否已经连接服务器
	private boolean bConnect;
	// 判断是否测试连接成功
	private boolean bUse = true;
	// 接收IP地址、为空就默认本机
	private String ip;
	// 接收端口
	private int port;
	// 用来标记数组。
	private int index = 0;

	// 定义一个独一无二的数，来标记每一个client
	Date d = new Date();
	private String mark = String.valueOf(d.getTime());

	/**
	 * 客户端的main方法，用于启动程序
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Client c = new Client();
		c.setLocation(400, 100);
		c.setSize(600, 600);
		c.setTitle("客户端");
		c.setResizable(false);
		c.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		c.setVisible(true);
	}

	/**
	 * 客户端的构造方法，用于构造客户端。
	 */
	public Client() {
		Accept a = new Accept(this);
		initFrame();
		// 在线程启动之前，先把jbtest的点击事件添加
		jbTest.addActionListener(this);
		// 线程启动方法
		new Thread(a).start();
		// 将其他的点击信息添加进去。
		addListener();

	}

	/*
	 * 初始化端口socket
	 */
	private void initSocket() {
		try {
			// if (jtfIp.getText().trim().equals("")) {
			// JOptionPane.showMessageDialog(this, "请输入IP地址", "错误",
			// JOptionPane.ERROR_MESSAGE);
			// } else if (jtfPort.getText().trim().equals("")) {
			// JOptionPane.showMessageDialog(this, "请输入端口号", "错误",
			// JOptionPane.ERROR_MESSAGE);
			// } else {
			// ip = jtfIp.getText().trim();
			// port = Integer.parseInt(jtfPort.getText().trim());
			// }
			// 获得IP地址和端口号。
			ip = jtfIp.getText().trim();
			port = Integer.parseInt(jtfPort.getText().trim());
			// 初始化socket
			socket = new Socket(ip, port);
			// 初始化输入输出端口
			dis = new DataInputStream(socket.getInputStream());
			dos = new DataOutputStream(socket.getOutputStream());
			System.out.println("我连入了。。");
			// 已连接
			bConnect = true;
			// 可使用
			bUse = true;


		} catch (NumberFormatException e) {
			bUse = false;
			System.out.println("--------------服务器未开启---------------");
			JOptionPane.showMessageDialog(this, "请输入正确格式", "错误",
					JOptionPane.ERROR_MESSAGE);
			return;
		} catch (UnknownHostException e) {
			bUse = false;
			System.out.println("-------------找不到主机-------------");
			return;
		} catch (ConnectException e) {
			bUse = false;
			System.out.println("--------------服务器未开启---------------");
			JOptionPane.showMessageDialog(this, "请检查服务器是否开启或端口号是否正确", "连接失败",
					JOptionPane.ERROR_MESSAGE);
			return;
		} catch (IOException e) {
			bUse = false;
			System.out.println("--------------端口错误---------------");
			JOptionPane.showMessageDialog(this, "端口或IP地址输入不正确", "错误",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

	}

	/**
	 * 初始化客户端界面
	 */
	public void initFrame() {
		this.setLayout(null);
		JLabel jl1 = new JLabel("请输入连接IP地址：");
		jl1.setLocation(10, 10);
		jl1.setSize(150, 30);
		this.add(jl1);

		jtfIp.setLocation(150, 10);
		jtfIp.setSize(100, 30);
		this.add(jtfIp);

		this.setLayout(null);
		JLabel jl2 = new JLabel("请输入连接端口号：");
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
	 * 断开连接，不过本程序没使用，如果检测到已断开的，服务器端会将其从clients中清除。
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
			System.out.println("-------断开连接失败---------");
		}
	}

	/**
	 * 客户端多线程类，用于无限期无线频率的接收服务器发来的信息。
	 * 
	 * @author U-anLA
	 *
	 */
	private class Accept implements Runnable {
		// 获取客户端的句柄，用于给出提示信息
		JFrame j;

		/**
		 * 线程类的构造方法
		 * 
		 * @param j
		 *            主窗体
		 */
		public Accept(JFrame j) {
			this.j = j;
		}

		// 线程类的启动方法
		public void run() {

			System.out.println("rrrrrrrrrrrrrrr");
			// 死循环，主要用于将输入框变为可写
			while (true) {

				try {

					if (bConnect) {
						if (bUse == true) {
							// 如果已经登录成功，则将下列变为课操作，或不可操作。
							jtfSend.setEditable(true);
							jbSend.setEnabled(true);
							jtfIp.setEnabled(false);
							jtfPort.setEditable(false);
							jbTest.setEnabled(false);
							// 输出其实信息。
							if (index == 0) {
								JOptionPane.showMessageDialog(j, "连接成功", "恭喜",
										JOptionPane.YES_OPTION);
								System.out.println("************");
							}

							index++;
						}
						System.out.println("&&&&&&&&&&&&&&&&&7");
						// 读入发送来的数据
						String str = dis.readUTF();
						// 将其显示到textarea中
						jtaMsg.append(str + "\n");
					}

				} catch (SocketException e) {
					System.out.println("------服务器已断开--------");
					JOptionPane.showMessageDialog(j, "服务器已断开", "错误",
							JOptionPane.ERROR_MESSAGE);
					System.exit(0);
				} catch (IOException e) {
					System.out.println("------读取消息失败--------");
					e.printStackTrace();
				}

			}
		}

	}

	@Override
	/**
	 * 用于编写各个事件的动作方法
	 * @param e 用于获取时间对象
	 */
	public void actionPerformed(ActionEvent e) {
		// 如果对象是发送框或者发送按钮
		if (e.getSource() == jtfSend || e.getSource() == jbSend) {
			StringBuffer strSend = new StringBuffer();
			strSend.append(jtfSend.getText());
			// 将标志嵌入其中。
			strSend.append(mark);

			try {
				dos.writeUTF(strSend.toString());
				dos.flush();
			} catch (IOException e1) {
				System.out.println("-------写入失败，服务器未开启-------");
				e1.printStackTrace();
			}
			jtfSend.setText("");
		} else if (e.getSource() == jbTest) {
			// 如果对象是测试按钮，就初始化socket
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
