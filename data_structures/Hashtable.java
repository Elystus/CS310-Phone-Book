package data_structures;

import java.util.ConcurrentModificationException;
import java.util.Iterator;


public class Hashtable<K,V> implements DictionaryADT<K,V> {
	protected int tableSize;
	protected int curSize;
	protected LinearList<hashNode<K,V>> [] hashTable;
	protected long modCounter;
	
	private class hashNode<K, V> implements Comparable<hashNode<K,V>> {
		K key;
		V value;
		
		public hashNode(K k, V v) {
			key = k;
			value = v;
		}

		@Override
		public int compareTo(hashNode hn) {
			if(hn.key != null)
				return ((Comparable<K>)key).compareTo((K) hn.key);
			
			return ((Comparable<V>)value).compareTo((V) hn.value);
		}
	}
	
	// Constructor! :D
	public Hashtable(int size) {
		tableSize = size;
		modCounter = 0;
		clear();
	}
	
	@Override
	public boolean contains(K key) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean add(K key, V value) {
		hashNode<K, V> tmpNode = new hashNode<K, V>(key, value);
		if(hashTable[(key.hashCode() & 0X7FFFFFFF) % tableSize].addFirst(tmpNode)) {
			curSize++;
			modCounter++;
			return true;
		}
		
		return false;
	}

	@Override
	public boolean delete(K key) {
		if( hashTable[(key.hashCode() & 0X7FFFFFFF) % tableSize].remove(new hashNode<K, V>(key, null)) != null) {
			curSize = curSize - 1;
			modCounter++;
			return true;
		}
		
		return false;
	}

	@Override
	public V getValue(K key) {
		hashNode<K, V> tmpNode = hashTable[(key.hashCode() & 0X7FFFFFFF) % tableSize].find(new hashNode(key, null));
		return (V) ((tmpNode != null) ? tmpNode.value : null);
	}

	@Override
	public K getKey(V value) {
		ValueIteratorHelper<V> values = new ValueIteratorHelper<V>();
		while(values.hasNext()) {
			hashNode<K,V> tmpNode = values.iterNode();
			V val = values.next();
			
			if(value.toString().compareTo(val.toString()) == 0)
				return tmpNode.key;
		}
		
		return null;
	}

	@Override
	public int size() {
		return curSize;
	}

	@Override
	public boolean isFull() {
		return false;
	}

	@Override
	public boolean isEmpty() {
		return curSize == 0;
	}

	@Override
	public void clear() {
		curSize = 0;
		hashTable = new LinearList[tableSize];
		
		for(int i = 0 ; i < tableSize ; i++) {
			hashTable[i] = new LinearList<hashNode<K,V>>();
		}
	}
	
	@Override
	public Iterator<K> keys() {
		return new KeyIteratorHelper();
	}
	

	@Override
	public Iterator<V> values() {
		return new ValueIteratorHelper();
	}
	
	abstract class IteratorHelper<E> implements Iterator<E> {
		protected hashNode<K,V> [] nodes;
		protected int iterIndex;
		protected long modCheck;
		
		public IteratorHelper () {
			nodes = new hashNode[curSize];
			iterIndex = 0;
			int j = 0;
			modCheck = modCounter;
			
			for(int i = 0 ; i < tableSize ; i++) 
				for(hashNode<K,V> n : hashTable[i]) 
					nodes[j++] = n;
					
			sort();
		}
		
		public boolean hasNext() {
			if(modCheck != modCounter)
				throw new ConcurrentModificationException();
			
			return (iterIndex < curSize);
		}
		
		public abstract E next();
		
		public void remove() {
			throw new UnsupportedOperationException();
		}
		
		public void sort() {
			sortArray(0, nodes.length - 1);
		}
		
		private void sortArray(int left,int right) {
			if(right - left <= 0)
				return;
			
			hashNode pivot = nodes[right];
			int partition = getPartition(left, right, pivot);
			sortArray(left, partition - 1);
			sortArray(partition + 1, right);
		}
		
		private int getPartition(int left, int right, hashNode pivot) {
			int lPtr = left - 1;
			int rPtr = right;
			
			for(;;) {
				while(pivot.compareTo(nodes[++lPtr]) > 0);
				while(rPtr > 0 && (pivot.compareTo(nodes[--rPtr]) < 0));
				if(lPtr >= rPtr)
					break;
				else
					swap(lPtr, rPtr);
			}
			
			swap(lPtr, right);
			return lPtr;
		}
		
		private void swap(int one, int two) {
			hashNode temp = nodes[one];
			nodes[one] = nodes[two];
			nodes[two] = temp;
		}
	}
	
	class KeyIteratorHelper<K> extends IteratorHelper<K> {
		public KeyIteratorHelper() {
			super();
		}
		
		public K next() {
			return (K) nodes[iterIndex++].key;
		}
	}
	
	class ValueIteratorHelper<V> extends IteratorHelper<V> {
		public ValueIteratorHelper() {
			super();
		}
		
		public V next() {
			return (V) nodes[iterIndex++].value;
		}
		
		public hashNode<K,V> iterNode () {
			return (hashNode<K, V>) nodes[iterIndex];
		}
	}
}
