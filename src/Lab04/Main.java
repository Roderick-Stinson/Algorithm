package Lab04;

public class Main {
    public static void main(String[] args) {
        int[] arr = {4, 7, 2, 1, 5, 6};
        int k = 2;
        Solution solution = new Solution(arr);
        //System.out.println("K最小问题答案为：" + solution.findKthMinimum());
        System.out.println("逆序对个数为：" + solution.reverseParis());
    }
}

class Solution {
    int pivot;
    int left;
    int right;
    int[] nums;
    int k;

    Solution(int[] arr, int k) {
        this.nums = arr;
        this.k = k;
    }

    public int findKthMinimum() {
        // 使用快速排序的思路，
        // 快速排序每一趟都会随机找个数作为pivot，该趟执行后，pivot在排序后数组的位置就会被确定。
        // 如果pivot的index正好为k，那就找到了结果，
        // 否则，如果pivot的index>k，那么在pivot之前的区间再次重复上述过程。
        // 反之，如果pivot的index<k，那么在pivot之后的区间再次重复上述过程。

        return findKthByQuickSort(0, nums.length - 1, k - 1);
    }

    /**
     * @param from
     * 数组的开始
     * @param to
     * 数组的结束
     * @param k
     * 第k个值
     * @return
     */
    public int findKthByQuickSort(int from, int to, int k) {
        left = from;
        right = to;
        pivot = nums[left];
        //将原数组进行排序，确定pivot在排序中的位置
        while (left < right) {
            while (left < right) {
                if (nums[right] < pivot) {
                    nums[left] = nums[right];
                    left++;
                    break;
                }
                right--;
            }
            while (left < right) {
                if (nums[left] > pivot) {
                    nums[right] = nums[left];
                    right--;
                    break;
                }
                left++;
            }
        }
        //恢复数组
        nums[left] = pivot;
        if (left == k) {
            return pivot;
        } else if (left < k) {
            // 如果pivot的index>k，那么在pivot之前的区间再次重复上述过程。
            return findKthByQuickSort(left + 1, to, k);
        } else {
            // 如果pivot的index<k，那么在pivot之后的区间再次重复上述过程。
            return findKthByQuickSort(from, left - 1, k);
        }
    }

    /**************************************************************************************/

    private int[] temp;//归并排序的辅助数组，全局使用
    private int len;//输入数组的长度

    Solution (int[] nums) {
        this.nums = nums;
        this.len = nums.length;
        this.temp = new int[len];
    }

    public int reverseParis() {
        if (len < 2)
            return 0;

        int left = 0;
        int right = len-1;

        return reverseParis(0, len-1, temp);
    }

    /**
     * 要求nums[left..mid]， nums[mid+1..right]有序
     * @param nums
     * @param left
     * @param mid
     * @param right
     * @param temp
     * @return
     */
    private int mergeAndCount(int[] nums, int left, int mid, int right, int[] temp) {
        for (int i = left; i <= right; ++i) {
            temp[i] = nums[i];
        }

        int pointerLeft = left;
        int pointerRight = mid+1;

        int count = 0;
        for (int i = left; i <= right; ++i) {
            if (pointerLeft == mid+1) //左数组为空，将右数组整理到原数组中
                nums[i] = temp[pointerRight++];
            else if (pointerRight == right+1) //右数组为空，将左数组整理到原数组中
                nums[i] = temp[pointerLeft++];
            else if (temp[pointerLeft] <= temp[pointerRight]) //一般情况
                nums[i] = temp[pointerLeft++];
            else {
                nums[i] = temp[pointerRight++];
                count += (mid - pointerLeft + 1);//左边数组剩余的个数即为相对于刚刚排序的元素的逆序对
                for (int k = pointerLeft; k <= mid; ++k) {
                    System.out.println(temp[k] + "->" + nums[i]);
                }
            }
        }

        return count;
    }

    private int reverseParis(int left, int right, int[] temp) {
        if (left == right)
            return 0;

        int mid = left + (right-left)/2;//避免溢出

        int leftParis = reverseParis(left, mid, temp);
        int rightParis = reverseParis(mid+1, right, temp);
        int crossParis = mergeAndCount(nums, left, mid, right, temp);

        return leftParis + rightParis + crossParis;
    }
}
