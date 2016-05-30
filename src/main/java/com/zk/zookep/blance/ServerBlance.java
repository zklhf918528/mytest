package com.zk.zookep.blance;

import java.io.IOException;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

import com.zk.zookep.Constants;

public class ServerBlance {
	private static ZooKeeper zk = null;
	
	private void createZoo(){
		try {
			zk = new ZooKeeper(Constants.HOST, Constants.TIME_OUT, new Watcher(){
				@Override
				public void process(WatchedEvent event) {
				}});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void createNode(){
		try {
			String path = zk.create("/subs/sub", "sub".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
			System.out.println(path);
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		ServerBlance s = new ServerBlance();
		s.createZoo();
		try {
			s.createNode();
			Thread t1 = new Thread(){
				@Override
				public void run() {
					try {
						zk.close();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					super.run();
				}
			};
			Runtime.getRuntime().addShutdownHook(t1);
			Thread.sleep(Long.MAX_VALUE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
