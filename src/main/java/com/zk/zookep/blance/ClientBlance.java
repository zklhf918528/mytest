package com.zk.zookep.blance;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import com.zk.zookep.Constants;

public class ClientBlance {
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
			System.out.println(list);
		} catch (KeeperException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private synchronized String getChild(){
		boolean isMax = false;
		if(list  != null && list.size() > 0){
			for(String s : list){
				if(oldString != null){
					if(s.compareTo(oldString) > 0){
						oldString = s;
						isMax = true;
						break;
					}
				}
			}
			if(oldString == null || !isMax){
				oldString = list.get(0);
			}
		}else{
			return null;
		}
		return oldString;
	}
	public static void main(String[] args) {
		ClientBlance cl = new ClientBlance();
		cl.createZoo();
		for(int i =0;i<100;i++){
			if(i%10 == 0){
				try {
					System.out.println("负载中------------------》");
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			System.out.println(cl.getChild());
		}
		//
				
	}
}
