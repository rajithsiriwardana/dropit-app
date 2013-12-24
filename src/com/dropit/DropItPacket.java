package com.dropit;

import java.io.Serializable;

public class DropItPacket implements Serializable {

	private String METHOD = "STORE";
	private String FILE_NAME = "";
	private byte[] DATA;
	
	public String getMETHOD() {
		return METHOD;
	}
	public void setMETHOD(String mETHOD) {
		METHOD = mETHOD;
	}
	public String getFILE_NAME() {
		return FILE_NAME;
	}
	public void setFILE_NAME(String fILE_NAME) {
		FILE_NAME = fILE_NAME;
	}
	public byte[] getDATA() {
		return DATA;
	}
	public void setDATA(byte[] dATA) {
		DATA = dATA;
	}

	
	
}
