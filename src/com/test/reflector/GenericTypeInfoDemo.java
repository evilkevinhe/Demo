package com.test.reflector;


import java.io.Closeable;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.hamcrest.core.IsInstanceOf;
import org.junit.Test;
import org.junit.runners.Parameterized;


public class GenericTypeInfoDemo {
	
	private final static String DELIMITER="++++++++++++++++++++++++++++++++++++++";
	@Test
	public void resolveGenericReturnTypeOnMethod() {
		Method[] declaredMethods = Base.class.getDeclaredMethods();
		Arrays.stream(declaredMethods).forEach(__->{
			System.out.println(__);
			System.out.println(__.getDeclaringClass());
			System.out.println(__.getName());
			System.out.println(__.getGenericReturnType());
			System.out.println(__.getReturnType());//返回值是通配符直接获取返回类型是Object，范围太多
			});
		Method[] methods = Son.class.getMethods();
		Arrays.stream(methods).filter(__->"baseMethod".equals(__.getName())).forEach(__->{
			System.out.println(__);
			System.out.println(__.getDeclaringClass());
			System.out.println(__.getName());
			System.out.println(__.getGenericReturnType());
			System.out.println(__.getReturnType());
		});
		
	}
	@Test
	public void getTypeParametersAndActualTypeArgumentsFromGenericSuperclass() {
		Type type = Son.class.getGenericSuperclass();
		TypeVariable<?>[] typeParameters = Son.class.getTypeParameters();
		ParameterizedType parentAsType= (ParameterizedType) type;
		Class<?> parentAsClass = (Class<?>) parentAsType.getRawType();
		Type[] typeArgus = parentAsType.getActualTypeArguments();
		TypeVariable<?>[] typeParams = parentAsClass.getTypeParameters();
		
		IntStream.rangeClosed(0,typeArgus.length-1).forEach(__->{
			System.out.println(typeParams[__]+"="+typeArgus[__]);
		});
		System.out.println(Arrays.toString(typeArgus));
		System.out.println(Arrays.toString(typeParams));
		System.out.println(Arrays.toString(typeParameters));
		System.out.println(Son.class.getTypeName());
	}
	@Test
	public void resolveGenericTypeOnSuperclass(){
		
		//获取父类的类型
//		Type type = Son.class.getSuperclass();
		//获取带有泛型的父类类型
		Type type = Son.class.getGenericSuperclass();
		TypeVariable<?>[] typeParameters = Base.class.getTypeParameters();
		System.out.println("Base类声明的TypeVariable："+Arrays.toString(typeParameters));
		TypeVariable<?>[] typeVariables = Son.class.getTypeParameters();
		System.out.println("Son类声明的TypeVariable："+Arrays.toString(typeVariables));
		Arrays.asList(typeVariables).forEach(t->resolveGenericTypeInfo(t, message->{}, null));
		System.out.println("Son类带有泛型的父类类型: "+type);
		if (type instanceof ParameterizedType) {
			ParameterizedType pType = (ParameterizedType) type;
			Type[] types = pType.getActualTypeArguments();
			for (Type t : types) {
				resolveGenericTypeInfo(t, message->System.out.println(message),DELIMITER);
			}
		}
	}




	private void resolveGenericTypeInfo(Type t, Consumer<String> consumer,String message) {
		switch (TypeEunm.getType(t)) {
		case PARAMETERIZED_TYPE:
			consumer.accept(message);
			//强转化为参数化类型
			ParameterizedType pType2 =(ParameterizedType) t;
			printByType(t);
			System.out.print("参数化类型的原始类型为："+((Class<?>) pType2.getRawType()).getName());
			//获得泛型参数中的参数类型数组
			Type[] types = (pType2.getActualTypeArguments());
			System.out.println(" 实际类型参数列表: "+Arrays.toString(types));
			Arrays.asList(types).forEach(type->resolveGenericTypeInfo(type,msg->System.out.println(msg)," 实际类型参数为: "+TypeEunm.getType(type).name()));
			break;
		case TYPE_VARIABLE:	
			consumer.accept(message);
			TypeVariable<?> tv = (TypeVariable<?>) t;
			printByType(t);
			System.out.println("类型变量名："+tv.getName());
			System.out.println("声明类型变量的类："+tv.getGenericDeclaration());
			System.out.println("上边界数组："+Arrays.toString(tv.getBounds()));//TypeVariable最多只能做下边界，否则就是WildCard,形如<K extends Object,Runnable>
			break;
		case GENERIC_ARRAY_TYPE:
			consumer.accept(message);
			Type genericComponentType = ((GenericArrayType) t).getGenericComponentType();
			printByType(t);
		    System.out.println("元素类型名称："+genericComponentType); //java.util.concurrent.Callable<java.lang.String>
		    resolveGenericTypeInfo(genericComponentType, msg->System.out.println(msg), "元素类型："+TypeEunm.getType(genericComponentType).name());
			break;
		case WILDCARD_TYPE:
			consumer.accept(message);
			WildcardType wType = (WildcardType) t;
			printByType(t);
			System.out.println("上边界："+Arrays.toString(wType.getUpperBounds()));
			System.out.println("下边界："+Arrays.toString(wType.getLowerBounds()));
			break;
		case OTHER:	
			consumer.accept(message);
			//打印不带泛型的参数
			printByType(t);
			break;
		default:
			break;
		}
	}




	private void printByType(Type t) {
		Object[] args=new Object[]{TypeEunm.getType(t).getDesc(),TypeEunm.getType(t).name(),t};
		System.out.println(String.format("%1$s【%2$s】：%3$s",args));//argument number用“index$”表示（从1开始），可以按照下标取需要格式化的实参，默认按照传入的顺序取实参
	}
	
	

	
	@Test
	public void resolveGenericTypeOnField() {
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
	            getParameterTypeByFieldName("map" );
	            getParameterTypeByFieldName("entry" );


	        } catch (NoSuchFieldException e) {
	            e.printStackTrace();
	        } catch (SecurityException e) {
	            e.printStackTrace();
	        }

	    }

	    private static void getParameterTypeByFieldName(String fieldName) throws NoSuchFieldException {
	        Field f;
	        f = ParameterizedTypeBean.class.getDeclaredField(fieldName);
	        f.setAccessible(true);
	        Type t = f.getGenericType();
	        System.out.println(t);
	        if(t instanceof ParameterizedType){
	            ParameterizedType pType = (ParameterizedType)t;
	            System.out.println(pType.getRawType());
	            for (Type type : pType.getActualTypeArguments()) {
	                System.out.println(type);
	            }
	            System.out.println(pType.getOwnerType()); 
	        }
	    }
	    
	    @Test
	    public void getGenericOnMethod(){
	    	
	    	Method[] methods = Base.class.getDeclaredMethods();
	    	for (Method method : methods) {
	    		//获取包含有泛型的参数
				Type[] types = method.getGenericParameterTypes();
//				System.out.println(Arrays.asList(types));//转化成ArrayList去打印
				System.out.println(Arrays.toString(types));
				//getParameterTypes()不包含参数中的泛型
				Class<?>[] parameterTypes = method.getParameterTypes();
				Arrays.asList(parameterTypes)
				.forEach(clz->
				{System.out.println(clz.getSimpleName() + "===="+clz.getName());});
				for (Type t : types) {
					resolveGenericTypeInfo(t, message->System.out.println(message),DELIMITER);
					/*//如果参数是类型变量
					if (t instanceof TypeVariable) {
						System.out.print(((TypeVariable<?>) t).getName());
						System.out.println("  类型变量"+t+"定义在"+((TypeVariable<?>) t).getGenericDeclaration());//类型变量声明的位置
					//如果参数是泛型数组即元素类型component为ParamaterizeType(如：Map<K, V>)或者TypeVariable(如：T)	
					}else if (t instanceof GenericArrayType) {
						Type genericComponentType = ((GenericArrayType) t).getGenericComponentType();
						Object[] args = new Object[]{t.getTypeName(),genericComponentType};
						System.out.println(String.format("泛型数组类型：{%1$s}，其中数组元素类型名：{%2$s}",args ));
						if (genericComponentType instanceof ParameterizedType) {
							System.out.println(" 数组元素类型为ParameterizedType");
							Type[] actualTypeArguments = ((ParameterizedType) genericComponentType).getActualTypeArguments();
							for (Type type : actualTypeArguments) {
								if (type instanceof TypeVariable) {
									System.out.println(((TypeVariable<?>) type).getTypeName());
								}
							}
						}
						if (genericComponentType instanceof TypeVariable) {
							System.out.println(" 数组元素类型为TypeVariable");
							System.out.print(((TypeVariable<?>) genericComponentType).getName());
						}
					}else if(t instanceof ParameterizedType) {
						System.out.println("参数化类型"+t.getTypeName());
						Type[] actualTypeArguments = ((ParameterizedType) t).getActualTypeArguments();
						for (Type type : actualTypeArguments) {
							System.out.println(type.getTypeName());
							if (type instanceof WildcardType) {//形如? extends,? super但是WildcardType并不属于Java-Type中的一钟
									Type[] lowerBounds = ((WildcardType) type).getLowerBounds();
									System.out.println(lowerBounds.length>0?"lowerBounds:"+lowerBounds[0].getTypeName():"没有下边界");
									Type[] upperBounds = ((WildcardType) type).getUpperBounds();
									System.out.println(upperBounds.length>0?"upperBounds:"+upperBounds[0].getTypeName():"没有上边界");
							}
						}
					}else{
						System.out.println(t.getTypeName());
					}*/
				}
			}
	    }
	    @Test
	    public void testParameterType() {
	    	User<Integer>.InnerUser<String>subUser = new SubUser<Integer>().new SubInnerUser<>();
//	    	SubUser<String>.SubInnerUser<String>subUser = new SubUser<String>().new SubInnerUser<>();
	    	Type type = subUser.getClass().getGenericSuperclass();
	    	ParameterizedType pType;
	    	if (type instanceof ParameterizedType) {
	    		 pType = (ParameterizedType) type;
	    		 System.out.println(pType);
			}
		}
	    @Test
	    public void getSimpleNameOfAnonymousClass() {
			Runnable runnable = new  Runnable() {
				public void run() {
					
				}
			};
			System.out.println(runnable.getClass().getSimpleName());//匿名类的simpleName为null
		}
	    
	    @Test
		public <E> void testOrdinalOfEachEnumConstant() {
			TypeEunm[] values = TypeEunm.values();
			for (TypeEunm typeEunm : values) {
				System.out.println(typeEunm.name()+"："+typeEunm.ordinal());
			}
			TypeVariable<Class<Base>>[] typeParameters = Base.class.getTypeParameters();
			System.out.println(Arrays.toString(typeParameters));
		}
}

//带参数的父类,方法中可以使用定义在类或者方法上定义的泛型
class Base <T,ID extends Serializable,T2,T3 extends CharSequence & Closeable,T4,T6,T7,T8 extends Set<?>> {
	
	public <T5, V, K> T baseMethod(int a,T5 t5,String str,LinkedHashMap<? extends K,? super User<Object>> map,Map<K, V>[] gMaps,K[][] arr){
		return null ;
	}
}

//子类继承自基类
class Son<E extends CharSequence & Closeable, K> extends Base<User<Serializable>,Double,ArrayList<? extends K>,E,HashMap<String, User<Object>>,Integer[],Callable<String>[],LinkedHashSet<E[][]>>{
	
	public <T5, V, L> User<Serializable> baseMethod(int a, T5 t5, String str, java.util.LinkedHashMap<? extends L,? super User<Object>> map, java.util.Map<L,V>[] gMaps, L[][] arr) {
		return null;
	};
}


//泛型参数类
class User<F> {

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
	class InnerUser<I>{
		
	}
}
class SubUser<S extends Serializable> extends User<S>{
	class SubInnerUser<SI extends Object> extends User<S>.InnerUser<SI>{
			
	}
}

class ParameterizedTypeBean<E>{
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
    Set<E> set;
    List aList;

    static class Holder<V> {

    }
}


