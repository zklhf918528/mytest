package com.zk.zookep.queue;

import java.io.IOException;
import java.util.ArrayList;
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

public class QueueSender extends Thread {
	private ZooKeeper zk = null;
	
	private static List<String> list = new ArrayList<String>();

	private void createZoo() {
		try {
			zk = new ZooKeeper(Constants.HOST, Constants.TIME_OUT, new Watcher() {
				@Override
				public void process(WatchedEvent event) {
					try {
						zk.exists("/queue", true);
					} catch (KeeperException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					if(event.getType().equals(EventType.NodeChildrenChanged)){
						String path = event.getPath();
						try {
							Stat st = zk.exists(path, true);
							String data = new String(zk.getData(path, true, st));
							System.out.println(data);
							if("1".equals(data)){
								zk.delete(path, -1);
							}else{
								zk.setData(path, "0".getBytes(), 1);
							}
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

	private void  removeNode(){
		
	}
	
	private synchronized void senderMsg() {
		try {
			String path = zk.create("/queue/qu", "0".getBytes(), Ids.OPEN_ACL_UNSAFE,
					CreateMode.PERSISTENT_SEQUENTIAL);
			list.add(path);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	public static void main(String[] args) {
//		for (int i = 0; i < 3; i++) {
			QueueSender q = new QueueSender();
			Thread t = new Thread(q);
			t.start();
//		}
	}

	@Override
	public void run() {
		createZoo();
//		for(int i = 0;i<3;i++){
			senderMsg();
//		}
		try {
			Thread.sleep(Long.MAX_VALUE);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
