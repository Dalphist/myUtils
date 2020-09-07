package test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author DJF
 * @version 0.1.0
 * @Description
 * @create 2020-07-15 14:48
 * @since 0.1.0
 **/
public class Test {
    public static void main(String[] args) {
        Student s1 = new Student();
        s1.setName("aa");

        Student s2 = new Student();
        s2.setName("bb");

        List<Student> list1 = new ArrayList<>();
        list1.add(s1);
        list1.add(s2);

        List<User> list2 = new ArrayList<>();

        list2 = list1.stream().map(s -> {
            User u = new User();
            u.setName(s.getName());
            return u;
        }).collect(Collectors.toList());


    }
}
