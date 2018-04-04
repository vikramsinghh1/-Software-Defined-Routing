
// HW2:  Outline of Producer-Consumer:

import java.util.Random;

public class ProducerConsumer {
	public static void main(String[] args) {
		DropBox db = new DropBox(5);
		Producer p = new Producer(db);
		Consumer c = new Consumer(db);
		p.start();
		c.start();
	}
}

class Producer extends Thread {
	private DropBox db;

	public Producer(DropBox db) {
		this.db = db;
	}

	public void run() {
		for (int i = 0; i < 20; i++) {
			System.out.println("Producer produced "+i);
			db.put(i);
			try {
				Thread.sleep(new Random().nextInt(100));
			} catch (Exception e) {
			}
		}
	}
}

class Consumer extends Thread {
	private DropBox db;
	int value;

	public Consumer(DropBox db) {
		this.db = db;
	}

	public void run() {
		while (true) {
			value = db.get();
			System.out.println("Consumer consumed \n"+value);
			try {
				Thread.sleep(new Random().nextInt(500));
			} catch (Exception e) {
			}
		}
	}
}

class DropBox {

	private int count = 0, p = 0, g = 0, size;
	int circularbuffer[];
	boolean lastConsumed = false,lastProduced =false;

	DropBox(int dbox) {
		size = dbox;
		circularbuffer = new int[dbox];
	}

	boolean isEmpty() {
		
		if(count == 0)
			return true;
		else
			return false;
	}

	boolean isFull() {
		
		if(count == size)
			return true;
		else 
			return false;
	}

	public  void put(int i) {
		synchronized(this)
		{
		try {

			while (isFull())
				wait();
			if (p < size-1) {
				circularbuffer[p] = i;
				p++;
				count++;
			} else {
				circularbuffer[p] = i;
				p = 0;
				count++;
			}

			notify();

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	}

	public  int get() {
	
		synchronized(this)
		{
		try {
			while(isEmpty())
				wait();
			
			int num;
			
			if (g < size-1) {
				num = circularbuffer[g];
				//circularbuffer[g] =0;
				g++;
				count--;
			} else {
				num = circularbuffer[g];
			//	circularbuffer[g] = 0;
				g = 0;
				count--;
			}
			notify();
			return num;
		} 
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		return 0;
	}}

}
