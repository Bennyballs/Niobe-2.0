package src;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.*;
import javax.swing.*;
import java.net.*;
import java.io.*;

class ScreenClient extends JFrame{
	private static final long serialVersionUID = 1L;
	//	port no 143 for sending images
	//	port no 365 for receiving mouse events
	//	port no 333 for receiving key events
	Socket imgSender;
	ServerSocket mouseEventSender;
	ServerSocket keyboardEventSender;
	public static void main(String args[]){
		new ScreenClient();
	}
	ScreenClient(){
		super("Remote Access Client");
		Container c=getContentPane();
		Toolkit t=Toolkit.getDefaultToolkit();
		Dimension d=t.getScreenSize();
		setBounds(d.width/4,d.height/4,d.width/2,(d.height/4+10));
		setResizable(false);
		Image img=t.createImage("d:/Client.jpg");
		c.add(new ImagePanel(img),BorderLayout.NORTH);
		JPanel center=new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel status;
		center.add(status=new JLabel("Waiting for the server to connect"));
		JPanel lower=new JPanel();
		final JButton cancel;
		lower.add(cancel=new JButton("Cancel"));
		cancel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent a){
				System.exit(0);
			}
		});
		c.add(center);
		c.add(lower,BorderLayout.SOUTH);
		addWindowListener(new WindowAdapter(){
			public void windowClosed(WindowEvent w){
				
			}
			public void windowClosing(WindowEvent w){
				System.exit(0);
			}
		});
		setVisible(true);
		InetAddress address=null;
		try{
			ServerSocket s=new ServerSocket(123);
			Socket socket=s.accept();
			address=socket.getInetAddress();
			status.setForeground(Color.green);
			status.setText("Connect to pc:"+address.getHostName());
			Thread.sleep(500);
		}catch(Exception e){}
		setState(Frame.ICONIFIED);
		new ImageSender(address.getHostName());
		new MouseEventsReceiver();
		new KeyboardEventsReceiver();
	}
}
class ImagePanel extends JPanel{
	private static final long serialVersionUID = 1L;
	Image img;
	ImagePanel(Image img){
		this.img=img;
		MediaTracker mt=new MediaTracker(this);
		mt.addImage(img,1);
		try{
			mt.waitForID(1);
		}catch(Exception e){System.out.println(e);}
		setPreferredSize(new Dimension(img.getWidth(this),img.getHeight(this)));
	}
	public void paint(Graphics g){
		super.paint(g);
		g.drawImage(img,0,0,this);
	}
}
class ImageSender implements Runnable{		
	String serverName;
	ImageSender(String s){
		serverName=s;
		Thread t=new Thread(this);
		t.start();
	}
	public void run(){
		try{
			InetAddress address=InetAddress.getByName(serverName);

			Robot r=new Robot();
			Toolkit t=Toolkit.getDefaultToolkit();
			Rectangle rect=new Rectangle(t.getScreenSize());
			while(true){
				Socket s=new Socket(address,143);			
				OutputStream out=s.getOutputStream();
				ObjectOutputStream oos=new ObjectOutputStream(out);
				BufferedImage b_img=r.createScreenCapture(rect);
				ImageIcon img=new ImageIcon(b_img);
				oos.writeObject(img);
				oos.close();	
				out.close();
			}
		}catch(Exception e){System.out.println(e);}
	}	
}
class MouseEventsReceiver implements Runnable{
	MouseEventsReceiver(){
		Thread t=new Thread();
		t.start();
	}
	public void run(){
		try{
			Robot r=new Robot();
			while(true){
				ServerSocket s=new ServerSocket(321);			
				System.out.println("Socket created at port 321");
				Socket client=s.accept();
				InputStream in=client.getInputStream();
				ObjectInputStream ois=new ObjectInputStream(in);
				MouseEvents m=(MouseEvents)ois.readObject();
				System.out.println("Mouse Event Received X="+m.getX());
				r.mouseMove(m.getX(),m.getY());
				r.mousePress(m.getButton());
				ois.close();
				in.close();
			}
		}catch(Exception e){System.out.println(e);}
	}
}

class KeyboardEventsReceiver implements Runnable{
	KeyboardEventsReceiver(){
		Thread t=new Thread();
		t.start();
	}
	public void run(){
		try{
			Robot r=new Robot();
			while(true){
				ServerSocket s=new ServerSocket(365);			
				Socket client=s.accept();
				InputStream in=client.getInputStream();
				ObjectInputStream ois=new ObjectInputStream(in);
				KeyboardEvents k=(KeyboardEvents)ois.readObject();
				System.out.println("Key Received="+k.getKeyCode());
				r.keyPress(k.getKeyCode());
				ois.close();
				in.close();
			}
		}catch(Exception e){System.out.println(e);}
	}
}
