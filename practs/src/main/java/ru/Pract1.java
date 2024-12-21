package ru;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.*;

public class Pract1 {

    private final int thNum = Runtime.getRuntime().availableProcessors() - 2;

    public Pract1() throws ExecutionException, InterruptedException {
        fun3();
    }

    private void fun1() throws ExecutionException, InterruptedException {
        int[] array = new int[10000];
        for (int i = 0; i < 10000; i++) {
            array[i] = (int) Math.round((Math.random() * 20000) - 10000);
        }
        Instant start = Instant.now();
        //int max = fun1_forkjoin(array,thNum);
        //int max = fun1_thread(array, thNum);
        //int max = fun1_posl(array);
        Instant finish = Instant.now();
        long elapsed = Duration.between(start, finish).toMillis();
        long _total = Runtime.getRuntime().totalMemory();
        long _free = Runtime.getRuntime().freeMemory();
        long _usedMem = _total - _free;
        //System.out.println(max);
        System.out.println("Прошло времени, мс: " + elapsed + " Использовано памяти: " + _usedMem + " bytes");
    }

    public static int fun1_posl(int[] array) {
        int max = array[0];
        for (int num : array) {
            if(max < num) {
                max = num;
                try {
                    //System.out.println("Thread: " + Thread.currentThread().getName());
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return max;
    }

    private int fun1_thread(int[] arrayList, int threads) throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        Future<Integer>[] rez = new Future[threads];
        int[] rezi = new int[threads];
        int ch = arrayList.length / threads, i = 0;
        for(; i < threads; i++)
        {
            int i1 = i * ch, i2 = (i + 1) * ch - 1;
            /*Callable<Integer> task = () ->
            {
                System.out.println("Thread 1: " + Thread.currentThread().getName());
                return fun1_posl(Arrays.copyOfRange(arrayList, i1, i2));
            };*/
            rez[i] = executor.submit(() -> fun1_posl(Arrays.copyOfRange(arrayList, i1, i2)));
        }
        i = 0;
        for (Future<Integer> fut : rez)
        {
            rezi[i] = fut.get();
            i++;
        }
        executor.shutdown();
        return fun1_posl(rezi);
    }

    private int fun1_forkjoin(int[] arrayList, int div) throws ExecutionException, InterruptedException {
        ForkJoinPool forkJoinPool = new ForkJoinPool(div);
        ForkJoinTask<Integer>[] rez = new ForkJoinTask[div];
        int[] rezi = new int[div];
        int ch = arrayList.length / div, i = 0;
        for(; i < div; i++)
        {
            int i1 = i * ch, i2 = (i + 1) * ch - 1;
            rez[i] = forkJoinPool.submit(new Worke(Arrays.copyOfRange(arrayList, i1, i2)));
        }
        i = 0;
        for (ForkJoinTask<Integer> fut : rez)
        {
            rezi[i] = fut.get();
            i++;
        }
        return fun1_posl(rezi);
    }// end of 1

    private static Callable<Integer> task;

    public void fun2() {
        Scanner in = new Scanner(System.in);
        task = () -> {
            System.out.print("Введите число: ");
            int num = in.nextInt();
            new Thread(Pract1::func).start();
            Thread.sleep(5000);
            System.out.println();
            System.out.println("Pow of " + num + " : " + Math.pow(num, 2));
            System.out.print("Введите число: ");
            return 0;
        };
        System.out.println("Калькулятор квадратов. Задержка 5 секунд");
        func();
    }

    public static void func() {
        FutureTask<Integer> future = new FutureTask<>(task);
        new Thread(future).start();
        try {
            future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }// end of 2

    private static final Queue<MFile> files = new LinkedList<>();

    public void fun3() {
        //ForkJoinPool forkJoinPool = new ForkJoinPool(thNum);
        Generator gen = new Generator();
        Act act = new Act(Type.JSON);
        Act act1 = new Act(Type.XLS);
        Act act2 = new Act(Type.XML);
        gen.fork();
        act.fork();
        act1.fork();
        act2.fork();
        while (true)
        {
            get_pause(new Object(), 10);
            if(isEmpty())
            {
                if(gen.isDone())
                {
                    gen.reinitialize();
                    gen.fork();
                }
            } else {
                if(act.isDone())
                {
                    act.reinitialize();
                    act.fork();
                }
                if(act1.isDone())
                {
                    act1.reinitialize();
                    act1.fork();
                }
                if(act2.isDone())
                {
                    act2.reinitialize();
                    act2.fork();
                }
            }
        }
    }

    public static Queue<MFile> getFiles() {
        return files;
    }

    public static final Timer timer = new Timer();

    public static TimerTask ge(Object lock)
    {
        return new TimerTask() {
            public void run() {
                synchronized (lock) {
                    lock.notify();
                }
            }
        };
    }

    public static void get_pause(Object lock, long timeout)
    {
        timer.schedule(ge(lock), timeout);
        synchronized (lock) {
            try {
                lock.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static synchronized MFile peek()
    {
        return files.peek();
    }

    public static synchronized MFile poll()
    {
        return files.poll();
    }

    public static synchronized boolean isEmpty()
    {
        return files.isEmpty();
    }

    public static synchronized int size()
    {
        return files.size();
    }

    public static synchronized void add(MFile mFile)
    {
        files.add(mFile);
    }
}

class Worke extends RecursiveTask<Integer> {

    int[] arrayList;

    public Worke(int[] array){
        arrayList = array;
    }

    @Override
    protected Integer compute() {
        return Pract1.fun1_posl(arrayList);
    }
}//end of 1

enum Type { XML, JSON, XLS }

record MFile(Type type, int size){}

class Act extends RecursiveAction {

    private final Object lock = new Object();

    private final Type type;

    public Act(Type type) {
        this.type = type;
    }

    @Override
    protected void compute() {
        System.out.println("Thread Act: " + Thread.currentThread().getName());
        while (true) {
            if (Pract1.isEmpty() || Pract1.peek() == null) {
                System.out.print("");
                break;
            }
            if (Pract1.peek().type() != type) {
                System.out.print("");
                continue;
            }
            MFile file = Pract1.poll();
            int timeout = file.size() * 7;
            Pract1.get_pause(lock, timeout);
            System.out.println("Act Timeout: " + timeout + "ms, MFile: " + file);
        }
    }
}

class Generator extends RecursiveAction {

    private final Object lock = new Object();

    @Override
    protected void compute() {
        System.out.println("Thread Gen: " + Thread.currentThread().getName());
        while (Pract1.size() < 5) {
            MFile file = new MFile(Type.values()[(int) Math.round(Math.random() * 2)], (int) Math.round((Math.random() * 90) + 10));
            long timeout = Math.round((Math.random() * 900) + 100);
            Pract1.get_pause(lock, timeout);
            Pract1.add(file);
            System.out.println("Gen Timeout: " + timeout + "ms, MFile: " + file + " size: " + Pract1.size());
        }
    }
}