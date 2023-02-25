//package testsocket;

import java.net.*;
import java.util.StringTokenizer;
import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.sql.*;
/*
 *clientA 
 */
class TestClient extends JFrame implements Runnable{
//public class TestClient extends JFrame implements Runnable{
	protected int PORT = 8888;
	protected String IP = "127.0.0.1";
	protected String name = "";
	JList<String> lstMsg = new JList<>();
	DefaultListModel<String> lstMsgmodel = new DefaultListModel<>();
	JTextField txtInput = new JTextField(20);
	JButton btnSend = new JButton("发送");
	JButton btnStart = new JButton("Start Connection");
	
	private void init() {
		JPanel pnl = new JPanel();
		pnl.add(txtInput);
		pnl.add(btnSend);
		pnl.add(btnStart);
		getContentPane().add(pnl,BorderLayout.SOUTH);
		
		JPanel pnl2 = new JPanel();
		pnl2.add(new JScrollPane(lstMsg));
		getContentPane().add( pnl2,BorderLayout.CENTER);
		
		lstMsg.setCellRenderer(new MyListCellRendered());
		lstMsg.setFixedCellWidth(360);
		lstMsg.setFixedCellHeight(28);
		lstMsg.setBackground(Color.gray);

		lstMsg.setModel(lstMsgmodel);
		
		btnSend.addActionListener( e -> {
			if(!bConnected) {
				System.out.println("No Connection");
			}else{
				if(txtInput.getText().length() != 0) {
					try {
						sendMsg(name +"##" + txtInput.getText());
						processMsg("00##"+name +"##" + txtInput.getText());
						sqlsave("00##"+name +"##" + txtInput.getText());
						txtInput.setText("");
					}catch(IOException e2) {
						System.out.print(e2.toString());
					}
				}	
			}					
		});
		
		btnStart.addActionListener(e -> {
			startConnect();
		});
		
		this.setSize(460,320);//340,220
		this.setTitle("ChatA");
		this.setResizable(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);		
		this.setVisible(true);	
	}
	
	TestClient(){
		sqlinit();
		firstinit();
		init();		
		wordsinit();
	}
	
	public void sendMsg(String msg) throws IOException{
		out.println(msg);
		out.flush();
	}
	
	public String receiveMsg() throws IOException{
		String msg = in.readLine();
		return msg;
	}
	
	public void processMsg(String str) {
		SwingUtilities.invokeLater(() -> {
			lstMsgmodel.addElement(str);
		});
		
	}
	
	private Statement stat;
	private Connection conn;
	
	public void sqlinit() {
		try {
			Class.forName("org.sqlite.JDBC");
			String connString = "jdbc:sqlite:clientA.db";
			conn = DriverManager.getConnection(connString);
			conn.setAutoCommit(false);
			stat = conn.createStatement();
			stat.executeUpdate("create table if not exists client_A"
					+ "(name char(20),sentence char(500));");
			conn.commit();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void wordsinit() {
		try {
			String sql = "select * from client_A;";
			ResultSet rs = stat.executeQuery(sql);
			while(rs.next()) {
				String sentence = rs.getString("sentence");
				processMsg(sentence);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void sqlsave(String msg) {
		try {
			stat.executeUpdate("insert into client_A values ('" + name +"','" + msg + "');");
			conn.commit();
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	boolean bConnected = false;
	Socket sock;
	BufferedReader in;
	PrintWriter out;
	Thread thread;
	
	public void startConnect() {
		if(!bConnected) {
			try {
				sock = new Socket(IP,PORT);
				bConnected = true;
				System.out.println("ClientA Connection ok!");
				in = new BufferedReader(new InputStreamReader(sock.getInputStream()) );
				out = new PrintWriter(sock.getOutputStream());
			}catch(IOException e) {
				e.printStackTrace();
				System.out.println("Connection faliled.");
			}
			if( thread == null ) {
				thread = new Thread(this);
				thread.start();
			}
		}
	}
	
	public void run() {
		while(true) {
			try {
				String msg = receiveMsg();				
				thread.sleep(100L);
				msg = "@@##" + msg;
				processMsg(msg);
				sqlsave(msg);
			}catch (IOException e) {
				// 
//				e.printStackTrace();
//				System.out.println("ooo");
			}catch (InterruptedException e) {
				// TODO: handle exception
			}
		}
	}
	//图片，IP地址，端口
	//名字，IP地址，端口
	public void firstinit() {
		JDialog dialog = new JDialog(this,"设置",true);
		dialog.setLayout(new FlowLayout());
		
		JTextField inputIp = new JTextField("127.0.0.1",15);
		JTextField inputPort = new JTextField("8888",8);
		JTextField inputName = new JTextField("请输入两个字符",8);
		JButton btnOk = new JButton("确定");
		JButton btnCancel = new JButton("取消");
		JPanel pnl1 = new JPanel();
		pnl1.add(new JLabel("服务器地址:"));
		pnl1.add(inputIp);
		JPanel pnl2 = new JPanel();
		pnl2.add(new JLabel("端口:"));
		pnl2.add(inputPort);
		JPanel pnl3 = new JPanel();
		pnl3.add(new JLabel("昵称"));
		pnl3.add(inputName);
		JPanel pnl4 = new JPanel();
		pnl4.add(btnOk);pnl4.add(btnCancel);
		dialog.add(pnl1);dialog.add(pnl2);dialog.add(pnl3);dialog.add(pnl4);
		
		btnOk.addActionListener(e -> {
			IP = inputIp.getText();
			PORT = (int)Double.parseDouble(inputPort.getText());
			name = inputName.getText();
			dialog.dispose();

		});
		
		btnCancel.addActionListener(e -> {
			System.exit(0);
		});
		

		dialog.setSize(280,200);
		dialog.setLocationRelativeTo(null);
		dialog.setVisible(true);
		dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);		
	}
	
	
	

//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//			new TestClient();
//		});
//	}
	
}


