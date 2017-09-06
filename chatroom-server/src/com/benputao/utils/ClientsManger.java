package com.benputao.utils;

import java.util.HashMap;
import java.util.Map;

import com.benputao.listen.ClientManger;

public class ClientsManger {
	public static Map<String, ClientManger>mangers=new HashMap<>();
	
	public static Map<String, ClientManger>getClientManagers(){
		return mangers;
	}
	public static ClientManger getClientManager(String key){
		return mangers.get(key);
	}
	public static void putClientManager(String key,ClientManger manger){
		mangers.put(key, manger);
	}
	public static void removeClientManager(String key){
		mangers.remove(key);
	}
	public static void removeClientManager(String key,ClientManger manger){
		mangers.remove(key,manger);
	}
	public static int getSize(){
		return mangers.size();
	}
}
