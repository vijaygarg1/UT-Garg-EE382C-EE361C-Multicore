// You do NOT need to modify this file
// The interface of the ListBasedSet 
// You should implement CoarseGrainedListBasedSet, FineGrainedListBasedSet and
// LockFreeListBasedSet by extending this class
package q5;

public interface ListSet {
  // Appends the specified element to the end of this list
  // Return true if successful
  public boolean add(int value);

  // Removes the first occurrence of the specified element from this list, if
  // it is present
  // Return true if successful
  public boolean remove(int value);

  // Returns true if this list contains the specified element.
  public boolean contains(int value);

}
