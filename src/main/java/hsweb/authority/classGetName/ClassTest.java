package hsweb.authority.classGetName;

public class ClassTest implements TestInterface {


    public static void main(String[] args) {

        System.out.println("sqrt ----> " + new ClassTest().sqrt(100));

        /**
         * 结果是 hsweb.authority.classGetName.TestInterface ,表示getName输出的是全路径名
         */
        System.out.println(TestInterface.class.getName());
    }
}
