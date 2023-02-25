//package testsocket;

import java.io.*;
import java.net.*;
import java.util.List;
class TestserverTwo{
//public class TestserverTwo{
	
	private static final int DEFAULT_PORT_A = 8888;
	private static final int DEFAULT_PORT_B = 8887;
	protected List<String> messageList;
	static BufferedReader Ain,Bin;
	static PrintWriter Bout,Aout;
	public static void init() {
		try {
			System.out.println("Server starting.");
			ServerSocket serversocketA = new ServerSocket(DEFAULT_PORT_A);  
			ServerSocket serversocketB = new ServerSocket(DEFAULT_PORT_B); 
			Socket socketA = serversocketA.accept();
			Socket socketB = serversocketB.accept();
			Ain = new BufferedReader(new InputStreamReader(socketA.getInputStream()));
			Bin = new BufferedReader(new InputStreamReader(socketB.getInputStream()));
			Aout = new PrintWriter(socketA.getOutputStream());
			Bout = new PrintWriter(socketB.getOutputStream());	
			System.out.println("Server connection ok.");
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void start() {
		init();
		bufferedmsg atob = new bufferedmsg(),btoa = new bufferedmsg();
		readmessage r1 = new readmessage(Ain, atob);
		readmessage r2 = new readmessage(Bin, btoa);
		writemessage w1 = new writemessage(Aout, btoa);
		w1.setDaemon(true);
		writemessage w2 = new writemessage(Bout, atob);
		w2.setDaemon(true);
		r1.start();
		r2.start();
		w1.start();
		w2.start();
	}
//	public static void main(String[] args) {
//		init();
//		bufferedmsg atob = new bufferedmsg(),btoa = new bufferedmsg();
//		readmessage r1 = new readmessage(Ain, atob);
//		readmessage r2 = new readmessage(Bin, btoa);
//		writemessage w1 = new writemessage(Aout, btoa);
//		w1.setDaemon(true);
//		writemessage w2 = new writemessage(Bout, atob);
//		w2.setDaemon(true);
//		r1.start();
//		r2.start();
//		w1.start();
//		w2.start();		
//		
//	}
}
class readmessage extends Thread{
	BufferedReader in;
	private bufferedmsg bmsg;
	public readmessage(BufferedReader  in,bufferedmsg bmsg) {
		// TODO Auto-generated constructor stub
		this.in = in;
		this.bmsg = bmsg;
	}
	
	public void run() {
		try {
			while(true) {
				String line = receiveMsg();
//				if (line == null) continue;
				Thread.sleep(100L);
				bmsg.put(line);				
			}
		}catch(IOException e) {
//			e.printStackTrace();
			System.out.println("Connection finished.");
			try {
				in.close();
			}catch (IOException e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
		}catch (InterruptedException e2) {
			// TODO: handle exception
		}		
	}
	
	public String receiveMsg() throws IOException{
		String msg = in.readLine();
		return msg;
	}	
}

class writemessage extends Thread{
	PrintWriter out;
	private bufferedmsg bmsg;
	public writemessage(PrintWriter out,bufferedmsg bmsg) {
		// TODO Auto-generated constructor stub
		this.out = out;
		this.bmsg = bmsg;
	}
	
	public void run() {
		try {
			while(true) {
				String line = bmsg.get();
				sendMsg(line);
			}
		}catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public void sendMsg(String msg) throws IOException{
		out.println(msg);
		out.flush();
	}
}

class bufferedmsg{
	private String string;
	private boolean available = false;
	
	public synchronized String get() {
		while(available == false) {
			try {
				wait();
			}catch(InterruptedException e) {
				
			}
		}
		available = false;
		notify();
		return string;
	}
	
	public synchronized void put(String str) {
		while(available == true) {
			try {
				wait();
			}catch(InterruptedException e) {
				
			}
		}
		string = str;
		available = true;
		notify();
	}
}