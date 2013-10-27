package serializable;



public class SerializableMain {
	public static void main(String[] args) throws Exception{
		final Person person = new Person ("Arno", "Haase");
//		final Person person = Person.SUPERUSER; 
		
		final Person cloned = (Person) GenericCloner.clone(person);
		
		System.out.println(cloned == person);
	}
}
