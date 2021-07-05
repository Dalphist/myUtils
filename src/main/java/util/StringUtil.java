package util;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

/**
 * @author DJF
 * @version 0.1.0
 * @Description
 * @create 2021-05-14 11:07
 * @since 0.1.0
 **/
public class StringUtil {



    public static void main(String[] args) {
        // 运算式字符串直接计算
        Binding binding = new Binding();
//        binding.setVariable("F",2.5);
//        binding.setVariable("T",30);
//        binding.setVariable("A",100);
//        binding.setVariable("P0",100);
        binding.setVariable("I_0000011",85);
        binding.setVariable("language", "Groovy");
        GroovyShell shell = new GroovyShell(binding);
        String gs = "I * 100";

//        String gs = "(1+0.1 * (F/100) * T)*P0";
        String s = "P1=I_0000011/1365; return P1";
//
        Object F1 =shell.evaluate(s);
//        Object F2 =shell.evaluate("P1=P0*(0.055*0.20+1.0011)+A; return P1 ");
//
        System.out.println(F1);
//        System.out.println(F2);

    }
}
