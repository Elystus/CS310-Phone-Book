
import java.util.Iterator;
import data_structures.*;

public class PhoneNumber implements Comparable<PhoneNumber>{
	String areaCode, prefix, number;
	String phoneNumber;
	
	public PhoneNumber(String string) {
		verify(string);

		phoneNumber = string;
		
		String splitNum[] = string.split("-");
		areaCode = splitNum[0];
		prefix = splitNum[1];
		number = splitNum[2];
	}
	
	@Override
	public int compareTo(PhoneNumber n) {
		return phoneNumber.compareTo(n.phoneNumber);
	}

	public int hashCode() {
		return (int) (areaCode + prefix.hashCode() + number.hashCode()).hashCode() ;
	}
	
	private void verify(String n){
		if(!n.matches("(\\d{3})-(\\d{3})-(\\d{4})"))
			throw new IllegalArgumentException();
	}
	
	public String getAreaCode() {
		return areaCode;
	}
	
	public String getPrefix() {
		return prefix;
	}
	
	public String getNumber() {
		return number;
	}
	
	public String toString() {
		return phoneNumber;
	}
}
