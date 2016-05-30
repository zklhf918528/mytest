package com.zk.zookep.doublebarrier;

import java.io.IOException;
import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import com.zk.zookep.Constants;

public class Barrier {
	private static ZooKeeper zk = null;
	
	private static List<String> list = null;
	private void createProcessZoo(){
		try {
			zk = new ZooKeeper(Constants.HOST, Constants.TIME_OUT,new Watcher(){
				@Override
				public void process(WatchedEvent event) {
					if(event.getType().equals(EventType.NodeChildrenChanged)){
						getProcessList();
					}else{
						try {
							list = zk.getChildren("/barrier/process", true);
						} catch (KeeperException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private synchronized void getProcessList(){
		try {
			/**
			 * 监听process当process达到5时启动readyZk并创建ready节点
			 */
			list = zk.getChildren("/barrier/process", true);
			System.out.println("processList-----"+list);
			if(list != null){
				if(list.size() >= 6){
					Stat st = zk.exists("/barrier/ready", true);
					if(st == null){
						zk.create("/barrier/ready", "true".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
						System.out.println("达到指定个数，创建ready节点");
					}
				}
			}
		} catch (KeeperException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Barrier b = new Barrier();
		b.createProcessZoo();
		Thread t1 = new Thread(){
			@Override
			public void run() {
				try {
					zk.delete("/barrier/ready", -1);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				super.run();
			}
		};
		Runtime.getRuntime().addShutdownHook(t1);
		try {
			Thread.sleep(Long.MAX_VALUE);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
