package q2.a;

import java.util.concurrent.TimeUnit;

public class PIncrement {

    public static int parallelIncrement(int c, int numThreads) {
    	int total = 120000;
    	int perThread = total / numThreads;
    	IntHolder count = new IntHolder(0);
    	IntHolder x = new IntHolder(0);
    	
        Incrementer[] threadList = new Incrementer[numThreads];
        for(int i = 0; i < numThreads; i++){
        	threadList[i] = new Incrementer(count,x,i,perThread);
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
        
        return count.get();
    }
    
    

}

class Incrementer extends Thread{
	private IntHolder c;
	private IntHolder x;
	private final int pid;
	private int incrementNumber;
	
	public Incrementer(IntHolder c, IntHolder x, int pid, int incrementNumber){
		this.c = c;
		this.x = x;
		this.pid = pid;
		this.incrementNumber = incrementNumber;
	}
	
	@Override
	public void run(){
		for(int i = 0; i < incrementNumber; i++){
			if(x.get() == 0){
				while(x.get() != pid){
					x.set(pid);
					try {
						TimeUnit.MICROSECONDS.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				try{
					c.increment();
				}
				finally{
					x.set(0);
				}
			}
			else{
				i--;
			}
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