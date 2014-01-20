package com.dropit;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class DropItPacket implements Serializable {

	private String METHOD = "";
	private Map<String, String> values;
	private byte[] DATA;

	public DropItPacket(String methodname) {
		METHOD = methodname;
		values = new HashMap();
	}

	public String getMETHOD() {
		return METHOD;
	}
	
	
	public void setMETHOD(String m) {
		this.METHOD = m;
	}

	public void setKeyValue(String key, String value) {
		values.put(key, value);
	}

	public String getKeyValue(String key) {
		if (values.containsKey(key)) {
			return values.get(key);
		}
		return "";
	}

	public byte[] getDATA() {
		return DATA;
	}

	public void setDATA(byte[] dATA) {
		DATA = dATA;
	}

	
	
}
