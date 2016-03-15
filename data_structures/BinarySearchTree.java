package data_structures;

import java.util.ConcurrentModificationException;
import java.util.Iterator;


public class BinarySearchTree<K, V> implements DictionaryADT<K, V>  {
	public int treeSize;
	public treeNode<K, V> parentNode;
	public int modCounter;
	
	private class treeNode<K, V> implements Comparable<treeNode<K,V>> {
		public K key;
		public V value;
		public treeNode<K, V> leftChild;
		public treeNode<K, V> rightChild;
		
		public treeNode (K k, V v) {
			key = k;
			value = v;
		}
		
		@Override
		public int compareTo(treeNode<K, V> o) {
			if(o.key != null)
				return ((Comparable<K>)key).compareTo((K) o.key);
			
			return ((Comparable<V>)value).compareTo((V) o.value);
		}
	}
	
	public BinarySearchTree () {
		modCounter = 0;
		clear();
	}

	@Override
	public boolean contains(K key) {
		return (getValue(key) != null) ? true : false;
	}

	@Override
	public boolean add(K key, V value) {
		treeNode<K,V> newNode = new treeNode<K,V>(key, value);
		
		if(parentNode == null) {
			parentNode = newNode;
			treeSize++;
			modCounter++;
			return true;
		}
		
		treeNode<K,V> tmpNode = parentNode;
		while(tmpNode != null) {
			if(tmpNode.compareTo(newNode) > 0) {
				if(tmpNode.leftChild == null) {
					tmpNode.leftChild = newNode;
					treeSize++;
					break;
				}
				
				tmpNode = tmpNode.leftChild;
				continue;
			}
			
			if(tmpNode.compareTo(newNode) < 0) {
				if(tmpNode.rightChild == null) {
					tmpNode.rightChild = newNode;
					treeSize++;
					break;
				}
				
				tmpNode = tmpNode.rightChild;
				continue;
			}
		}

		modCounter++;
		return true;
	}

	@Override
	public boolean delete(K key) {
		treeNode<K,V> delNode = find(key, null);
		
		if(delNode == null)
			return false;
		
		
		if(delNode.leftChild == null && delNode.rightChild == null) {
			treeNode<K,V> pNode = getParent(delNode.key);
			
			if(pNode == null) {
				parentNode = null;
				treeSize--;
				modCounter++;
				return true;
			}
			
			if(pNode.leftChild != null && delNode.compareTo(pNode.leftChild) == 0)
				pNode.leftChild = null;
			else
				pNode.rightChild = null;
			
			treeSize--;
			modCounter++;
			return true;
		}
		
		else if(delNode.leftChild != null && delNode.rightChild != null) {
			treeNode<K,V> s = getSuccessor(delNode);
			treeNode<K,V> pNode = getParent(delNode.key);
			
			if(pNode == null) {
				s.leftChild = parentNode.leftChild;
				s.rightChild = parentNode.rightChild;
				parentNode = s;

				treeSize--;
				modCounter++;
				return true;
			}
				
			s.rightChild = delNode.rightChild;
			s.leftChild = delNode.leftChild;
			if(pNode.leftChild != null && delNode.compareTo(pNode.leftChild) == 0)
				pNode.leftChild = s;
			else
				pNode.rightChild = s;
			
			treeSize--;
			modCounter++;
			return true;
		}
			
		else if( delNode.leftChild != null || delNode.rightChild != null ) {
			treeNode<K,V> pNode = getParent(delNode.key);
			// ParentNode
			if(pNode == null) {
				if(parentNode.leftChild != null)
					parentNode = parentNode.leftChild;
				else
					parentNode = parentNode.rightChild;
				
				treeSize--;
				modCounter++;
				return true;
			}
				
			if(delNode.leftChild != null) {
				if(pNode.leftChild != null && delNode.compareTo(pNode.leftChild) == 0) 
					pNode.leftChild = delNode.leftChild;
				else
					pNode.rightChild = delNode.leftChild;
					
			} else {
				if(pNode.leftChild != null && delNode.compareTo(pNode.leftChild) == 0) 
					pNode.leftChild = delNode.rightChild;
				else
					pNode.rightChild = delNode.rightChild;
			}

			modCounter++;
			treeSize--;
			return true;
		}
		
		return false;
	}
	
	private treeNode<K,V> getSuccessor(treeNode<K,V> startNode) {
		// No need for successor
		if(startNode.rightChild == null || startNode.leftChild == null)
			return null;
		
		
		treeNode<K,V> minNode = startNode.rightChild;
		
		if(minNode.leftChild == null) {
			if(minNode.rightChild != null) 
				startNode.rightChild = minNode.rightChild;
			else
				startNode.rightChild = null;
			
			return minNode;
		}
		
		while(minNode.leftChild != null)
			minNode = minNode.leftChild;
		
		treeNode<K,V> pNode = getParent(minNode.key);
		
		// If Successor has right node
		if(minNode.rightChild != null)
			pNode.leftChild = minNode.rightChild;
		else if(pNode.compareTo(parentNode) != 0)
			pNode.leftChild = null;
			
		return minNode;
	}

	private treeNode<K,V> find (K key, V value) {
		treeNode<K,V> searchNode = parentNode;
		treeNode<K,V> compNode = new treeNode(key, value);
		
		if(key != null) {
			while (searchNode != null && compNode.compareTo(searchNode) != 0) {
				if(compNode.compareTo(searchNode) > 0)
					searchNode = searchNode.rightChild;
				else
					searchNode = searchNode.leftChild;
			}

			return searchNode;
		} else {
			Iterator<K> keys = new keyIteratorHelper<K>();
			while(keys.hasNext()) {
				treeNode<K,V> tmpNode = find(keys.next(), null);
				if(tmpNode.compareTo(compNode) == 0)
					return tmpNode;
			}
			
			return null;
		}
	}
	
	
	private treeNode<K,V> getParent (K key) {
		treeNode<K,V> searchNode = parentNode;
		treeNode<K,V> compNode = new treeNode(key, null);
		treeNode<K,V> parent = null;
		
		while(searchNode != null && compNode.compareTo(searchNode) != 0) {
			if(compNode.compareTo(searchNode) > 0) {
				parent = searchNode;
				searchNode = searchNode.rightChild;
			} else {
				parent = searchNode;
				searchNode = searchNode.leftChild;
			}
		}
		
		return parent;
	}
	
	@Override
	public V getValue(K key) {
		treeNode<K,V> tmpNode = find(key, null);
		return (tmpNode != null) ? tmpNode.value : null;
	}
	
	@Override
	public K getKey(V value) {
		treeNode<K,V> tmpNode = find(null, value);
		return (tmpNode != null) ? tmpNode.key : null;
	}

	@Override
	public int size() {
		return treeSize;
	}

	@Override
	public boolean isFull() {
		return false;
	}

	@Override
	public boolean isEmpty() {
		return (treeSize == 0);
	}

	@Override
	public void clear() {
		treeSize = 0;
		parentNode = null;
	}

	@Override
	public Iterator<K> keys() {
		return new keyIteratorHelper();
	}

	@Override
	public Iterator<V> values() {
		return new valueIteratorHelper();
	}
	
	public class keyIteratorHelper<K> extends IteratorHelper<K> {

		@Override
		public K next() {
			if(iterMod != modCounter)
				throw new ConcurrentModificationException();
		
	    	treeNode<K,V> r = (treeNode<K,V>) iterNode;
	    	if(iterNode.rightChild != null){
	    	 	iterNode = iterNode.rightChild;
	    	 	while (iterNode.leftChild != null)
    	 		iterNode = iterNode.leftChild;
	    	 	return r.key;
	     	} else { 
	     		while(true) {
	     			if(getParent(iterNode.key) == null) {
     					iterNode = null;
		     				return r.key;
		     			}
		     			if(getParent(iterNode.key).leftChild == iterNode){
		     				iterNode = getParent(iterNode.key);
		     				return r.key;
		     			}
	     			iterNode = getParent(iterNode.key);
	     		}	
	     	}
		}
	}
	
	public class valueIteratorHelper<V> extends IteratorHelper<V> {

		@Override
		public V next() {
		if(iterMod != modCounter)
			throw new ConcurrentModificationException();
	    	treeNode<K,V> r = (treeNode<K,V>) iterNode;
		     if(iterNode.rightChild != null){
		    	 iterNode = iterNode.rightChild;
		    	 while (iterNode.leftChild != null)
		    		 iterNode = iterNode.leftChild;
		    	 return r.value;
		     	} else { 
		     		while(true) {
		     			if(getParent(iterNode.key) == null){
		     				iterNode = null;
		     				return r.value;
		     			}
		     			if(getParent(iterNode.key).leftChild == iterNode){
		     				iterNode = getParent(iterNode.key);
		     				return r.value;
		     			}
		     			iterNode = getParent(iterNode.key);
		     		}	
		     	}
			}
	}
	
	abstract class IteratorHelper<E> implements Iterator<E> {
		protected treeNode<K,V> iterNode; 
		protected int iterMod;
		
		public IteratorHelper () {
			iterNode = parentNode;
			iterMod = modCounter;
			
			if(iterNode == null)
				return;
			while(iterNode.leftChild != null)
				iterNode = iterNode.leftChild;
		}
		
		public boolean hasNext () {
			return iterNode != null;
		}
		
		public abstract E next();
	}
	
}
