package Lab03;

import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Queue;

public class Main {
    public static void main(String[] args) {
        int[] weight = { 4, 7, 5, 3 };
        int[] value = { 40, 43, 25, 12 };
        int i = 3;
        int capacity = 10;
        Solution solution = new Solution(weight, value, capacity);
        System.out.println("01背包问题：");
        System.out.println(solution.getAnswerPackage01());
        /********************************************************/
        int INF = 100000;
        int[][] distanceMatrix = { { INF, 3, 1, 5, 8 }, { 3, INF, 6, 7, 9 }, { 1, 6, INF, 4, 2 }, { 5, 7, 4, INF, 3 },
                { 8, 9, 2, 3, INF } };
        int startCity = 0;
        Solution solution1 = new Solution(distanceMatrix, startCity);
        System.out.println("TSP: ");
        System.out.println(solution1.getAnswerTSP());
    }
}

class Solution {
    Package01 package01Solution;

    Solution(int[] weight, int[] value, int capacity) {
        package01Solution = new Package01(weight, value, capacity);
    }

    public int getAnswerPackage01() {
        return package01Solution.getMaxValue();
    }

    class Package01 {
        private int[] weight;
        private int[] value;
        private int capacity;
        private int downBound;// 下界
        private double[] unitValue;// 单位价值数组
        private int[] oldIndex;// 根据单位价值排序的单位价值数组映射到原数组的下标

        Package01(int[] weight, int[] value, int capacity) {
            this.weight = weight;
            this.value = value;
            this.capacity = capacity;
            this.unitValue = new double[weight.length];
            this.oldIndex = new int[unitValue.length];
            sort();
            downBound = value[oldIndex[0]];
        }

        private void sort() {
            // 初始化
            for (int i = 0; i < unitValue.length; ++i) {
                unitValue[i] = value[i] / weight[i];
                oldIndex[i] = i;
            }
            // 开始排序(根据单位价值从大到小),使用泡沫法
            for (int i = 0; i < unitValue.length; ++i) {
                for (int j = i; j < unitValue.length - 1; ++j) {
                    if (unitValue[j] < unitValue[j + 1]) {
                        double tmpVdivisionW = unitValue[j + 1];
                        unitValue[j + 1] = unitValue[j];
                        unitValue[j + 1] = tmpVdivisionW;

                        int tmpIndex = oldIndex[j + 1];
                        oldIndex[j + 1] = oldIndex[j];
                        oldIndex[j] = tmpIndex;
                    }
                }
            }
        }

        private double getUpperBound(int curWeight, double curValue, int level) {
            // 求上界函数
            if (level < weight.length - 1) {
                return (curValue + (capacity - curWeight) * unitValue[level + 1]);
            } else {
                return curValue;
            }
        }

        public int getMaxValue() {
            int curWeight = 0;
            double curValue = 0;
            int maxValue = 0;
            Queue<Package01Node> priorityQueue = new PriorityQueue<Package01Node>();

            // 存放root节点
            Package01Node root = new Package01Node();
            root.level = -1;
            root.upprofit = getUpperBound(curWeight, curValue, root.level);
            priorityQueue.add(root);

            while (!priorityQueue.isEmpty()) {
                Package01Node fatherNode = priorityQueue.poll();

                // 若当前节点为叶子节点
                if (fatherNode.level == weight.length - 1) {
                    if (fatherNode.curValue > maxValue) {
                        // 设置最大价值
                        downBound = maxValue;
                        maxValue = (int) fatherNode.curValue;
                    } else {
                        // 更新下界
                        downBound = (int) fatherNode.curValue;
                    }
                } else {
                    // 广度优先搜索，先搜索左节点
                    if (weight[oldIndex[fatherNode.level + 1]] + fatherNode.curWeight <= capacity) {
                        Package01Node node1 = new Package01Node();
                        node1.level = fatherNode.level + 1;
                        node1.curWeight = fatherNode.curWeight + weight[oldIndex[fatherNode.level + 1]];
                        node1.curValue = fatherNode.curValue + value[oldIndex[fatherNode.level + 1]];
                        node1.upprofit = getUpperBound(node1.curWeight, node1.curValue, node1.level);
                        node1.isLeft = true;
                        if (node1.upprofit > downBound)
                            priorityQueue.add(node1);
                    }
                    // 搜索右节点
                    if (getUpperBound(fatherNode.curWeight, fatherNode.curValue, fatherNode.level + 1) > maxValue) {
                        Package01Node node2 = new Package01Node();
                        node2.level = fatherNode.level + 1;
                        node2.curValue = fatherNode.curValue;
                        node2.curWeight = fatherNode.curWeight;
                        node2.upprofit = getUpperBound(node2.curWeight, node2.curValue, node2.level);
                        node2.isLeft = false;
                        if (node2.upprofit > downBound)
                            priorityQueue.add(node2);
                    }
                }
            }
            return maxValue;
        }

        class Package01Node implements Comparable<Package01Node> {
            private double curValue;// 该节点目前的背包价值
            private int curWeight;// 该节点目前的重量
            private int level;// 该节点是第几个物品的选择是否加入背包
            private boolean isLeft;// 该节点是否属于左节点
            private double upprofit;// 该节点的价值上界

            // 根据upprofit排序
            @Override
            public int compareTo(Package01Node node) {
                if (this.upprofit < node.upprofit)
                    return 1;
                else if (this.upprofit == node.upprofit)
                    return 0;
                else
                    return -1;
            }
        }
    }

    TSP tspSolution;

    Solution(int[][] distanceMatrix, int startCity) {
        tspSolution = new TSP(distanceMatrix, startCity);
    }

    public int getAnswerTSP() {
        return tspSolution.getMinLength();
    }

    class TSP {
        private int[][] distaceMatrix;
        private int startCity;
        private int upBound = 0;
        private int downBound = 0;

        TSP(int[][] distanceMatrix, int startCity) {
            this.distaceMatrix = distanceMatrix;
            this.startCity = startCity;
            this.upBound = greedyMinDistance(startCity, 0, 0, new boolean[distanceMatrix.length]);
        }

        // 利用贪心法 DFS暴力求出最优解作为分支界限法的上界
        private int greedyMinDistance(int curCity, int count, int length, boolean[] visited) {
            if (count == distaceMatrix.length - 1) {
                return length + distaceMatrix[curCity][0];
            }
            int min = 100000;
            int nextCity = -1;
            for (int i = 0; i < distaceMatrix.length; ++i) {
                if (i != curCity && visited[i] == false && min > distaceMatrix[curCity][i]) {
                    min = distaceMatrix[curCity][i];
                    nextCity = i;
                }
            }
            visited[curCity] = true;// 设置当前城市已经经过，则在递归过程中，将不计算与该城市相连的边
            return greedyMinDistance(nextCity, count + 1, length + min, visited);
        }

        private int getDownBound() {
            // 最暴力的方法，每行去两个最小值算下界
            downBound = 0;
            for (int i = 0; i < distaceMatrix.length; ++i) {
                int[] temp = new int[distaceMatrix.length];
                for (int j = 0; j < distaceMatrix.length; ++j) {
                    temp[j] = distaceMatrix[i][j];
                }
                Arrays.sort(temp);
                downBound += temp[0] + temp[1];// 将每一行中最小的两个元素相加
            }
            return downBound / 2;
        }

        private int getLB(Node node) {
            // 价值评估函数，用来更新下界
            int ret = node.sumLength * 2;// 已经选择了的城市的进入城市算一次，离开城市算一次
            int min1 = 0x7fffffff, min2 = 0x7fffffff;

            for (int i = 0; i < distaceMatrix.length; ++i) {
                if (!node.visited[i] && min1 > distaceMatrix[i][node.startCity]) {
                    // 进入起始城市，离开起始城市已经在sumLength*2的时候算过一次了
                    min1 = distaceMatrix[i][node.startCity];
                }
            }
            ret += min1;
            for (int i = 0; i < distaceMatrix.length; ++i) {
                if (!node.visited[i] && min2 > distaceMatrix[node.endCity][i]) {
                    // 路径上最后一个城市后下一个应该走的城市
                    min2 = distaceMatrix[node.endCity][i];
                }
            }
            ret += min2;

            for (int i = 0; i < distaceMatrix.length; ++i) {
                if (!node.visited[i]) {
                    // 所有没访问过的城市的进和出的最短的两个路径
                    min1 = min2 = 0xfffffff;
                    int tmp = -1;// tmp用来存选择的城市的下标，防止两次选择的城市下标相同导致函数计算错误
                    for (int j = 0; j < distaceMatrix.length; ++j) {
                        if (min1 > distaceMatrix[i][j]) {
                            min1 = distaceMatrix[i][j];
                            tmp = j;
                        }
                    }
                    for (int j = 0; j < distaceMatrix.length; ++j) {
                        if (min2 > distaceMatrix[i][j] && j != tmp) {
                            min2 = distaceMatrix[i][j];
                        }
                    }
                    ret += min1 + min2;
                }
            }
            return (ret + 1) / 2;// 向上取整
        }

        public int getMinLength() {
            // 函数计算主体
            int ret = 0xfffffff;// ret为返回的答案
            Queue<Node> priorityQueue = new PriorityQueue<Node>();
            // root节点极其初始化
            Node root = new Node();
            root.lb = getDownBound();
            root.visited[0] = true;
            root.startCity = 0;
            root.endCity = 0;
            root.sumLength = 0;
            root.number = 1;
            priorityQueue.add(root);

            // BFS主体
            while (!priorityQueue.isEmpty()) {
                Node fatherNode = priorityQueue.poll();
                if (fatherNode.number == distaceMatrix.length - 1) {
                    // 还差一个点就能每个节点都访问一遍
                    int lastCity = -1;
                    for (int i = 0; i < distaceMatrix.length; ++i) {
                        if (!fatherNode.visited[i]) {
                            lastCity = i;
                            break;
                        }
                    }
                    // ans为当前选择的路径的代价值
                    int ans = fatherNode.sumLength + distaceMatrix[lastCity][fatherNode.startCity]
                            + distaceMatrix[fatherNode.endCity][lastCity];
                    if (ans < fatherNode.lb) {
                        // 小于下界，剪枝
                        ret = Math.min(ans, ret);
                        break;
                    } else {
                        // 更新已有上界
                        upBound = Math.min(upBound, ans);
                        ret = Math.min(ret, ans);
                    }
                }
                /**********************************************************/
                // 可以拓展节点
                for (int i = 0; i < distaceMatrix.length; ++i) {
                    Node next = new Node();
                    if (!fatherNode.visited[i]) {
                        next.startCity = fatherNode.startCity;
                        next.endCity = i;
                        next.sumLength = fatherNode.sumLength + distaceMatrix[fatherNode.endCity][i];
                        next.number = fatherNode.number + 1;
                        for (int j = 0; j < fatherNode.visited.length; ++j) {
                            next.visited[j] = fatherNode.visited[j];
                        }
                        next.visited[i] = true;
                        next.lb = getLB(next);
                        if (next.lb <= upBound)
                            priorityQueue.add(next);
                    }
                }
            }
            return ret;
        }

        class Node implements Comparable<Node> {
            int lb;// 价值函数值
            boolean[] visited = new boolean[distaceMatrix.length];
            int number;// 访问过的城市总数
            int startCity;// 起始城市
            int endCity;// 路径上最后一个城市
            int sumLength;// 总代价消耗

            @Override
            public int compareTo(Node o) {
                if (this.lb < o.lb)
                    return 1;
                else if (this.lb == o.lb)
                    return 0;
                else
                    return -1;
            }
        }

    }
}
