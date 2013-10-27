package de.arnohaase.javastuff.javassist;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;

public class JavassistMain {
	public static void main(String[] args) throws Exception {
		final ClassPool classPool = ClassPool.getDefault();
		final CtClass ctClass = classPool.makeClass("demo.MyClass");
		ctClass.addInterface(classPool.getCtClass(Greeter.class.getName()));
		final CtMethod mtd = CtNewMethod.make("public void hello(String name) {System.out.println (\"Hallo, \" + name);}", ctClass);
		ctClass.addMethod(mtd);
		
		final Class<?> myClass = ctClass.toClass();
		System.out.println("class name: " + myClass.getName());
		final Greeter greeter = (Greeter) myClass.newInstance();
		
		greeter.hello("ihr alle");
		System.out.println(greeter.getClass().getClassLoader());
	}
}
