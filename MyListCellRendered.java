//package testsocket;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.util.StringTokenizer;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

public class MyListCellRendered extends DefaultListCellRenderer{
	String str;
	private final int arc = 8;
	private final int x = 0,y = 0;	
	private Font myFont = new Font("TimesRoman",Font.PLAIN,12);
	@Override
	public Component getListCellRendererComponent(JList<? extends Object> list,Object value,
			int index,boolean isSelected,boolean cellHasFocus) {
		setText(value.toString());
		str = value.toString();		
		return this;
	}	

	@Override
	public void paintComponent(Graphics g) {
//		super.printComponent(g); // ??为什么会报错
		
		this.setLayout(new FlowLayout(LEFT));
		StringTokenizer st = new StringTokenizer(str,"##");
		if(st.nextToken().equals("@@")) g.setColor(Color.white); else g.setColor(Color.green);
		JLabel lbprofilepic = new JLabel(st.nextToken());
		this.add(lbprofilepic);
		lbprofilepic.setSize(30,30);
		lbprofilepic.setLocation(0,0);
		String str1 = st.nextToken();
		g.fillRoundRect(30 + 4,2,8 + str1.length() * 15,25,arc,arc);
		g.setColor(Color.black);
		g.drawString(str1, x + 8 + 30 + 5, y + 18 +2);

	}	
}