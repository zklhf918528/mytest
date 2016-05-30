package com.zk.zookep.namespace;

import java.io.IOException;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import com.zk.zookep.Constants;

public class MySqlNameSpace {
	
	private static ZooKeeper zk = null;
	
	public void createZoo(){
		try {
			zk = new ZooKeeper(Constants.HOST, Constants.TIME_OUT, new Watcher(){
				@Override
				public void process(WatchedEvent event) {
					System.out.println("已经触发了" + event.getType() + "事件！"); 
				}});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getMySqlConn(){
		try {
			return new String(zk.getData("/mysqlConn",true,null));
		} catch (KeeperException | InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static void main(String[] args) {
		MySqlNameSpace mn = new MySqlNameSpace();
		mn.createZoo();
		System.out.println(mn.getMySqlConn());
	}
}
