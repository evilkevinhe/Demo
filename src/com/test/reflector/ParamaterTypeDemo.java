package com.test.reflector;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


import org.junit.Test;

public class ParamaterTypeDemo {
	
	@SuppressWarnings("rawtypes")
	@Test
	public void getParameterOfBaseClass(){
		
		Son son = new Son();
		//获取父类的类对象
//		Type type = son.getClass().getSuperclass();
		//获取带有泛型的父类类型
		Type type = son.getClass().getGenericSuperclass();
		
		System.out.println("获取带有泛型的父类类型: "+type);
		if (type instanceof ParameterizedType) {
			
			ParameterizedType pType = (ParameterizedType) type;
			Type[] types = pType.getActualTypeArguments();
			for (Type t : types) {
				//如果参数还是一个泛型参数
				if (t instanceof ParameterizedType) {
					//强转化为参数化类型
					ParameterizedType pType2 =(ParameterizedType) t;
					System.out.print("泛型参数的原始类型为："+((Class<?>) pType2.getRawType()).getSimpleName());
					//获得泛型参数中的参数类型数组
					Type[] types2 = (pType2.getActualTypeArguments());
					System.out.print(" 泛型参数中的参数为: ");
					for (int i = 0; i < types2.length; i++) {
						if (types2[i] instanceof WildcardType) {
							WildcardType wType = (WildcardType) types2[i];
							System.out.println(wType);
							System.out.println("表达式上边界："+wType.getUpperBounds()[wType.getUpperBounds().length-1]);
							System.out.println("表达式下边界："+wType.getLowerBounds().length);
//							if (types2.length>1&&i<types2.length-1) {
//								System.out.print(",");
//							}else
//								System.out.println();
						}else{
							System.out.print(((Class<?>) types2[i]).getSimpleName());
							if (types2.length>1&&i<types2.length-1) {
								System.out.print(",");
							}else	
							System.out.println();
						}
					}
//					for (Type t2 : types2) {
//						if (t2 instanceof WildcardType) {
//							System.out.print(" 泛型参数中的参数为: ");
//							WildcardType wType = (WildcardType) t2;
//							System.out.println(wType);
//	
//						}else{
//							System.out.print(" 泛型参数中的参数为: ");
//							System.out.print(((Class<?>) t2).getSimpleName()+" ");
//						}
//					}
				//如果参数是一个类型变量	
				}else if (t instanceof TypeVariable) {
					@SuppressWarnings("unchecked")
					TypeVariable<GenericDeclaration> typeVariable=(TypeVariable<GenericDeclaration>) t;
					System.out.println("类型变量的名称为："+typeVariable.getName());
					System.out.println("定义类型变量类的类对象："+typeVariable.getGenericDeclaration());
					System.out.println("类型变量的上边界为："+typeVariable.getBounds()[typeVariable.getBounds().length-1]);
				}else{
					//打印不带泛型的参数
					System.out.println("不带泛型的参数: "+((Class<?>)t).getSimpleName());
				}
			}
		}
		
	}
	
	

	
	@Test
	public void testParameterizedType() {
	        Field f = null;
	        try {
	            Field[] fields = ParameterizedTypeBean.class.getDeclaredFields();
	            // 打印出所有的 Field 的 TYpe 是否属于 ParameterizedType
	            for (int i = 0; i < fields.length; i++) {
	                f = fields[i];
	                System.out.println(f.getName()
	                        + " getGenericType() instanceof ParameterizedType "
	                        + (f.getGenericType() instanceof ParameterizedType));
	                System.out.println(f.getGenericType());
	                System.out.println(f.getType());
	            }
	            getParameterizedTypeMes("map" );
	            getParameterizedTypeMes("entry" );


	        } catch (NoSuchFieldException e) {
	            e.printStackTrace();
	        } catch (SecurityException e) {
	            e.printStackTrace();
	        }

	    }

	    private static void getParameterizedTypeMes(String fieldName) throws NoSuchFieldException {
	        Field f;
	        f = ParameterizedTypeBean.class.getDeclaredField(fieldName);
	        f.setAccessible(true);
	        System.out.println(f.getGenericType());
	        boolean b=f.getGenericType() instanceof ParameterizedType;
	        System.out.println(b);
	        if(b){
	            ParameterizedType pType = (ParameterizedType) f.getGenericType();
	            System.out.println(pType.getRawType());
	            for (Type type : pType.getActualTypeArguments()) {
	                System.out.println(type);
	            }
	            System.out.println(pType.getOwnerType()); 
	        }
	    }
	    
	    @Test
	    public void getParamaterOnMethod(){
	    	
	    	Method[] methods = Base.class.getDeclaredMethods();
	    	for (Method method : methods) {
	    		//获取包含有泛型的参数
				Type[] types = method.getGenericParameterTypes();
				for (Type t : types) {
					//如果参数是类型变量
					if (t instanceof TypeVariable) {
						System.out.println(((TypeVariable<?>) t).getName());
						System.out.println(((TypeVariable<?>) t).getGenericDeclaration());//类型变量声明的位置
					//如果参数是泛型数组即组件为ParamaterizeType(如：Map<K, V>)或者TypeVariable(如：T)	
					}else if (t instanceof GenericArrayType) {
						Type genericComponentType = ((GenericArrayType) t).getGenericComponentType();
						System.out.println(genericComponentType);
						if (genericComponentType instanceof ParameterizedType) {
							Type[] actualTypeArguments = ((ParameterizedType) genericComponentType).getActualTypeArguments();
							for (Type type : actualTypeArguments) {
								if (type instanceof TypeVariable) {
									System.out.println(((TypeVariable<?>) type).getName());
								}
							}
						}
					}else {
						
						System.out.println(((Class<?>) t).getSimpleName());
					}
				}
			}
	    }
}

//带参数的父类
class Base <T,ID extends Serializable,T2,T3,T4> {
	
	<T5, V, K> void  baseMethod(T5 t5,String str,Map<K, V>[] ay){
		
	}
}

//子类继承自基类
class Son<T3, K> extends Base<User,Double,ArrayList<? extends K>,T3,HashMap<String, User>>{
}


//泛型参数类
class User {

	private Integer id;
	private String name;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}

class ParameterizedTypeBean {
    // 下面的 field 的 Type 属于 ParameterizedType
    Map<String, Person> map;
    Set<String> set1;
    Class<?> clz;
    Holder<String> holder;
    List<String> list;
    // Map<String,Person> map 这个 ParameterizedType 的 getOwnerType() 为 null，
    // 而 Map.Entry<String, String> entry 的 getOwnerType() 为 Map 所属于的 Type。
    Map.Entry<String, String> entry;
    // 下面的 field 的 Type 不属于ParameterizedType
    String str;
    Integer i;
    Set set;
    List aList;

    static class Holder<V> {

    }
}


