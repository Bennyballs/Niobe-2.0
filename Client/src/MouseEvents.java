package src;

import java.io.*;
class MouseEvents implements Serializable{
	private static final long serialVersionUID = 1L;
	int x,y,button;
	MouseEvents(int x,int y,int button){
		setEvents(x,y,button);
	}
	public void setEvents(int x,int y,int button){
		this.x=x;
		this.y=y;
		this.button=button;
	}
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	public int getButton(){
		return button;
	}
}
