package Lab05;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        //params for package0_1
        int[] weight = {7, 3, 4, 5};
        int[] value = {42, 12, 40, 25};
        int capacity = 10;

        Solution solution = new Solution(weight, value, capacity);

        System.out.println("01背包问题答案：");
        System.out.println(solution.getMaxValueWithOneDimensionalArray());

        int[][] timeInterval = {
                {0, 4},
                {2, 6},
                {1, 3},
                {4, 8},
                {9, 10}
        };
        int[] weightRequire = {5, 6, 3, 5, 2};

        Solution solution1 =  new Solution(timeInterval, weightRequire);

        System.out.println("带权区间问题答案：");
        System.out.println(solution1.getMaxWeight());
    }
}

class Solution {
    private int[] weight;
    private int[] values;
    private int capacity;
    private int length;

    Solution(int[] weight, int[] values, int capacity) {
        this.weight = weight;
        this.values = values;
        this.capacity = capacity;
        this.length = weight.length;
    }

    public int getMaxValue() {
        //初始化动态规划数组
        int[][] dp = new int[this.length][this.capacity+1];
        //为了便于理解,将dp[i][0]和dp[0][j]均置为0，从1开始计算
        for(int i=1; i<this.length; i++){
            for(int j=0; j<this.capacity+1; j++){
                //如果第i件物品的重量大于背包容量j,则不装入背包
                //由于weight和value数组下标都是从0开始,故注意第i个物品的重量为weight[i-1],价值为value[i-1]
                if(weight[i] > j)
                    dp[i][j] = dp[i-1][j];
                else
                    dp[i][j] = Math.max(dp[i-1][j],dp[i-1][j-weight[i]]+values[i]);
            }
        }
        return dp[this.length-1][this.capacity];
    }

    public int getMaxValueWithOneDimensionalArray() {
        int[] dp = new int[this.capacity+1];
        dp[0] = 0;
        for (int i = 1; i < this.length; ++i) {
            for (int j = this.capacity; j >= weight[i-1]; --j) {
                dp[j] = Math.max(dp[j], dp[j-weight[i-1]]+values[i]);
            }
        }
        return dp[this.capacity];
    }

    /************************************************************************************************************/

    private int numRequire;
    private int[][] timeInterval;
    private int[] weightRequire;
    private List<Event> events = new ArrayList<>();

    Solution(int[][] timeInterval, int[] weightRequire) {
        this.numRequire = weightRequire.length;
        this.timeInterval = timeInterval;
        this.weightRequire = weightRequire;
    }

    public int getMaxWeight() {

        for (int i = 0; i < this.numRequire; ++i) {
            Event event = new Event(timeInterval[i][0], timeInterval[i][1], weightRequire[i]);
            events.add(event);
        }
        Collections.sort(events);

        int[] p = new int[this.numRequire];

        for (int i = 1; i < numRequire; ++i) {
            for (int j = i - 1; j > 0; --j) {
                if (events.get(j).endTime <= events.get(i).beginTime) {
                    p[i] = j;
                    break;
                }
            }
        }

        int[] dp = new int[this.numRequire];
        dp[0] = 0;

        for (int i = 1; i < numRequire; ++i) {
            if (dp[p[i]] + events.get(i).weight > dp[i-1]) {
                dp[i] = dp[p[i]] + events.get(i).weight;
            } else {
                dp[i] = dp[i-1];
            }
        }

        return dp[numRequire-1];
    }

    class Event implements Comparable<Event>{
        public int beginTime;
        public int endTime;
        public int weight;

        Event(int beginTime, int endTime, int weight) {
            this.beginTime = beginTime;
            this.endTime = endTime;
            this.weight = weight;
        }

        @Override
        public int compareTo(Event o) {
            if (endTime < o.endTime)
                return -1;
            else if (endTime == o.endTime)
                return 0;
            else
                return 1;
        }
    }
}