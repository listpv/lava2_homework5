package ru.geekbrains;

/*1. Необходимо написать два метода, которые делают следующее:
        1) Создают одномерный длинный массив, например:

static final int size = 10000000;
static final int h = size / 2;
        float[] arr = new float[size];

        2) Заполняют этот массив единицами;
        3) Засекают время выполнения: long a = System.currentTimeMillis();
        4) Проходят по всему массиву и для каждой ячейки считают новое значение по формуле:
        arr[i] = (float)(arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
        5) Проверяется время окончания метода System.currentTimeMillis();
        6) В консоль выводится время работы: System.out.println(System.currentTimeMillis() - a);
        Отличие первого метода от второго:
        Первый просто бежит по массиву и вычисляет значения.
        Второй разбивает массив на два массива, в двух потоках высчитывает новые значения и
        потом склеивает эти массивы обратно в один.
        Пример деления одного массива на два:

        System.arraycopy(arr, 0, a1, 0, h);
        System.arraycopy(arr, h, a2, 0, h);
        Пример обратной склейки:

        System.arraycopy(a1, 0, arr, 0, h);
        System.arraycopy(a2, 0, arr, h, h);
        Примечание:
        System.arraycopy() – копирует данные из одного массива в другой:
        System.arraycopy(массив-источник, откуда начинаем брать данные из массива-источника,
        массив-назначение, откуда начинаем записывать данные в массив-назначение, сколько ячеек копируем)
        По замерам времени:
        Для первого метода надо считать время только на цикл расчета:

        for (int i = 0; i < size; i++) {
        arr[i] = (float)(arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
        }

        Для второго метода замеряете время разбивки массива на 2, просчета каждого из двух массивов и склейки.*/


import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Main {

//    static final int size = 10000000;
//    static int quant = 5;
//    static final int h = size / quant;


    public static void main(String[] args)
    {
	// write your code here

        ArrayCount arrayCount = new ArrayCount(10000000, 7);
        arrayCount.countSomeArray1();
        System.out.println();
        SimpleArrayCount simpleArrayCount = new SimpleArrayCount(10000000);
        simpleArrayCount.countArray();
    }


    //  рабочие моменты, не обращайте внимания.
/*     @Contract(pure = true)
     public static float @NotNull [] fullfillarray ()
     {
         float[] arr = new float[size];
//         long a = System.currentTimeMillis();
         for(int i = 0; i < arr.length; i++)
         {
             arr[i] = 1;
         }
//         System.out.println(System.currentTimeMillis() - a);
         return arr;
     }

     public static void countArray(float @NotNull [] arr, String num)
     {
         long a = System.currentTimeMillis();
         for(int i = 0; i < arr.length; i++)
         {
             arr[i] = (float)(arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
         }
         System.out.println(num + "  " + (System.currentTimeMillis() - a));
     }

    public static void countArray()
    {
        float[] arr = fullfillarray();
        long a = System.currentTimeMillis();
        for(int i = 0; i < arr.length; i++)
        {
            arr[i] = (float)(arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
        }
        System.out.println(System.currentTimeMillis() - a);
    }

    public static void countSomeArray()
    {
        float[] arr = fullfillarray();
        long a = System.currentTimeMillis();
        float[] a1 = new float[h];
        float[] a2 = new float[h];
        System.arraycopy(arr, 0, a1, 0, h);
        System.arraycopy(arr, h, a2, 0, h);
        System.out.println(System.currentTimeMillis() - a);
        Thread t1 = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                countArray(a1, Thread.currentThread().getName());
            }

        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                countArray(a2, Thread.currentThread().getName());
            }
        });

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long b = System.currentTimeMillis();
        System.arraycopy(a1, 0, arr, 0, h);
        System.arraycopy(a2, 0, arr, h, h);
        System.out.println(System.currentTimeMillis() - b);
        System.out.println(System.currentTimeMillis() - a);

        System.out.println(arr[0] + " " + arr[arr.length - 1] + " " + arr.length);
    }

    public static void countSomeArray1()
    {
        float[] arr = fullfillarray();
        long a = System.currentTimeMillis();
        HashMap<Integer, float[]> mass = new HashMap<>();

        for(int i = 0; i < quant; i++)
        {
            float[] ar = new float[h];
            System.arraycopy(arr, 0, ar, 0, h);
            mass.put(i, ar);
        }

        //  остаток
        float[] add = new float[size % quant];
        if(size % quant != 0)
        {
            System.arraycopy(arr, (arr.length - size % quant), add, 0, size % quant);


        }
//

        System.out.println(System.currentTimeMillis() - a);


        Thread[] t = new Thread[mass.size()];
        int var = 0;

        Set<Map.Entry<Integer, float[]>> set = mass.entrySet();
        for (Map.Entry<Integer, float[]> me: set)
        {
            t[var] = new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    countArray(me.getValue(), Thread.currentThread().getName());
                }
            });
            var++;
        }

        // остаток
        Thread addT = null;
        if(size % quant != 0)
        {
            addT = new Thread(new Runnable() {
                @Override
                public void run()
                {
                    countArray(add, Thread.currentThread().getName());
                }
            });
        }
//

        for(int i = 0; i < t.length; i++)
        {
            t[i].start();
        }


        //  остаток
        if(size % quant != 0)
        {
            addT.start();
        }
//

        for(int i = 0; i < t.length; i++)
        {
            try {
                t[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // остаток.
        if(size % quant != 0)
        {
            try {
                addT.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //

        long b = System.currentTimeMillis();

        int step = 0;
        for (Map.Entry<Integer, float[]> me: set)
        {
            System.arraycopy(me.getValue(), 0, arr, step, h);
            step = step + h;
        }

        // остаток.
        if(size % quant != 0)
        {
            System.arraycopy(add, 0, arr, (arr.length - add.length), add.length);
        }
//

        System.out.println(System.currentTimeMillis() - b);
        System.out.println(System.currentTimeMillis() - a);

        System.out.println(arr[0] + " " + arr[arr.length - 1] + " " + arr.length);
    }*/
}
