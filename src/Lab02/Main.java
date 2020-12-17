package Lab02;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        // write your code here
        //params for package_01
        int[] weight = {7, 3, 4, 5};
        int[] value = {42, 12, 40, 25};
        int capacity = 10;

        //Test Package_01
        Solution solutionPackage01 = new Solution(weight, value, capacity);
        solutionPackage01.backTrack_Package01(0);
        int maxValue = solutionPackage01.getMaxValue();
        System.out.println("01背包问题答案: " + maxValue);

        //params for graph coloring
        int[][] matrix = {
                {0, 1, 1, 1, 0},
                {1, 0, 1, 1, 1},
                {1, 1, 0, 1, 0},
                {1, 1, 1, 0, 1},
                {0, 1, 0, 1, 0}
        };
        int colorSum = 4;

        //Test Graph Coloring
        Solution solutionGraphColoring = new Solution(matrix, colorSum);
        solutionGraphColoring.backTrack_GraphColor(0);
        int sum = solutionGraphColoring.getSum();
        System.out.println("颜色数为: " + colorSum + "时，" + "可选的方案数为: " + sum);
    }
}


class Solution {
    public int capacity;
    public int number;
    public int[] weight;
    public int[] values;
    public int currentWeight;
    public int currentValue;
    public int maxValue;
    public int[] chosion;

    public Solution(int[] weight, int[] values, int capacity) {
        //Problem Package_01
        this.weight = weight;
        this.values = values;
        this.capacity = capacity;
        this.number = weight.length;
        this.chosion = new int[number];
        this.maxValue = 0;
        this.currentValue = 0;
        this.currentWeight = 0;
    }

    public void backTrack_Package01(int i) {
        //递归结束条件判定
        if (i >= number) {
            if (this.currentValue > this.maxValue) {
                this.maxValue = this.currentValue;
            }
            return ;
        }

        //第i个东西放入背包
        if (this.currentWeight+this.weight[i] <= this.capacity) {
            //记录选择
            this.chosion[i] = 1;

            this.currentWeight += this.weight[i];
            this.currentValue += this.values[i];
            //此处进行递归
            this.backTrack_Package01(i+1);
            //跳出递归后还原数据
            this.currentValue -= this.values[i];
            this.currentWeight -= this.weight[i];
        }

        //第i个东西不放入背包中
        this.chosion[i] = 0;
        this.backTrack_Package01(i+1);
    }

    public int getMaxValue() { return this.maxValue; }

    //Problem graphColoring
    public int[][] matrix;
    public int colorSum;
    public int[] color;
    public int sum;

    public Solution(int[][] matrix, int colorSum) {
        this.matrix = matrix;
        this.colorSum = colorSum;
        this.sum = 0;
        this.color = new int[this.matrix.length];
    }

    public void backTrack_GraphColor(int cur) {
        //递归结束条件判定
        if (cur >= this.matrix.length) {
            System.out.println(Arrays.toString(this.color));
            this.sum++;
            return;
        }
        // 递归主体
        for (int i = 1; i <= this.colorSum; ++i) {
            this.color[cur] = i;

            //判断是否冲突
            if (!this.isConflict(cur)) {
                //未冲突,则继续递归
                this.backTrack_GraphColor(cur+1);
            }

            //恢复数据
            this.color[cur] = 0;
        }

    }

    public boolean isConflict(int k) {
        for (int i = 0; i < k; ++i) {
            if (this.matrix[k][i] == 1 && this.color[k] == this.color[i])
                return true;
        }
        return false;
    }

    public int getSum() { return this.sum; }

}