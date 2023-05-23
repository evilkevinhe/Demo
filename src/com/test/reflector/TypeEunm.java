package com.test.reflector;

import static org.hamcrest.CoreMatchers.either;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;


public enum  TypeEunm{
	//Map<K,V>
	PARAMETERIZED_TYPE("参数化类型",ParameterizedType.class),
	
	//Map<K,V>[]
	GENERIC_ARRAY_TYPE("泛型数组类型",GenericArrayType.class),
	
	//? extends K
	WILDCARD_TYPE("通配符类型",WildcardType.class),
	
	//T
	TYPE_VARIABLE("类型变量",TypeVariable.class),
	
	//class interface enum
	OTHER("不带泛型的参数",Class.class);
	
	private String desc;
	private Class<? extends Type> typeInterface;

	public Class<? extends Type> getTypeClass() {
		return typeInterface;
	}

	public String getDesc(){
		return desc;
	}
	
	private TypeEunm(String desc, Class<? extends Type> typeClass) {
		this.desc = desc;
		this.typeInterface = typeClass;
	}
	
	public static TypeEunm getType(Type t ) {
		TypeEunm[] values = TypeEunm.values();
//		for (TypeEunm e : values) {
//			if (e.getTypeClass().isAssignableFrom(t.getClass())) {
//				return e;
//			}
//		}
//		 return null;
		//functional style
		Optional<TypeEunm> matchedEum = Arrays.asList(values).stream().filter(e->e.getTypeClass().isAssignableFrom(t.getClass())).findFirst();
		return matchedEum.isPresent()?matchedEum.get():null;
		
	}
	
}
