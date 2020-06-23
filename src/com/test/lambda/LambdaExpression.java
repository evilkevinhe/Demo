package com.test.lambda;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.junit.Test;

public class LambdaExpression {
	
	@Test
	public void test(){
		
		new Thread( () -> System.out.println("In Java8, Lambda expression rocks !!") ).start();
	}
	@Test
	public void test2(){
		
		// Java 8之后：
		List<String> features = Arrays.asList("Lambdas", "Default Method", "Stream API", "Date and Time API");
		features.forEach(n -> System.out.println(n));
		
		// 使用Java 8的方法引用更方便，方法引用由::双冒号操作符标示，
		// 看起来像C++的作用域解析运算符
		features.forEach(System.out::println);
	}
	@Test
	public void test3(){
		
		List<String> languages = Arrays.asList("Java", "Scala", "C++", "Haskell", "Lisp");
		 
	    System.out.println("Languages which starts with J :");
	    filter(languages, (str)->((String) str).startsWith("J"));
	 
	    System.out.println("Languages which ends with a ");
	    filter(languages, (str)->((String) str).endsWith("a"));
	 
	    System.out.println("Print all languages :");
	    filter(languages, (str)->true);
	 
	    System.out.println("Print no language : ");
	    filter(languages, (str)->false);
	 
	    System.out.println("Print language whose length greater than 4:");
	    filter(languages, (str)->((String) str).length() > 4);
	    
	    
	    Predicate<String> startsWithJ = (n) -> n.startsWith("J");
	    Predicate<String> fourLetterLong = (n) -> n.length() == 4;
	    languages.stream()
	    .filter(startsWithJ.and(fourLetterLong))
	    .forEach((name) -> System.out.print("languages, which starts with 'J' and four letter long is : " + name));
	}
	
	private void filter(List<String> names, Predicate<String> condition) {
		names.stream().filter((name) -> (condition.test(name))).forEach((name) -> {
	        System.out.println(name + " ");
	    });
		//filter中直接写condition可以不用写lambda表达式
//		names.stream().filter(condition).forEach((name) -> {
//			System.out.println(name + " ");
//		});
		
	}
	
	@Test
	public void test4(){
		// 为每个订单加上12%的税
		// 老方法：
		List<Integer> costBeforeTax = Arrays.asList(100, 200, 300, 400, 500);
		double total = 0;
		for (Integer cost : costBeforeTax) {
		    double price = cost + .12*cost;
		    total = total + price;
		}
		System.out.println("Total : " + total);
		 
		// 新方法：
//		List costBeforeTax = Arrays.asList(100, 200, 300, 400, 500);
		double bill = costBeforeTax.stream().map((cost) -> cost + .12*cost).reduce((sum, cost) -> sum + cost).get();
		System.out.println("Total : " + bill);
	}
	
	@Test
	public void test5(){
		// 将字符串换成大写并用逗号链接起来
		List<String> G7 = Arrays.asList("USA", "Japan", "France", "Germany", "Italy", "U.K.","Canada");
		String G7Countries = G7.stream().map(x -> x.toUpperCase()).collect(Collectors.joining(", "));
		System.out.println(G7Countries);
	}
	@Test
	public void test7(){
		List<String> strList=Arrays.asList("Java", "Scala", "C++", "Haskell", "Lisp","C");;
		// 创建一个字符串列表，每个字符串长度大于2
		List<String> filtered = strList.stream().filter(x -> x.length()> 2).collect(Collectors.toList());
		System.out.printf("Original List : %s, filtered list : %s %n", strList, filtered);

	}
	
	@Test
	public void test8(){
		Set<Integer> ccUserIdList=new HashSet<>();
		ccUserIdList.forEach(ccUserid->
		{	
			System.out.println("running into the lookup?");
			if (ccUserid==0) {
				System.out.println("ccUserid==0");
			}
		});
	}
	@Test
	public void test9(){
	    List<Object> list = new ArrayList<>(2);
	    list.add(1);
	    list.add(2);
	    list.add(3);
	    list.add(4);
	    list.add(5);
	    list.stream().forEach(t->{System.out.println(t);});
	}
}
