// From Herlihy's Book

// Pseudo code for Wait Free Universal Construction.


public class WFUniversal {
  private Node[] announce; 
  private Node[] head;
  private Node tail = new node();
  tail.seq = 1;
  for (int j=0; j < n; j++){
    head[j] = tail; announce[j] = tail
  };

public Response apply(Invoc invoc) {
  int i = ThreadID.get();
  announce[i] = new Node(invoc);
  head[i] = Node.max(head); 
  while (announce[i].seq == 0) {
    Node before = head[i]; 
    Node help = announce[(before.seq + 1 % n)]; 
    if (help.seq == 0) 
     prefer = help;
    else
     prefer = announce[i]; 
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


