package com.test.reflector;


import static org.hamcrest.CoreMatchers.instanceOf;

import java.lang.reflect.Array;
import java.util.Arrays;

import org.junit.Test;

public class ReflectedArrayDemo {
	
	
	/*
	 * 通过反射生成一维数组
	 */
	@Test
	public void getArrayByReflector(){
		
//		Class<?> componentType=Integer.TYPE;
		Class<?> componentType=Integer.class;
//		System.out.println(int.class);
//		System.out.println(Integer.class);
		
		int length=3;
		
		//反射生成数组，参数1为数组元素类型，参数2位数组长度
		Object array = Array.newInstance(componentType,length);
		
		System.out.println("数组的元素类型为："+array.getClass().getComponentType().getName());
		//获取数组长度
		System.out.println("数组长度为："+Array.getLength(array));
		
		// 通过反射把索引位置为2的元素设为1  
		Array.set(array, 2, 1); 
		
		// 通过反射获取索引位置为2的元素  
		System.out.println("数组索引2的值为："+Array.get(array, 2));
		
		//用数组接收反射获得的数组
		if (array instanceof int[] ) {
			int[] ay= (int[]) array;
			System.out.println("接收数组长度为："+ay.length);
			for (int i = 0; i < ay.length; i++) {
				System.out.println("接收数组索引"+i+"的值为"+ay[i]);
			}
		}
		//用数组接收反射获得的数组
		if (array instanceof Integer[] ) {
			Integer[] ay= (Integer[]) array;
			System.out.println("接收数组长度为："+ay.length);
			for (int i = 0; i < ay.length; i++) {
				System.out.println("接收数组索引"+i+"的值为"+ay[i]);
			}
		}
//		复制元数组内容到目标数组，srcPos原数组开始拷贝的索引位置，destPos目标数组开始粘贴的索引位置，length拷贝元素的数量
//		System.arraycopy(src, srcPos, dest, destPos, length);
	}
	
	/*
	 * 通过反射生成多维数组
	 */
	@Test
	public void getMutilDemensionsArray(){
		
		Class<?> componentType=Integer.TYPE;
		
		int[] demensions = new int[]{1,2,3};
		
		//反射生成数组，参数1为数组元素类型，参数2为一个可变的整型参数，demensions.length即为数组维度,三维数组长度为1.二维长度为2,以此类推
		Object array = Array.newInstance(componentType,demensions);
		
		//获取三维数组长度
		System.out.println("三维数组长度为："+Array.getLength(array));
		System.out.println("三维数组中存储的元素类型为："+array.getClass().getComponentType().getSimpleName());
		
		//获取三维数组中保存的二维数组
		Object array2 = Array.get(array, 0);
//		new int[]
		Array.set(array2, 0, new int[4]);
		
		//获取二维数组长度
		System.out.println("二维数组长度为："+Array.getLength(array2));
		System.out.println("二维数组中存储的元素类型为："+array2.getClass().getComponentType().getSimpleName());
		
		//获取二维数组中保存的第1个一维数组
		Object array3 = Array.get(array2, 0);
		
		//获取一维数组长度
		System.out.println("二维数组中保存的第1个一维数组长度为："+Array.getLength(array3));
		System.out.println("一维数组中存储的元素类型为："+array3.getClass().getComponentType().getSimpleName());
		
		
		//将反射得到的多维数组直接转化为多维数组
		int[][][] arrayCast = (int[][][])array;
		for (int[][] is : arrayCast) {
			for (int[] is2 : is) {
				for (int i : is2) {
					System.out.print(i+" ");
					System.out.println("打印完一个一维数组");
				}
				System.out.println("打印完一个二维数组");
			}
			System.out.println("打印完一个三维数组");
			
		}
		
		System.out.println(Arrays.deepToString(arrayCast));
	}
	
	@Test
	public void TT(){
		int i=0;
		int j=0;
		System.out.println(i++==0);
		System.out.println(++j==0);
		
	}
}


