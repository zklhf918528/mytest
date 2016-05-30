package com.zk.zookep.test;

import java.io.IOException;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.ZooKeeper;

import com.zk.zookep.Constants;

public class Service {
	private static ZooKeeper zk = null;
	
	private void createZoo(){
		try {
			zk = new ZooKeeper(Constants.HOST, Constants.TIME_OUT, new Watcher(){

				@Override
				public void process(WatchedEvent event) {
					if(event.getPath() != null && event.getPath().equals("/aa") && event.getType() == EventType.NodeCreated){
						System.out.println(123);
					}
					try {
						zk.getChildren("/aa", true);
						
//						zk.exists("/aa", true);
					} catch (KeeperException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println(event.getType());
					System.out.println(event.getType()+","+event.getPath());
				}
				
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Service s = new Service();
		s.createZoo();
		try {
			Thread.sleep(Long.MAX_VALUE);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
