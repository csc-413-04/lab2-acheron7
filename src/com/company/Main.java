package com.company;

class MatrixThreads implements Runnable{
int start;
int [][]a;
int [][]b;
int [][]d;
int stop;
int answer[][];

public MatrixThreads(int[][]a , int[][]b, int[][]d, int _start, int _stop ){
this.a = a;
this.b = b;
this.d = d;
start = _start;
stop = _stop;

}
@Override
public void run(){
    int size = a.length;
    int answer[][] = new int[size][size];
    for (int i = 0; i < size; i++) {
        for (int j = 0; j < size; j++) {
            for (int k = 0; k < size; k++) {
                Main.answer[i][j] = Main.answer[i][j] + (Main.a[i][k] * Main.b[k][j]);
            }
        }
    }
}


}

public class Main {

    public static int getRand(int min, int max) {
        int x = (int) (Math.random() * ((max - min) + 1)) + min;
        return x;
    }

    public static int[][] multiplySerial(int[][] a, int[][] b) {
        int size = a.length;
        int answer[][] = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                for (int k = 0; k < size; k++) {
                    answer[i][j] = answer[i][j] + (a[i][k] * b[k][j]);
                }
            }
        }
        return answer;
    }

    public static int[][] multiplyParallel(int[][] a, int[][] b) {
        int size = a.length;
        int answer[][] = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                for (int k = 0; k < size; k++) {
                    answer[i][j] = answer[i][j] + (a[i][k] * b[k][j]);
                }
            }
        }
        return answer;
    }

    public static boolean checkAnswer(int[][] a, int[][] b) {
        int size = a.length;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (a[i][j] != b[i][j]) return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        int size = 1000;
        int a[][] = new int[size][size];
        int b[][] = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                a[i][j] = getRand(1, 1000);
                b[i][j] = getRand(1, 1000);
            }
        }
        long startTime = System.nanoTime();
        int c[][] = multiplySerial(a, b);
        long endTime = System.nanoTime();
        long serialTime = endTime - startTime;
        System.out.println("Serial Time " + serialTime + " ns");

        startTime = System.nanoTime();
        // filler, make either a new class that extends thread, or have this one extend thread
        // figure out how to split work up into at least 2 more threads
        int d[][] = new[size][size];
        MatrixThreads m1 = new MatrixThreads (a,b,d,_start:0,_stop:500);
        MatrixThreads m2 = new MatrixThreads (a,b,d,_start:500,_stop:1000);
        Thread t1 = new Thread(m1);
        Thread t2 = new Thread(m2);
        t1.start();
        t2.start();
        try{

            t1.join();
            t2.join(); 
        }catch(InterruptedException e){
            e.printStackTrace();
        }

        endTime = System.nanoTime();
        long parallelTime = endTime - startTime;
        System.out.println("Parallel Time " + parallelTime + " ns");
        long diff = (parallelTime - serialTime);
        long percent = diff * 100 / serialTime;
        System.out.printf("Percent change %.2f \n", (float) percent);
        if (checkAnswer(c, d)) {
            System.out.println("Valid Answer");
        } else {
            System.out.println("Invalid Answer");
        }
    }
}
