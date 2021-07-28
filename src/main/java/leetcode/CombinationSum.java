package leetcode;

import java.util.*;

/**
 * @author DJF
 * @version 0.1.0
 * @Description
 * @create 2021-07-27 10:58
 * @since 0.1.0
 **/
public class CombinationSum {

    public static void main(String[] args) {
        int[] candidates = {5,4,1,2,3,1,1};
        List<List<Integer>> list = combinationSum2(candidates,6);
        System.out.println(list);
    }
    public static List<List<Integer>> combinationSum2(int[] candidates, int target) {
        List<List<Integer>> res=new ArrayList<>();
        Deque<Integer> path=new LinkedList<>();
        // 排序方便进行剪枝
        Arrays.sort(candidates);
        dfs(res,path,candidates,0,target);
        return res;
    }

    /**
     *
     * @param res 结果集，默认为空，符合条件时，直接将path赋值到res
     * @param path 普通队列，储存符合条件的数据队列
     * @param nums 原数组？
     * @param index 原数组下标
     * @param target 与目标差值
     */
    private static void dfs(List<List<Integer>> res,Deque<Integer> path,int[] nums,int index,int target){
        //如果结果符合，将队列数据插入到结果集
        if (target==0){
            res.add(new ArrayList<>(path));
            return;
        }
        // 下标越界，终止。
        if (index>=nums.length) {
            return;
        }
        List<Integer> pre=new ArrayList<>(nums.length);
        // 此处遍历决定的是每一个位置的数字的值
        for (int i=index;i<nums.length;i++){
            //去除重复结果集
            if (pre.contains(nums[i])) {
                continue;
            }
            // 剪枝
            if (target-nums[i]<0) {
                break;
            }
            path.addLast(nums[i]);
            pre.add(nums[i]);
            dfs(res,path,nums,i+1,target-nums[i]);
            //回溯
            path.removeLast();
        }
    }


}
