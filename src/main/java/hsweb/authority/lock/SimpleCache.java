package hsweb.authority.lock;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 读写锁 实现简单的缓存
 *
 * 需要注意的是 ： 读锁再申请写锁就会造成死锁,写锁申请读锁是一种锁的降级，没 */
public class SimpleCache {

    private Map<String, Object> cache = new HashMap<>();
    private static final ReadWriteLock lock = new ReentrantReadWriteLock();

    public Object get(String id) {
        Object value = null;
        lock.readLock().lock();
        value = cache.get(id);
        if (value == null) {
            //释放读 锁
            lock.readLock().unlock();

            lock.writeLock().lock();
            if (value == null) {
                //再次判断==null,是可能线程已经修改了数据
                //写入数据
                value = "test ok ----> " + Thread.currentThread().getName();
                cache.put(id, value);
            }

            //先申请读锁，然后释放写锁
            lock.readLock().lock();
            lock.writeLock().unlock();
        }
        value = cache.get(id);
        lock.readLock().unlock();
        return value;
    }

    public static void main(String[] args) {

        SimpleCache cache = new SimpleCache();
        for (int i = 0; i < 3; i++) {
            new Thread(() -> {
                while (true) {
                    System.out.println(Thread.currentThread().getName() + " be ready to get the cache");
                    System.out.println(Thread.currentThread().getName()+" get data ----> "+ cache.get("data"));
                    System.out.println(Thread.currentThread().getName() + " get data successfully!");
                }
            }).start();
        }
    }
}
