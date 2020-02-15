import java.util.*;
import java.lang.*;
import java.io.*;

/* Name of the class has to be "Main" only if the class is public. */
public class Solved1
{
    public static boolean mainTst (int[][] args) throws java.lang.Exception
    {
        int a1 = args[0][0];
        int a2 = args[0][1];
        int a3 = args[0][2];
        int b1 = args[1][0];
        int b2 = args[1][1];
        int b3 = args[1][2];
        int c1 = args[2][0];
        int c2 = args[2][1];
        int c3 = args[2][2];
        boolean b = false;

        // a<b<c
        if (a1 <= b1 && a2 <= b2 && a3 <= b3 && b1 <= c1 && b2 <= c2 && b3 <= c3 && (a1 < b1 || a2 < b2 || a3 < b3) && (b1 < c1 || b2 < c2 || b3 < c3))
            b = true;

        // a< c<b
        if (a1 <= c1 && a2 <= c2 && a3 <= c3 && c1 <= b1 && c2 <= b2 && c3 <= b3 && (a1 < c1 || a2 < c2 || a3 < c3) && (c1 < b1 || c2 < b2 || c3 < b3))
            b = true;

        //bca
        if (b1 <= c1 && b2 <= c2 && b3 <= c3 && c1 <= a1 && c2 <= a2 && c3 <= a3 && (b1 < c1 || b2 < c2 || b3 < c3) && (c1 < a1 || c2 < a2 || c3 < a3))
            b = true;

        //bac
        if (b1 <= a1 && b2 <= a2 && b3 <= a3 && a1 <= c1 && a2 <= c2 && a3 <= c3 && (b1 < a1 || b2 < a2 || b3 < a3) && (a1 < c1 || a2 < c2 || a3 < c3))
            b = true;

        //cba
        if (c1 <= b1 && c2 <= b2 && c3 <= b3 && b1 <= a1 && b2 <= a2 && b3 <= a3 && (c1 < b1 || c2 < b2 || c3 < b3) && (b1 < a1 || b2 < a2 || b3 < a3))
            b = true;

        //cab
        if (c1 <= a1 && c2 <= a2 && c3 <= a3 && a1 <= b1 && a2 <= b2 && a3 <= b3 && (c1 < a1 || c2 < a2 || c3 < a3) && (a1 < b1 || a2 < b2 || a3 < b3))
            b = true;

//        System.out.println((b) ? "yes" : "no");

        return b;
    }
}