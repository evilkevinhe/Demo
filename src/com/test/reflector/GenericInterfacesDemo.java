package com.test.reflector;

import java.io.Serializable;
import java.lang.reflect.Type;

import org.junit.Test;

public class GenericInterfacesDemo {
		
		@Test
	    public  void testGenericInterfaces() {
	        Grand child = new Child();
	        Type[] types = child.getClass().getGenericInterfaces();
	        Type[] interfaces = child.getClass().getInterfaces();
	        if (types != null) {
	            for (Type type : types) {
	                System.out.println(type.getTypeName());
	            }
	        }
	        for (Type type : interfaces) {
	        	System.out.println(type.getTypeName());
			}
	    }
	    @Test
	    public void testGenericSuperclass() {
	        Grand child = new Child();
	        Type type1 = child.getClass().getGenericSuperclass();
	        Class<?> class1 = child.getClass().getSuperclass();
	        System.out.println(type1.getTypeName());    // Super<java.lang.Integer, java.lang.Integer>
	        System.out.println(class1.getTypeName());   // Super
	 
	        Grand child2 = new Child2();
	        Type type2 = child2.getClass().getGenericSuperclass();
	        Class<?> class2 = child2.getClass().getSuperclass();
	        System.out.println(type2.getTypeName());    // test.Super<A, B>
	        System.out.println(class2.getTypeName());   // Super
	 
	        Grand child3 = new Child3();
	        Type type3 = child3.getClass().getGenericSuperclass();
	        Class<?> class3 = child3.getClass().getSuperclass();
	        System.out.println(type3.getTypeName());    // Super
	        System.out.println(class3.getTypeName());   // Super
	    }

	
	 
}
abstract class Grand implements Comparable<Grand>{}
abstract class Super<T,E> extends Grand implements Serializable{}
class Child extends Super<Integer,Integer> implements Cloneable {public int compareTo(Grand o) {return 0;}}
class Child2<A,B,C> extends Super<A,B>{public int compareTo(Grand o) {return 0;}}
class Child3 extends Super{public int compareTo(Grand o) {return 0;}}
	
