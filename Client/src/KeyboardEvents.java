package src;


import java.io.*;
class KeyboardEvents implements Serializable{
	private static final long serialVersionUID = 1L;
	int code;
	KeyboardEvents(int code){
		this.code=code;
	}
	public void setKeyCode(int code){
		this.code=code;
	}
	public int getKeyCode(){
		return code;
	}
}