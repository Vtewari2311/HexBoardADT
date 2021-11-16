package edu.uwm.cs351;

import java.util.AbstractCollection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

import junit.framework.TestCase;

/**
 * An implementation of the HexBoard ADT using 
 * a binary search tree implementation.
 * A hex board is a collection of hex tiles except that there can 
 * never be two tiles at the same location. 
 */
public class HexBoard extends AbstractCollection<HexTile> {

	private static int compare(HexCoordinate h1, HexCoordinate h2) {
		if (h1.b() == h2.b()) {
			return h1.a() - h2.a();
		}
		return h1.b() - h2.b();
	}
	
	private static class Node {
		HexCoordinate loc;
		Terrain terrain;
		Node left, right;
		Node(HexCoordinate l, Terrain t) { loc = l; terrain = t; }
	}
	
	private Node root;
	private int size;
	private int version;
	
	private static boolean doReport = true; 
	private static boolean report(String s) {
		if (doReport) System.err.println("Invariant error: " + s);
		else System.out.println("Detected invariant error: " + s);
		return false;
	}
	
	/**
	 * Return true if the nodes in this BST are properly
	 * ordered with respect to the {@link #compare(HexCoordinate, HexCoordinate)}
	 * method.  If a problem is found, it should be reported (once).
	 * @param r subtree to check (may be null)
	 * @param lo lower bound (if any)
	 * @param hi upper bound (if any)
	 * @return whether there are any problems in the tree.
	 */
	private static boolean isInProperOrder(Node r, HexCoordinate lo, HexCoordinate hi) {
		if (r == null) return true;
		if (r.loc == null) return report("null location in tree");
		if (r.terrain == null) return report("null terrain for " + r.loc);
		if (lo != null && compare(lo,r.loc) >= 0) return report("out of order " + r.loc + " <= " + lo);
		if (hi != null && compare(hi,r.loc) <= 0) return report("out of order " + r.loc + " >= " + hi);
		return isInProperOrder(r.left,lo,r.loc) && isInProperOrder(r.right,r.loc,hi);
	}
	
	/**
	 * Return the count of the nodes in this subtree.
	 * @param p subtree to count nodes for (may be null)
	 * @return number of nodes in the subtree.
	 */
	private static int countNodes(Node p) {
		if (p == null) return 0;
		return 1 + countNodes(p.left) + countNodes(p.right);
	}
	
	private boolean wellFormed() {
		if (!isInProperOrder(root,null,null)) return false;
		int count = countNodes(root);
		if (size != count) return report("size " + size + " wrong, should be " + count);
		return true;
	}
	//Helper methods
        private Node getParent(Node p, Node root) {
                if(root == null || node == null)
                   return null;
                else if((root.right != null && root.right == p) || (root.left != null && root.left == p)
                    return root;
                else
                   Node parent = getParent(root.right, p);
                   if(parent == null)
                           parent = getParent(root.left, p);
                           return parent;
        }

        private Node CurrentNode() {
               Node currNode = new Node(current.getLocation(), current.getTerrain())
               return currNode;
        }

		
        private Node getSuccessor() {
               root = root.right;
               while(root.left != null) {
                    root = root.right;
               }
               return root;
        }

		

	private Node getPredecessor() {
               root = root.left;
               while(root.right != null) {
                    root = root.left;
               }
                return root;
        }

		

		//recursive method if target is found

        private boolean inOrder(Node target) {
               if(root == null)
                  return root;
               if(root == target)
                  return true;
                 else
                    root = root.right;
                 }
                 return false;
        }

	
	/**
	 * Create an empty hex board.
	 */
	public HexBoard() {
		root = null;
		size = 0;
		assert wellFormed() : "in constructor";
	}
	
	/** Return the terrain at the given coordinate or null
	 * if nothing at this coordinate.
	 * @param c hex coordinate to look for (null OK but pointless)
	 * @return terrain at that coordinate, or null if nothing
	 */
	public Terrain terrainAt(HexCoordinate l) {
		assert wellFormed() : "in terrainAt";
		for (Node p = root; p != null; ) {
			int c = compare(l,p.loc);
			if (c == 0) return p.terrain;
			if (c < 0) p = p.left;
			else p = p.right;
		}
		return null;
	}

	@Override // required by Java
	public Iterator<HexTile> iterator() {
		assert wellFormed() : "in iterator";
		return new MyIterator();
	}

	@Override // required by Java
	public int size() {
		assert wellFormed() : "in size";
		return size;
	}
	
	@Override // required for efficiency
	public boolean contains(Object o) {
		assert wellFormed() : "in contains()";
		if (o instanceof HexTile) {
			HexTile h = (HexTile)o;
			return terrainAt(h.getLocation()) == h.getTerrain();
		}
		return false;
	}

	@Override // required for correctness
	public boolean add(HexTile e) {
		assert wellFormed() : "in add()";
		Node lag = null;
		Node p = root;
		int c = 0;
		while (p != null) {
			c = compare(e.getLocation(),p.loc);
			if (c == 0) break;
			lag = p;
			if (c < 0) p = p.left;
			else p = p.right;
		}
		if (p != null) { // found it!
			if (p.terrain == e.getTerrain()) return false;
			p.terrain = e.getTerrain();
			// size doesn't increase...
		} else {
			p = new Node(e.getLocation(),e.getTerrain());
			++size;
			if (lag == null) root = p;
			else if (c < 0) lag.left = p;
			else lag.right = p;
		}
		++version;
		assert wellFormed() : "after add()";
		return true;
	}

	@Override // more efficient
	public void clear() {
		if (size > 0) {
			root = null;
			size = 0;
			++version;
		}
	}
	
	@Override //functionality
	public void remove() {
		throw new UnsupportedOperationException("no removal yet"); // TODO
                      // base case: no child and one child
                      if(curNode.getParent() == root) {
                       if(root.right == null && root.left == null){
                          root.curNode = null;
                       }
                      }
                      else if(curNode.getParent().left != null)
                          curNode = curNode.getParent().left;
                      else
                          curNode = curNode.getParent().right;
                      if(curNode.getParent() != null)
                        if(curNode == curNode.getParent().left)
                          curNode.getParent().left = curNode;
                          else
                             curNode.getParent().right = curNode;
                      else
                           root = curNode;
                       return;
                       
                      //Second Case: Two Children
                      currNode.getParent() = currNode.right;
                      Node temp = currNode;
	              while(currNode.getParent().left == null) {
                         temp = currNode.getParent();
                         currNode.getParent() = currNode.getParent().left;
                      }
                      currNode.root.getSuccessor() = currNode.getparent();
                      curNode.root.getPredecessor() = temp;
                      if(currNode.getParent() != null) {
                        if(currNode == currNode.getParent().left)
                          currNode.getParent().left = currNode.root.getSuccessor();
                         else
                            currNode.getParent().right = currNode.root.getSuccessor();
                      }
                     else
                        root = getSuccessor();
                      
	            currNode.root.getSuccessor().left = currNode.left;
                    currNode.root.getSuccessor().right = currNode.right;
                    return;
                    ++version;
       }

	
	private class MyIterator implements Iterator<HexTile> {
		// new data structure for iterator:
		private Stack<Node> pending = new Stack<>();
		private HexTile current; // if can be removed
		private int myVersion = version;
		
		private boolean wellFormed() {
			// TODO:
			Node curr = new Node(current.getLocation(), current.getTerrain());
			// 1. Check the outer invariant (see new syntax in homework description)
			   HexBoard.this.wellFormed();
			// 2. If we are stale, don't check anything else, pretend no problems
			   if(version != myVersion)
				   throw new ConcurrentModificationException();
			// 3. If current isn't null, there should be a node for it in the tree.
			   if(curr != null)
				   curr = new Node(current.getLocation(), current.getTerrain());
			           ++size;
			// 4. If current isn't null, the next node after it should be top of the stack
			   while(curr != null) {
				   pending.peek(curr.next());
				   return;
			   }
			// 5. If the stack isn't empty, then it should have all greater ancestors of top of stack and nothing else.
			    boolean flag = false;
			    if(!pending.isEmpty())
				    while(curr != null) {
					    pending.peek(curr);
					    flag = true;
				    }
			return true;
			++version;
		}
		
		private MyIterator(boolean ignored) {} // do not change, and do not use in your code
		
		// TODO: any helper method(s) (see homework description)
`		private MyIterator() {
			// TODO
			assert wellFormed();
			if(root != null)
				currNode = root;
		}
		
		@Override // required by Java
		public boolean hasNext() {
			return currNode != null || !pending.isEmpty(); // TODO
		}

		@Override // required by Java
		public HexTile next() {
			if(root == null)
				return root;
			currNode.terrain = root.terrain;
			
			while(!hasNext()) {
				while(currNode != null) {
					pending.push(currNode);
					currNode = currNode.left;
				}
				currNode = stack.pop();
				pending.push(currNode);
				currNode = currNode.right;
			}
			return pending; // TODO: find next entry and generate hex tile on demand
			++myVersion;
		}

		@Override // required for functionality
		public void remove() {
			throw new UnsupportedOperationException("no removal yet"); // TODO
			HexBoard.this.remove(current);
			++myVersion;
		}
	}
