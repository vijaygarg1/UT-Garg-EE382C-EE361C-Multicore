package q5;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Frequency implements Callable<Integer> {
	int start;
	int end;
	int[] A;
	int x;
	
	public Frequency (int start, int end, int[] A, int x) {
		this.start = start;
		this.end = end;
		this.A = A;
		this.x = x;
	}
	
	public static int parallelFreq(int x, int[] A, int numThreads){
		if (numThreads == 0) {
			return -1;
		}
		
		int totalFreq = 0;
		ExecutorService es = Executors.newCachedThreadPool();
		Frequency[] frequencies = new Frequency[numThreads];
		
		int range = A.length/numThreads;
		
		for (int i = 0; i < numThreads; i++) {
			int start = i*range;
			int end = start + range;
			if (i == numThreads - 1) {
				end = A.length;
			}
			frequencies[i] = new Frequency(start, end, A, x);
		}
		
		try {
			List<Future<Integer>> results = es.invokeAll(Arrays.asList(frequencies));
			for (Future<Integer> future : results) {
				totalFreq += future.get();
			}
		} catch (InterruptedException | ExecutionException e) {
			return -1;
		}
		
		es.shutdown();
		return totalFreq;
    } 
	
	public Integer call() throws Exception {
		int freq = 0;
		for (int i = start; i < end; i++) {
			if (A[i] == x) {
				freq++;
			}
		}
		return freq;
	}
}
