package com.zk.zookep.test;

import java.io.IOException;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

import com.zk.zookep.Constants;

public class Client {
private static ZooKeeper zk = null;
	
	private void createZoo(){
		try {
			zk = new ZooKeeper(Constants.HOST, Constants.TIME_OUT, new Watcher(){

				@Override
				public void process(WatchedEvent event) {
				}
				
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void test(){
		try {
//			zk.create("/aa/a1", "1".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
//			zk.setData("/aa", "2".getBytes(), -1);
			zk.delete("/aa/a1", -1);
		} catch (KeeperException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Client s = new Client();
		s.createZoo();
		s.test();
		try {
			Thread.sleep(Long.MAX_VALUE);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
