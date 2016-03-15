
public class p4Driver {
	public static void main(String [] args) {
		PhoneBook BP = new PhoneBook(new Integer(10000));
		
		BP.load("p4_data.txt");
		BP.addEntry(new PhoneNumber("473-321-3565"), "Black, Andrew");
		BP.printAll();
		System.out.println("Inserting new value");
		BP.addEntry(new PhoneNumber("434-132-5543"), "White, Drew");
		BP.printAll();
		System.out.println("Removing Black, Andrew");
		BP.deleteEntry(new PhoneNumber("473-321-3565"));
		BP.deleteEntry(BP.nameLookup("White, Drew"));
		BP.printAll();
		
	}
}
