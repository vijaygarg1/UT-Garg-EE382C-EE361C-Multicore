public class Node {
 public Invoc invoc; 
 public Consensus<Node> decideNext; 
 public Node next; 
 public int seq;                      
 public Node(Invoc invoc) {
    invoc = invoc;
    decideNext = new Consensus<Node>()
    seq = 0;
  }

public static Node max(Node[] array) {
    Node max = array[0];
    for (int i = 1; i < array.length; i++)
      if (max.seq < array[i].seq)
        max = array[i];
      return max;
  }
}
