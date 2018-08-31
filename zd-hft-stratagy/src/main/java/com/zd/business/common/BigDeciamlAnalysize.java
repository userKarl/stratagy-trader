package com.zd.business.common;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

public class BigDeciamlAnalysize {
	
	private static final Logger logger=LoggerFactory.getLogger(BigDeciamlAnalysize.class);
	
	public static final String P1="m";
	
	public static final String P2="P2";
	
	public static final String P3="P3";
	
	public static final String P4="P4";
	/**
	 *提前将 符号的优先级定义好
	 */
	private static final Map<Character, Integer> basic = new HashMap<Character, Integer>();
	static {
	    basic.put('-', 1);
	    basic.put('+', 1);
	    basic.put('*', 2);
	    basic.put('/', 2);
	    basic.put('(', 0);//在运算中  （）的优先级最高，但是此处因程序中需要 故设置为0
	}
	
	
	private static List<String> symbolList=Lists.newArrayList("+","-","*","/","(");

	public static void test(){
	    String a = toSuffix("55.6+4*60/2+33+(4.7*5.6)/5",null);//传入 一串 算数公式  
	    System.out.println(a);
	    System.out.println(dealEquation(a));
	}
	
	public static void cacl(double unknownValue) {
		long l=System.nanoTime();
		
		String str = toSuffix("55.6+d*60/2+33+(4.7*5.6)/(m-1)",null);//传入 一串 算数公式
		str=str.replace("d", ""+unknownValue);
//		System.out.println(str);
		List<Term> termlist=Lists.newArrayList();
		
		List<String> numlist = new ArrayList<String>();  
		String[] split = str.split(",");
		for(String s:split) {
			if(symbolList.contains(s)) {
				String second = numlist.remove(numlist.size()-1);
	        	String first = numlist.remove(numlist.size()-1);
	        	Term term=new Term();
	        	term.setFirst(first);
	        	term.setSecond(second);
	        	term.setSymbol(s);
	        	if(P1.equals(first) || P1.equals(second)) {
	        		numlist.add(P1);
	        		termlist.add(term);
	        	}else {
	        		switch (s) {
	    	        case "+": double a = new BigDecimal(first).doubleValue()+ new BigDecimal(second).doubleValue(); numlist.add(String.valueOf(a));     break;
	    	        case "-": double b = new BigDecimal(first).doubleValue()- new BigDecimal(second).doubleValue(); numlist.add(String.valueOf(b));     break;
	    	        case "*": double c = new BigDecimal(first).doubleValue()* new BigDecimal(second).doubleValue(); numlist.add(String.valueOf(c));     break;
	    	        case "/": double d = new BigDecimal(first).doubleValue()/ new BigDecimal(second).doubleValue(); numlist.add(String.valueOf(d));       break;
	    	        default: numlist.add(s);     break;                                    //如果是数字  直接放进list中
	        		}
	        	}
			}else {
				numlist.add(s);
			}
			
		}
		double value=3;
		if(termlist.size()==0) {
			System.out.println(numlist.get(0));
		}else {
			while(true) {
				if(termlist.size()==0) {
					break;
				}
				Term e = termlist.remove(termlist.size()-1);
				String first=e.getFirst();
				String second=e.getSecond();
				String symbol=e.getSymbol();
				if("+".equals(symbol)) {
					if(P1.equals(first)) {
						value=new BigDecimal(value).subtract(new BigDecimal(second)).doubleValue();
					}else if(P1.equals(second)) {
						value=new BigDecimal(value).subtract(new BigDecimal(first)).doubleValue();
					}
				}else if("-".equals(symbol)) {
					if(P1.equals(first)) {
						value=new BigDecimal(value).add(new BigDecimal(second)).doubleValue();
					}else if(P1.equals(second)) {
						value=new BigDecimal(first).subtract(new BigDecimal(value)).doubleValue();
					}
				}else if("*".equals(symbol)) {
					if(P1.equals(first)) {
						value=new BigDecimal(value).divide(new BigDecimal(second),10,BigDecimal.ROUND_HALF_UP).doubleValue();
					}else if(P1.equals(second)) {
						value=new BigDecimal(value).divide(new BigDecimal(first),10,BigDecimal.ROUND_HALF_UP).doubleValue();
					}
				}else if("/".equals(symbol)) {
					if(P1.equals(first)) {
						value=new BigDecimal(value).multiply(new BigDecimal(second)).doubleValue();
					}else if(P1.equals(second)) {
						value=new BigDecimal(first).divide(new BigDecimal(value),10,BigDecimal.ROUND_HALF_UP).doubleValue();
					}
				}
				
			}
		}
		logger.info("得到结果：{}，耗时：{} ms",value,(System.nanoTime()-l)/1e6);
	}
	public static void main(String[] args) throws InterruptedException {
//		while(true) {
//			cacl(Math.random()*100);
//			Thread.sleep(1);
//		}
		test();
	}
	/**
	 * 将  中缀表达式  转化为  后缀表达式
	 */
	public static String toSuffix(String infix,List<String> numsList){
		if(numsList!=null) {
			if(numsList.size()>0) {
				infix.replace(P1, numsList.get(0));
			}
			if(numsList.size()>1) {
				infix.replace(P2, numsList.get(1));
			}
			if(numsList.size()>2) {
				infix.replace(P3, numsList.get(2));
			}
			if(numsList.size()>3) {
				infix.replace(P4, numsList.get(3));
			}
		}
		
		
	    List<String> queue = new ArrayList<String>();                                    //定义队列  用于存储 数字  以及最后的  后缀表达式
	    List<Character> stack = new ArrayList<Character>();                             //定义栈    用于存储  运算符  最后stack中会被 弹空
	    
	    char[] charArr = infix.trim().toCharArray();                                    //字符数组  用于拆分数字或符号
	    String standard = "*/+-()";                                                        //判定标准 将表达式中会出现的运算符写出来
	    char ch = '&';                                                                    //在循环中用来保存 字符数组的当前循环变量的  这里仅仅是初始化一个值  没有意义
	    int len = 0;                                                                    //用于记录字符长度 【例如100*2,则记录的len为3 到时候截取字符串的前三位就是数字】
	    for (int i = 0; i < charArr.length; i++) {                                        //开始迭代
	        
	        ch = charArr[i];                                                            //保存当前迭代变量
	        if(Character.isDigit(ch)) {                                                    //如果当前变量为 数字  
	            len++;    
	        }else if(Character.isLetter(ch)) {                                            //如果当前变量为  字母
	            len++;
	        }else if(ch == '.'){                                                        //如果当前变量为  .  会出现在小数里面
	            len++;
	        }else if(Character.isSpaceChar(ch)) {                                        //如果当前变量为 空格  支持表达式中有空格出现
	            if(len > 0) {                                                            //若为空格 代表 一段结束 ，就可以往队列中  存入了  【例如100 * 2  100后面有空格 就可以将空格之前的存入队列了】
	                queue.add(String.valueOf(Arrays.copyOfRange(charArr, i - len, i)));    //往 队列存入 截取的 字符串 
	                len = 0;                                                            //长度置空
	            }
	            continue;                                                                //如果空格出现，则一段结束  跳出本次循环
	        }else if(standard.indexOf(ch) != -1) {                                        //如果是上面标准中的 任意一个符号
	            if(len > 0) {                                                            //长度也有
	                queue.add(String.valueOf(Arrays.copyOfRange(charArr, i - len, i)));    //说明符号之前的可以截取下来做数字
	                len = 0;                                                            //长度置空
	            }
	            if(ch == '(') {                                                            //如果是左括号
	                stack.add(ch);                                                        //将左括号 放入栈中
	                continue;                                                            //跳出本次循环  继续找下一个位置
	            }
	            if (!stack.isEmpty()) {                                                    //如果栈不为empty
	                int size = stack.size() - 1;                                        //获取栈的大小-1  即代表栈最后一个元素的下标
	                boolean flag = false;                                                //设置标志位
	                while (size >= 0 && ch == ')' && stack.get(size) != '(') {            //若当前ch为右括号，则 栈里元素从栈顶一直弹出，直到弹出到 左括号
	                    queue.add(String.valueOf(stack.remove(size)));                    //注意此处条件：ch并未入栈，所以并未插入队列中；同样直到找到左括号的时候，循环结束了，所以左括号也不会放入队列中【也就是：后缀表达式中不会出现括号】
	                    size--;                                                            //size-- 保证下标永远在栈最后一个元素【栈中概念：指针永远指在栈顶元素】
	                    flag = true;                                                    //设置标志位为true  表明一直在取（）中的元素
	                }
	                while (size >= 0 && !flag && basic.get(stack.get(size)) >= basic.get(ch)) {    //若取得不是（）内的元素，并且当前栈顶元素的优先级>=对比元素 那就出栈插入队列
	                    queue.add(String.valueOf(stack.remove(size)));                    //同样  此处也是remove()方法，既能得到要获取的元素，也能将栈中元素移除掉
	                    size--;
	                }
	            }
	            if(ch != ')') {                                                            //若当前元素不是右括号  
	                stack.add(ch);                                                        //就要保证这个符号 入栈
	            } else {                                                                //否则就要出栈 栈内符号
	                stack.remove(stack.size() - 1);
	            }
	        }
	        if(i == charArr.length - 1) {                                                //如果已经走到了  中缀表达式的最后一位
	            if(len > 0) {                                                            //如果len>0  就截取数字
	                queue.add(String.valueOf(Arrays.copyOfRange(charArr, i - len+1, i+1)));
	            }    
	            int size = stack.size() - 1;                                            //size表示栈内最后一个元素下标
	            while (size >= 0) {                                                        //一直将栈内  符号全部出栈 并且加入队列中  【最终的后缀表达式是存放在队列中的，而栈内最后会被弹空】
	                queue.add(String.valueOf(stack.remove(size)));
	                size--;
	            }
	        }
	        
	    }
	    return queue.stream().collect(Collectors.joining(","));                            //将队列中元素以,分割 返回字符串
	}


	/**
	 * 将 后缀表达式 进行  运算 计算出结果
	 * @param equation
	 * @return
	 */
	public static String dealEquation(String equation){
	    String [] arr = equation.split(",");                                    //根据, 拆分字符串
	    List<String> list = new ArrayList<String>();                            //用于计算时  存储运算过程的集合【例如list中当前放置  100   20  5  /  则取出20/5 最终将结果4存入list   此时list中结果为  100  4 】
	    
	    
	    for (int i = 0; i < arr.length; i++) {                                    //此处就是上面说的运算过程， 因为list.remove的缘故，所以取出最后一个数个最后两个数  都是size-2
	        int size = list.size();
	        switch (arr[i]) {
	        case "+": double a = Double.parseDouble(list.remove(size-2))+ Double.parseDouble(list.remove(size-2)); list.add(String.valueOf(a));     break;
	        case "-": double b = Double.parseDouble(list.remove(size-2))- Double.parseDouble(list.remove(size-2)); list.add(String.valueOf(b));     break;
	        case "*": double c = Double.parseDouble(list.remove(size-2))* Double.parseDouble(list.remove(size-2)); list.add(String.valueOf(c));     break;
	        case "/": double d = Double.parseDouble(list.remove(size-2))/ Double.parseDouble(list.remove(size-2)); list.add(String.valueOf(d));       break;
	        default: list.add(arr[i]);     break;                                    //如果是数字  直接放进list中
	        }
	    }
	    
	    return list.size() == 1 ? list.get(0) : "运算失败" ;                    //最终list中仅有一个结果，否则就是算错了
	}
	
	
	public static List<String> change(String equation) {
		 String [] arr = equation.split(",");                                    //根据, 拆分字符串
		 List<String> list = new ArrayList<String>();                            //用于计算时  存储运算过程的集合【例如list中当前放置  100   20  5  /  则取出20/5 最终将结果4存入list   此时list中结果为  100  4 】
		 for (int i = 0; i < arr.length; i++) {
			 int size = list.size();
			 switch (arr[i]) {
		        case "+": 
		        	String s1=list.remove(size-2);
		        	String s2=list.remove(size-2);
		        	if(!s1.contains(P1) && !s1.contains("n") && !s2.contains(P1) && !s2.contains("n")) {
		        		double a = Double.parseDouble(s1)+ Double.parseDouble(s2); list.add(String.valueOf(a));    
		        	}else {
		        		list.add(""+s1+"+"+s2);
		        	}
		        	break;
		        case "-": 
		        	String s3=list.remove(size-2);
		        	String s4=list.remove(size-2);
		        	if(!s3.contains(P1) && !s3.contains("n") && !s4.contains(P1) && !s4.contains("n")) {
		        		double a = Double.parseDouble(s3)+ Double.parseDouble(s4); list.add(String.valueOf(a));    
		        	}else {
		        		list.add(""+s3+"-"+s4);
		        	}
		        	break;
		        case "*": 
		        	String s5=list.remove(size-2);
		        	String s6=list.remove(size-2);
		        	if(!s5.contains(P1) && !s5.contains("n") && !s6.contains(P1) && !s6.contains("n")) {
		        		double a = Double.parseDouble(s5)+ Double.parseDouble(s6); list.add(String.valueOf(a));    
		        	}else {
		        		list.add(""+s5+"*"+s6);
		        	}
		        	break;
		        case "/": 
		        	String s7=list.remove(size-2);
		        	String s8=list.remove(size-2);
		        	if(!s7.contains(P1) && !s7.contains("n") && !s8.contains(P1) && !s8.contains("n")) {
		        		double a = Double.parseDouble(s7)+ Double.parseDouble(s8); list.add(String.valueOf(a));    
		        	}else {
		        		list.add(""+s7+"/"+s8);
		        	}
		        	break;
		        	
		        default: list.add(arr[i]);     break; 
			 }
		 }
		 
		return list;
	}
}
