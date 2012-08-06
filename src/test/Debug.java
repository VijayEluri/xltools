package test;

import java.util.ArrayList;


public class Debug
{
    public static void println(String s)
    {
        System.out.println(s);
    }

    public static void println(String s, String b)
    {
        System.out.println(b + " : " + s);
    }
    
    public static void print(String s){
    	System.out.print(s);
    }
    
    public static void err(String s){
    	System.err.print(s);
    }
    public static void errl(String s){
    	System.err.println(s);
    }
}