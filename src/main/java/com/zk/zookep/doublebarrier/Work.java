package com.zk.zookep.doublebarrier;

import java.io.IOException;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import com.zk.zookep.Constants;

public class Work extends Thread {
	private ZooKeeper zk = null;

	private void createZoo() {
		try {
			zk = new ZooKeeper(Constants.HOST, Constants.TIME_OUT, new Watcher() {
				@Override
				public void process(WatchedEvent event) {
					try {
						Stat st = zk.exists("/barrier/ready", true);
						if (st != null) {
							System.out.println("关掉===" + Thread.currentThread().getId());
							zk.close();
						}
					} catch (KeeperException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void createProcess() {
		try {
			String path = zk.create("/barrier/process/pro", "1".getBytes(), Ids.OPEN_ACL_UNSAFE,
					CreateMode.EPHEMERAL_SEQUENTIAL);
			System.out.println(path);
		} catch (KeeperException e) {
			// TODO Auto-generated catch blocks
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		for (int i = 0; i < 8; i++) {
			Work w = new Work();
			Thread t = new Thread(w);
			t.start();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void run() {
		createZoo();
		createProcess();
		try {
			Thread.sleep(Long.MAX_VALUE);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
