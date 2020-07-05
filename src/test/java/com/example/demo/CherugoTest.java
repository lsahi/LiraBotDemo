package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.UnsupportedEncodingException;

import com.example.demo.plugin.CherugoPlugin;

import org.junit.jupiter.api.Test;

public class CherugoTest {

    @Test
    void WordToCheruTest() {
        CherugoPlugin p = new CherugoPlugin();

        String test = "测";
        String c = p.wordToCheru(test);
        System.out.println(c);
        String a = new String();
        try {
            a = p.cheruToWord(c);
            System.out.println(a);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        assertEquals(test, a);

    }

    @Test
    void StrToCheruTest() {
        String test = "切噜～♪";
        CherugoPlugin p = new CherugoPlugin();

        String c = p.strToCheru(test);
        System.out.println("Cheru is " + c);
        String d = new String();
        try {
            d = p.cheruToStr(c);
            System.out.println("str is " + d);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        assertEquals(test, d);
    }
    
}