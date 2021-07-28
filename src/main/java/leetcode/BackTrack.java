package leetcode;

import java.util.Arrays;

/**
 * @author DJF
 * @version 0.1.0
 * @Description 回溯法
 * @create 2021-07-27 09:15
 * @since 0.1.0
 **/
public class BackTrack {
    static int[] nums = {1,2,3,4,5};
    static int target = 9;
    static int n = nums.length;
    static int[] currentX = new int[n];
    static int iter = 0;

    public static void main(String[] args) {
        backTrack(0);
    }
    static void backTrack(int i ) {
        if(i == n) {
            //结束
            return;
        }
        //求和
        int currentSum = sum(currentX);
        if(currentSum+nums[i] < target) {
            //满足约束条件
            currentX[i] = 1;
            backTrack(i+1);
        }else if(currentSum+nums[i]  == target) {
            //满足约束条件
            currentX[i] = 1;
            System.out.println(Arrays.toString(currentX));
            return;
        }
        //准备进入右子树，不一定能进入
        currentX[i] = 0;
        //不满足约束条件,加入剪枝函数，减低遍历次数
        //设计规则：当前和只加上右子树的最大值都无法达到target，则不用进入右子树
        // if(currentSum + bound(i+1) >= target) {
        System.out.println(++iter);
        backTrack(i+1);
        //}

    }
    static int bound(int i) {
        int sum = 0;
        for(;i<n;i++) {
            sum+=nums[i];
        }
        return sum;
    }
    static int sum(int[] x) {
        int sum = 0;
        for (int i = 0; i < x.length; i++) {
            if(x[i] == 1) {
                sum+=nums[i];
            }
        }
        return sum;
    }
}
