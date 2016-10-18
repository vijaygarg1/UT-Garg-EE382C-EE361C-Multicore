package basic;

import java.util.Arrays;

public class BitonicSorter 
{
    private int[] a;
    // sorting direction:
    private final static boolean ASCENDING=true, DESCENDING=false;

    public void sort(int[] a)
    {
        this.a=a;
        bitonicSort(0, a.length, ASCENDING);
    }

    private void bitonicSort(int lo, int n, boolean dir)
    {
        if (n>1)
        {
            int m=n/2;
            bitonicSort(lo, m, ASCENDING);
            bitonicSort(lo+m, m, DESCENDING);
            bitonicMerge(lo, n, dir);
        }
    }

    private void bitonicMerge(int lo, int n, boolean dir)
    {
        if (n>1)
        {
            int m=n/2;
            for (int i=lo; i<lo+m; i++)
                compare(i, i+m, dir);
            bitonicMerge(lo, m, dir);
            bitonicMerge(lo+m, m, dir);
        }
    }

    private void compare(int i, int j, boolean dir)
    {
        if (dir==(a[i]>a[j]))
            exchange(i, j);
    }

    private void exchange(int i, int j)
    {
        int t=a[i];
        a[i]=a[j];
        a[j]=t;
    }
    public static void main(String[] args) {
        final int n = 8;
     
        int[] b = new int[n];
        for(int i=0;i<n;i++) b[i] = (int)(Math.random() * 20);

        System.out.println(Arrays.toString(b));
        BitonicSorter sorter = new BitonicSorter();
        sorter.sort(b);
        System.out.println(Arrays.toString(b));

        
    }

}    