package ru.geekbrains;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

// можно было бы не писать отдельно, а всё сделать в ArrayCount, но задание, как я понял, требует именно так.
public class SimpleArrayCount
{
    protected int size;     // задаваемый при инициализации размер массива.
    float[] arr;            // массив.

    public SimpleArrayCount(int size)
    {
        this.size = size;
        arr = fullfillarray();
    }

    @Contract(pure = true)
    public float @NotNull [] fullfillarray ()
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

    public void countArray()
    {
        long a = System.currentTimeMillis();
        for(int i = 0; i < arr.length; i++)
        {
            arr[i] = (float)(arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
        }
        System.out.println("Время цикла расчёта  " + (System.currentTimeMillis() - a));
    }
}
