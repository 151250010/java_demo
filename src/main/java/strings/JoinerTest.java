package strings;

import com.google.common.base.Joiner;

import java.util.Arrays;
import java.util.List;

public class JoinerTest {

    public static void main(String[] args) {
        List<String> testList = Arrays.asList(null, "asd", "dfg", "ghj");
        Joiner joiner = Joiner.on("12").useForNull("曾锡豪");
        System.out.println(joiner.join(testList));
    }
}
