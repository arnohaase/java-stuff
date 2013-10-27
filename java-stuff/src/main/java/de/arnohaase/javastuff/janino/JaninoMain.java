package de.arnohaase.javastuff.janino;

import org.codehaus.janino.ExpressionEvaluator;

public class JaninoMain {
	public static void main(String[] args) throws Exception {
		final ExpressionEvaluator ee1 = new ExpressionEvaluator();
		ee1.cook("2+3");
		System.out.println(ee1.evaluate(null));
		
		final ExpressionEvaluator ee2 = new ExpressionEvaluator("a<b ? 2*a : 8-b", int.class, new String[] {"a", "b"}, new Class[] {int.class, int.class});
		System.out.println(ee2.evaluate(new Object[] {1, 2}));
		System.out.println(ee2.evaluate(new Object[] {2, 1}));

		final ExpressionEvaluator ee3 = new ExpressionEvaluator("a<b ? (a + \" ist kleiner\") : (b + \" ist kleiner\")", String.class, new String[] {"a", "b"}, new Class[] {int.class, int.class});
		System.out.println(ee3.evaluate(new Object[] {1, 5}));
		System.out.println(ee3.evaluate(new Object[] {4, 2}));
	}

}
