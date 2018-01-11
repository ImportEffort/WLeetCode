package day3;

import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/***
 * Design a logger system that receive stream of messages along with its timestamps, each message should be printed if and only if it is not printed in the last 10 seconds.

 * Given a message and a timestamp (in seconds granularity), return true if the message should be printed in the given timestamp, otherwise returns false.
 *
 * It is possible that several messages arrive roughly at the same time.
 *
 * Example:
 *
 * Logger logger = new Logger();
 *
 * // logging string "foo" at timestamp 1
 * logger.shouldPrintMessage(1, "foo"); returns true;
 *
 * // logging string "bar" at timestamp 2
 * logger.shouldPrintMessage(2,"bar"); returns true;
 *
 * // logging string "foo" at timestamp 3
 * logger.shouldPrintMessage(3,"foo") returns false;
 *
 * // logging string "bar" at timestamp 8
 * logger.shouldPrintMessage(8,"bar"); returns false;
 *
 * // logging string "foo" at timestamp 10
 * logger.shouldPrintMessage(10,"foo"); returns false;
 *
 * // logging string "foo" at timestamp 11
 * logger.shouldPrintMessage(11,"foo"); returns true;
 *
 * Credits:
 * Special thanks to @memoryless for adding this problem and creating all test cases.
 *
 */
public class leetCode359 {

    public static void main(String[] args) {

        Map<Integer, String> integerStringMap = gengerateMsg();

        integerStringMap.forEach((key, value) -> {

            boolean shouldPrintln = true;

            //随便取一个数这个数的 key 向前数10s 内如果有同样的 value 则不打印，如果没有相同的则打印

            for (int i = 1; i < 11; i++) {
                key = key - i;
                if (integerStringMap.containsKey(key)) {
                    String temp = integerStringMap.get(key);
                    if (temp.equals(value)) {
                        shouldPrintln = false;
                        break;
                    }
                }
            }

            printData(key, value, shouldPrintln);
        });


    }

    private static void printData(int key, String value, boolean shouldPrint) {
        System.out.println("logger.shouldPrintMessage(" + key + "," + value + ");" + "returns " + shouldPrint);
    }

    private static Map<Integer, String> gengerateMsg() {
        Map<Integer, String> timeDatas = new LinkedHashMap<>();
        timeDatas.put(1, "foo");
        timeDatas.put(2, "bar");
        timeDatas.put(3, "foo");
        timeDatas.put(8, "foo");
        timeDatas.put(10, "bar");
        timeDatas.put(11, "foo");
        timeDatas.put(13, "foo");
        timeDatas.put(16, "bar");
        timeDatas.put(16, "foo");
        timeDatas.put(46, "foo");
        timeDatas.put(61, "bar");
        return timeDatas;
    }
}
