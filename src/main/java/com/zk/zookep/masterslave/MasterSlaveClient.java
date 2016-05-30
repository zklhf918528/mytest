package com.zk.zookep.masterslave;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import com.zk.zookep.Constants;

public class MasterSlaveClient {
private static ZooKeeper zk = null;
	
	private static List<String> list = null;
	
	private static String oldString = null;
	
	public void createZoo(){
		try {
			zk = new ZooKeeper(Constants.HOST, Constants.TIME_OUT, new Watcher(){
					@Override
					public void process(WatchedEvent event) {
							getList();
					}});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private synchronized void getList(){
		try {
			list = zk.getChildren("/subs", true);
			Collections.sort(list);
			if(list == null || list.size() == 0){
				oldString = null;
			}
			oldString = list.get(0);
		} catch (KeeperException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private synchronized String getChild(){
		return oldString;
	}
	public static void main(String[] args) {
		MasterSlaveClient cl = new MasterSlaveClient();
		cl.createZoo();
		for(int i =0;i<10;i++){
				try {
					System.out.println("主从------------------》");
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			System.out.println(cl.getChild());
		}
	}
}
