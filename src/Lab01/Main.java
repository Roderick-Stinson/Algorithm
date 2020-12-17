package Lab01;

import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        //params for package0_1
        int[] weight = {7, 3, 4, 5};
        int[] value = {42, 12, 40, 25};
        int capacity = 10;

        System.out.println("01背包问题答案：");
        System.out.println(Solution.bruteForce_Package01(weight, value, capacity));

        //params for tsp
        int[][] matrix = {
                {0, 3, 6, 7},
                {12, 0, 2, 8},
                {8, 6, 0, 2},
                {3, 7, 6, 0}
        };

        System.out.println("TSP问题答案：");
        System.out.println(Solution.bruteForce_TSP(matrix, 0));
    }
}

class Solution {
    public static int bruteForce_Package01(int[] weight, int[] value, int capacity) {
        //共有2^n个子集情况
        int row = (int) Math.pow(2, weight.length);
        //设置每一个子集的大小最多为所有物品个数个
        int column = weight.length;
        //建立一个所有子集的二维数组，针对每一个子集进行计算
        int[][] goodsSubset = new int[row][column];
        //最大总价值
        int maxValue = 0;

        //填充二维数组
        for (int i = 0; i < row; i++) {
            int temp_1 = i;
            for (int j = 0; j < column; j++) {
                int temp_2 = temp_1 % 2;
                goodsSubset[i][j] = temp_2;
                temp_1 = temp_1 / 2;
            }
        }

        //遍历每一个子集，为每一个子集生成一个最大价值
        for (int i = 0; i < goodsSubset.length; ++i) {
            int tempWeight = 0;
            int tempValue = 0;
            for (int j = 0; j < goodsSubset[i].length; ++j) {
                if (goodsSubset[i][j] == 1) {
                    tempWeight += weight[j];
                    tempValue += value[j];
                }
            }
            if (tempWeight <= capacity) {
                if (tempValue > maxValue)
                    maxValue = tempValue;
            }
        }
        return maxValue;
    }

    public static int bruteForce_TSP(int[][] matrix, int visited) {
        int minPath = (int) Double.MAX_VALUE;
        int distance = 0;

        ArrayList<Integer> city_list = new ArrayList();
        ArrayList<int[]> list = new ArrayList();
        int[] city_array;
        //初始化需要全排列的数组
        for (int i = 0; i < matrix.length; ++i) {
            if (i != visited)
                city_list.add(i);
        }
        //将arraylist转化成int数组
        city_array = city_list.stream().mapToInt(k -> k).toArray();
        //进行全排列
        permutation(city_array, visited, list);
        //遍历所有的可能情况
        for (int i = 0; i < list.size(); ++i) {
            distance = 0;
            int[] per_arr = list.get(i);
            //初始城市和其他城市之间有路
            if (matrix[visited][per_arr[0]] != 0 && matrix[per_arr[per_arr.length - 1]][visited] != 0)
                distance += matrix[visited][per_arr[0]] + matrix[per_arr[per_arr.length - 1]][visited];
            else
                break;
            for (int j = 0; j < list.get(i).length - 1; ++j) {
                //两个城市之间有路
                //其他城市和其他城市之间是否通
                if (matrix[per_arr[j]][per_arr[j + 1]] != 0)
                    distance += matrix[per_arr[j]][per_arr[j + 1]];
                else
                    break;
                //所有城市之间全都通
                if (j == per_arr.length - 2) {
                    if (distance < minPath)
                        minPath = distance;
                }
            }
        }
        return minPath;
    }

    public static void permutation(int[] arr, int start, ArrayList<int[]> list) {
        if (start == arr.length - 1) {
            list.add(Arrays.copyOf(arr, arr.length));
        }
        for (int i = start; i < arr.length; ++i) {
            if (i == start || arr[i] != arr[start]) {
                //把第一个元素分别与后面的元素进行交换，递归调用其子数组进行排列
                swap(arr, i, start);
                permutation(arr, start + 1, list);
                //恢复数组
                swap(arr, i, start);
            }
        }
    }

    public static void swap(int[] arr, int i, int j) {
        int tmp;
        tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }
}
