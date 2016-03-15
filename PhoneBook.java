import data_structures.*;
import java.util.Iterator;
import java.io.*;

public class PhoneBook<K,V> {
	private int maxSize;
	private DictionaryADT<K, V> yellowPages;
	
	public PhoneBook(int maxSize) {
		this.maxSize = maxSize;
		yellowPages =	// new Hashtable<K,V>(maxSize);
				 		 new BinarySearchTree<K,V>();
						// new BalancedTree<K,V>();
	}
	
	public void load(String filename) {
		File file = new File(filename);
		
		try {
			InputStream ips = new FileInputStream(file);
			InputStreamReader ipsReader = new InputStreamReader(ips);
			BufferedReader buffReader = new BufferedReader(ipsReader);
			String txtLine;
			
			while((txtLine = buffReader.readLine()) != null) {
				String[] splitString = txtLine.split("=");
				PhoneNumber tmp = new PhoneNumber(splitString[0]);

				addEntry(tmp, splitString[1]);
			}
			
			buffReader.close();
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}
	
	public String numberLookup(PhoneNumber number) {
		return yellowPages.getValue((K) number).toString();
	}
	
	public PhoneNumber nameLookup(String name) {
		return (PhoneNumber) yellowPages.getKey((V) name);
	}
	
	public boolean addEntry(PhoneNumber number, String name) {
		if(yellowPages.add((K) number, (V) name))
			return true;
			
		return false;
	}
	
	public boolean deleteEntry(PhoneNumber number) {
		if(yellowPages.delete((K) number))
				return true;
		
		return false;
	}
	
	public void printAll() {
		Iterator<K> iter = yellowPages.keys();
		
		while(iter.hasNext()) {
			K tmpKey = iter.next();
			System.out.println(tmpKey.toString() + ": " + yellowPages.getValue(tmpKey));
		}
	}
	
	public void printByAreaCode(String code) {
		Iterator<K> iter = yellowPages.keys();
		
		while(iter.hasNext()) {
			PhoneNumber tmpKey = (PhoneNumber) iter.next();
			if(code.compareTo(tmpKey.areaCode) == 0) 
				System.out.println(tmpKey.toString() + ": " + yellowPages.getValue((K) tmpKey));
		}
			
	}
	
	public void printNames() {
		Iterator<V> iter = yellowPages.values();
		
		while(iter.hasNext()) {
			System.out.println(iter.next());
		}
	}
}
