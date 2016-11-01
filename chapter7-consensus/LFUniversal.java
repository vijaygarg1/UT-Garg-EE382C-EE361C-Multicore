// From Herlihy's Book

// Pseudo code for Lock Free Universal Construction.
public interface SeqObject {
   public abstract Response apply(Invocation invoc);
}


public class LFUniversal {
  private Node[] head;
  private Node tail = new Node(); 
  tail.seq = 1; 
  for (int j=0; j < n; j++){
      head[j] = tail;
  }
public Response apply(Invoc invoc) {
  int i = ThreadID.get();
  Node prefer = new node(invoc); 
  while (prefer.seq == 0) {
    Node before = Node.max(head);
    Node after = before.decideNext.decide(prefer);
    before.next = after; 
    after.seq = before.seq + 1;
    head[i] = after;
    }

    //compute my response
    SeqObject MyObject = new SeqObject();
    current = tail.next;
    while (current != prefer){ 
    MyObject.apply(current.invoc);
    current = current.next;
  } 
 return MyObject.apply(current.invoc);
}


