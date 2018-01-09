/***
 * Initially, there is a Robot at position (0, 0). Given a sequence of its moves, judge if this robot makes a circle,
 * which means it moves back to the original place.
 * The move sequence is represented by a string. And each move is represent by a character.
 * The valid robot moves are R (Right), L (Left), U (Up) and D (down).
 * The output should be true or false representing whether the robot makes a circle.
 *
 * Example 1:
 * Input: "UD"
 * Output: true
 * Example 2:
 * Input: "LL"
 * Output: false
 *
 */
public class LeetCode657 {
    public static void main(String[] args) {
        System.out.println("is Circle:: " + judgeCircle("LRLRDUUD"));
        System.out.println("is Circle:: " + leetCodeJudgeCircle("LRLRDUUD"));
    }

    private static boolean judgeCircle(String moves) {
        char[] chars = moves.toCharArray();
        int upSet = 0;
        int downSet = 0;
        int leftSet = 0;
        int rightSet = 0;
        for (char charAt : chars) {
            if (charAt == 'U') {
                upSet++;
            } else if (charAt == 'D') {
                downSet++;
            } else if (charAt == 'L') {
                leftSet++;
            } else if (charAt == 'R') {
                rightSet++;
            }
        }

        return upSet == downSet && leftSet == rightSet;
    }

    /**
     * 该题 比较简单 但是区别于自己的做法，下面这个做法更能节约变量空间 也更形象
     * @param moves
     * @return
     */
    private static boolean leetCodeJudgeCircle(String moves) {
        int x = 0, y = 0;
        for (char move : moves.toCharArray()) {
            if (move == 'U') y--;
            else if (move == 'D') y++;
            else if (move == 'L') x--;
            else if (move == 'R') x++;
        }
        return x == 0 && y == 0;
    }
}
