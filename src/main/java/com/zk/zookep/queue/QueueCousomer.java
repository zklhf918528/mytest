package com.zk.zookep.queue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import com.zk.zookep.Constants;

public class QueueCousomer{
	private ZooKeeper zk = null;

	private static List<String> list = null;
	
	private void createZoo() {
		try {
			zk = new ZooKeeper(Constants.HOST, Constants.TIME_OUT, new Watcher() {
				@Override
				public void process(WatchedEvent event) {
//					if(event.getType().equals(EventType.NodeChildrenChanged)){
//						String path = event.getPath();
//						try {
//							System.out.println(path);
//							
//							System.out.println(path);
//						} catch (Exception e) {
//							e.printStackTrace();
//						} 
//					}
					System.out.println(event.getType());
					getList();
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void getList() {
		try {
			List<String> path = zk.getChildren("/queue", true);
			List<String> oldPath = new ArrayList<>();
			for(String pa : path){
				String s = new String(zk.getData("/queue/"+pa, true, zk.exists("/queue/"+pa, true)));
				if(!"1".equals(s)){
					zk.setData("/queue/"+pa, "1".getBytes(), -1);
					System.out.println("修改数据"+"/queue/"+pa);
				}else{
					oldPath.add(pa);
				}
			}
			System.out.println(zk.setData("/queue", (oldPath+"").getBytes(), -1));
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	public static void main(String[] args) {
		QueueCousomer q = new QueueCousomer();
		q.createZoo();
		try {
			Thread.sleep(Long.MAX_VALUE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
