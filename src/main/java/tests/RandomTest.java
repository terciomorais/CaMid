package tests;

import java.util.Random;

public class RandomTest{

    public static void main(String []args){
        Random r = new Random();
        for(int i = 0; i < 11; i++)
            System.out.println(3 - r.nextInt(7));
    }
}