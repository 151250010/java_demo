package softReferences;

import java.lang.ref.SoftReference;

public class Main {



    public static void main(String[] args) {
        Sample sample = new Sample();

        //注意：softReference这个引用也是强引用，它是指向SoftReference这个对象的
        //那么这个软引用在哪呢？ 可以跟一下java.lang.Reference的源码
        //private T referent; 这个才是软引用， 只被jvm使用
        SoftReference<Sample> softReference = new SoftReference<>(sample); // softReference 仍然是强引用，但是现在已经有一个软引用指向上面new的sample对象

        sample = null;

        sample = softReference.get();

        // 软引用相比强引用可以避免OOM（out of memory）

        //
    }


}
class Sample{

}