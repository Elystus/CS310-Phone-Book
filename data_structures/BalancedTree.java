/*
 * Author: Andrew Black (RedID: 816520439)
 * Class Account: masc0792
 * Last Edited: 05/07/2015
 */
package data_structures;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeMap;


public class BalancedTree<K,V> implements DictionaryADT<K,V> {

    TreeMap<K, V> mapTree;
    
    public BalancedTree()
    {
        mapTree= new TreeMap<K, V>();
    }

    public boolean contains(K key) {
       return mapTree.containsKey(key);
    }

    public boolean add(K key, V value) {
        mapTree.put(key, value);
        return true;
    }

    public boolean delete(K key) {
        V status=mapTree.remove(key);
        
         return status != null;
    }

    public V getValue(K key) {
        return mapTree.get(key);
    }

    public K getKey(V value) {
        for (Entry<K, V> entry : mapTree.entrySet()) 
            if (value.equals(entry.getValue())) 
                return entry.getKey();
                
        return null;
    }

    public int size() {
        return mapTree.size();
    }

    public boolean isFull() {
        return false;
    }

    public boolean isEmpty() {
        return (this.size()<1);
    }

    public void clear() {
        mapTree.clear();
    }

    public Iterator<K> keys() {
       return mapTree.keySet().iterator();
    }

    public Iterator<V> values() {
        return mapTree.values().iterator();
    }
}


