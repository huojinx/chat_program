//package testsocket;
import javax.swing.*;
//import pk5.*;

public class ChatTwo {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Thread( () -> {
			new TestserverTwo().start();
		}).start();
		
		new Thread(() -> {
			SwingUtilities.invokeLater(() -> {
				new TestClient();
			});
		}).start();
//		SwingUtilities.invokeLater(() -> {
//			new TestClient();
//		});
		new Thread( () -> {
			SwingUtilities.invokeLater(() -> {
				new TestClientB();
			});
		}).start();
//		SwingUtilities.invokeLater(() -> {
//			new TestClientB();
//		});
	}
}
