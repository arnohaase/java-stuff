package _50_namedparams;


class Person {
	String vorname;
	String nachname;
	boolean verheiratet;
}



public class NamedParamsMain {
	public static void main(String[] args) {
		final Person person = new Person () {{
			vorname = "Arno";
			nachname = "Haase";
			verheiratet = true;
		}};
		
		System.out.println(person.vorname);
	}
}
