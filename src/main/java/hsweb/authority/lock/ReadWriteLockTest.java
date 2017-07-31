package hsweb.authority.lock;

import java.util.Random;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLockTest {

    public static void main(String[] args) {
        final Data data = new Data();
        for (int i = 0; i < 3; i++) {
            new Thread(() -> {
                do {
                    data.get();
                } while (true);
            }).start();
        }

        for (int i = 0; i < 3; i++) {
            new Thread(()->{
                while (true) {
                    data.put(new Random().nextInt(10000));
                }
            }).start();
        }
    }
}

class Data {
    private Object data = null; //数据
    private ReadWriteLock lock = new ReentrantReadWriteLock();
    public void get(){

        lock.readLock().lock();
        System.out.println(Thread.currentThread().getName() + " be ready to fetch data!");
        try {
            Thread.sleep((long) (Math.random() * 1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println(Thread.currentThread().getName() + "have read data! + " + data);
            lock.readLock().unlock();
        }
    }

    public void put(Object data) {

        lock.writeLock().lock(); //上写锁其他线程不允许读也不允许写
        System.out.println(Thread.currentThread().getName() + " be ready to put data!");
        try {
            Thread.sleep((long) (Math.random() * 1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            this.data = data;
            System.out.println(Thread.currentThread().getName() + " have set data!");
            lock.writeLock().unlock();

        }
    }
}
