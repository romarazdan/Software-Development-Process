package iothtr;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

public class ConsoleBox {
	
	public static int STANDARD = 0;
	public static int WARNING = 1;
	public static int EMERGENCY = 2;
	
	private JPanel container;
	private JPanel parent;
	private JScrollPane scroll;
	private ArrayList<JLabel> buffer = new ArrayList<JLabel>(); 
	
	public ConsoleBox(JPanel _parent) {
		parent = _parent;
		container = new JPanel();
		container.setBackground(Color.black);
		container.setPreferredSize(new Dimension(500, 0));
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
		scroll = new JScrollPane(container);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		parent.add(scroll);
	}
	
	public void sendMessage(String message, int severity) {
		JLabel newMessage = new JLabel();
		if (severity == 0 ) {
			newMessage.setForeground(Color.WHITE);
		}
		else if (severity == 1) {
			newMessage.setForeground(Color.YELLOW);
		}
		else {
			newMessage.setForeground(Color.RED);
		}
		newMessage.setAlignmentX(Component.LEFT_ALIGNMENT);
		buffer.add(newMessage);
		container.add(newMessage);
		newMessage.setText("<html> >>> " + message + "</html>");
		container.setPreferredSize(new Dimension(500, (buffer.size() * 16) + 150));
		JScrollBar vertical = scroll.getVerticalScrollBar();
		vertical.setValue(vertical.getMaximum());
		
		LogHandler.writeLog("IoTEngine", "Message logged \"" + message + "\"");
	}
	
	public void clearConsole() {
		for (int i = 0; i < buffer.size(); i++) {
			buffer.get(i).setVisible(false);
			container.remove(buffer.get(i));
		}
		buffer.clear();
		container.setPreferredSize(new Dimension(400, 0));
	}
}
