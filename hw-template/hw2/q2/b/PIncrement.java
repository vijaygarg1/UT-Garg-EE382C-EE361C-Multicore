package q2.b;

import java.util.concurrent.TimeUnit;

import q2.b.Incrementer;
import q2.b.IntHolder;

public class PIncrement {

    public static int parallelIncrement(int c, int numThreads) {
    	int total = 120000;
    	int perThread = total / numThreads;
    	IntHolder count = new IntHolder(0);
    	IntHolder counter = new IntHolder(120000);
    	IntHolder x = new IntHolder(0);
    	IntHolder y = new IntHolder(0);
    	boolean[] b = new boolean[numThreads];
    	
        Incrementer[] threadList = new Incrementer[numThreads];
        for(int i = 0; i < numThreads; i++){
        	threadList[i] = new Incrementer(count,x,y,b,numThreads,i,perThread);
        	threadList[i].start();
        }
        
        for(int i = 0; i < numThreads; i++){
        	try {
				threadList[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        
        return counter.get();
    }
    
    

}

class Incrementer extends Thread{
	private IntHolder c;
	private IntHolder x;
	private IntHolder y;
	private boolean[] b;
	private int numThreads;
	private final int pid;
	private int incrementNumber;
	
	public Incrementer(IntHolder c, IntHolder x, IntHolder y, boolean[] b, int numThreads, int pid, int incrementNumber){
		this.c = c;
		this.x = x;
		this.y = y;
		this.b = b;
		this.numThreads = numThreads;
		this.pid = pid;
		this.incrementNumber = incrementNumber;
	}
	
	@Override
	public void run(){
		for(int i = 0; i < incrementNumber; i++){
			b[pid] = true;
			x.set(pid);
			if(y.get() != 0){
				b[pid] = false;
				while(y.get() != 0){};
				i--;
				continue;
			}
			y.set(pid);
			if(x.get() != pid){
				b[pid] = false;
				for(int j = 0; j < numThreads; j++){
					while(b[j]){};
				}
				if(y.get() != pid){
					while(y.get() != 0){};
					i--;
					continue;
				}
			}
			c.increment();
			y.set(0);
			b[pid] = false;
		}
	}
}

class IntHolder{
	private int i;
	public IntHolder(int i){
		this.i = i;
	}
	public int get(){
		return i;
	}
	public void increment(){
		i++;
	}
	public void set(int i){
		this.i = i;
	}
}