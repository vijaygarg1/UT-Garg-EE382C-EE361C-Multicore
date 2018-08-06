import java.util.Arrays;

class PetersonN implements Lock {
    int N;
    int[] gate; 
    int[] last; 
    public PetersonN(int numProc) {
        N = numProc;
        gate = new int[N];//We only use gate[1]..gate[N-1]; gate[0] is unused
        Arrays.fill(gate, 0);
        last = new int[N];
        Arrays.fill(last, 0);
    }
    public void requestCS(int i) {
	for (int k = 1; k < N; k++) { 
           gate[i] = k; 
           last[k] = i;
           for (int j = 0; j < N; j++) {
               while ((j != i) &&  // there is some other process
	              (gate[j] >= k) &&  // that is ahead or at the same level
                      (last[k] == i)) // and I am the last to update last[k]
               {};// busy wait
           }
        }
    }
    public void releaseCS(int i) {
        gate[i] = 0;
    }
}
