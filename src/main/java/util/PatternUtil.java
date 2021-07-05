package util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author DJF
 * @version 0.1.0
 * @Description 正则工具类尝试
 * @create 2021-05-14 10:34
 * @since 0.1.0
 **/
public class PatternUtil {
    private static  Pattern p1 = Pattern.compile("(I-)[0-9]+");
    public static void main(String[] args) {
        //长字符串里截取多个某规律的字符串，比如  I-0000011/I-0000026 +  I-0000033 截取到 [I-0000011,I-0000026,I-0000033]
        String input = "(I-0000011 + 2)/I-0000026 * 6 +  I-0000033";
        Matcher m = p1.matcher(input);
        List<String> output = new ArrayList<>();
        while (m.find()) {
            output.add(m.group());
        }
        output.forEach(System.out :: println);

    }
}
