package ru.geekbrains;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ArrayCount
{
    protected int size;     // задаваемый при инициализации размер массива.
    protected int quant;    // задаваемое при инициализации количество частей для деления массива.
    protected int h;        // size / quant.
    float[] arr;            // массив.

    public ArrayCount(int size, int quant)
    {
        this.size = size;
        this.quant = quant;
        h = size / quant;
        arr = fullfillarray();

    }

    @Contract(pure = true)
    public float @NotNull [] fullfillarray ()    // заполнение единицами.
    {
        arr = new float[size];
//         long a = System.currentTimeMillis();
        for(int i = 0; i < arr.length; i++)
        {
            arr[i] = 1;
        }
//         System.out.println(System.currentTimeMillis() - a);
        return arr;
    }

    public void countArray(float @NotNull [] arr, String num)   //  цикл расчёта и имя потока.
    {
        long a = System.currentTimeMillis();
        for(int i = 0; i < arr.length; i++)
        {
            arr[i] = (float)(arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
        }
        System.out.println(num + "  " + (System.currentTimeMillis() - a));
    }

    public void countSomeArray1()    // метод для разбивки массива и склейки.
    {
        System.out.println("Старт  " + arr[0] + " " + arr[arr.length - 1] + " " + arr.length); // исходный массив.
        // разбивка массива
        long a = System.currentTimeMillis();

        HashMap<Integer, float[]> mass = new HashMap<>();
        int srcPos = 0;
        for(int i = 0; i < quant; i++)
        {
            float[] ar = new float[h];
            System.arraycopy(arr, srcPos, ar, 0, h);
            mass.put(i, ar);
            srcPos = srcPos + h;
        }

        //  если при разбивке массива остаётся остаток.
        float[] add = new float[size % quant];
        if(size % quant != 0)
        {
            System.arraycopy(arr, (arr.length - size % quant), add, 0, size % quant);


        }
        //

        System.out.println("Время разбивки массива на части  " + (System.currentTimeMillis() - a));

        // создание потоков и подсчёт каждой части.
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

        // если при разбивке массива остаётся остаток.
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


        //  если при разбивке массива остаётся остаток.
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

        // если при разбивке массива остаётся остаток.
        if(size % quant != 0)
        {
            try {
                addT.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //

        // склейка.
        long b = System.currentTimeMillis();

        int step = 0;
        for (Map.Entry<Integer, float[]> me: set)
        {
            System.arraycopy(me.getValue(), 0, arr, step, h);
            step = step + h;
        }

        // если при разбивке массива остаётся остаток.
        if(size % quant != 0)
        {
            System.arraycopy(add, 0, arr, (arr.length - add.length), add.length);
        }
//

        System.out.println("Время склейки массива  " + (System.currentTimeMillis() - b));
        System.out.println("Общее время  " + (System.currentTimeMillis() - a));

        System.out.println("Итог  " + arr[0] + " " + arr[arr.length - 1] + " " + arr.length);  // результат. Для самоконтроля.
    }
}
