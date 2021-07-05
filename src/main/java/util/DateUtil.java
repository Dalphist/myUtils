package util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author DJF
 * @version 0.1.0
 * @Description  尝试用下jdk1.8 的LocaDate
 * @create 2020-10-14 10:48
 * @since 0.1.0
 **/
public class DateUtil {
    public static void main(String[] args) {
        LocalDate localDate = LocalDate.now();
        LocalDate localDateBefore = localDate.minusDays(7);
        System.out.println(localDateBefore);

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateStr = localDate.format(fmt);
        System.out.println(dateStr);
    }
}
