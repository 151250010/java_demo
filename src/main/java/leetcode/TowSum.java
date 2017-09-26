package leetcode;

 /*Given an array of integers, return indices of the two numbers such that they add up to a specific target.

    You may assume that each input would have exactly one solution, and you may not use the same element twice.

         Example:
         Given nums = [2, 7, 11, 15], target = 9,

         Because nums[0] + nums[1] = 2 + 7 = 9,
         return [0, 1].

 */

import java.util.HashMap;
import java.util.Map;

public class TowSum {

    public int[] twoSum(int[] nums, int target) {

        for (int i = 0; i < nums.length; i++) {
            int total = nums[i];
            for (int j = i + 1; j < nums.length; j++) {
                total += nums[j];
                if (total == target) {
                    return new int[]{i, j};
                }
                total = nums[i];
            }
        }

        return null;
    }

    /**
     * LeetCode 提供的三种解法
     */
    public int[] twoSumViolentSolution(int[] nums, int target) {

        for (int i = 0; i < nums.length; i++) {
            for (int j=i+1;j<nums.length;j++) {
                if (nums[j] == target - nums[i]) {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }

    /**
     * 使用HashMap接近常数级的遍历，牺牲空间换取运行效率
     * @param nums
     * @param target
     * @return
     */
    public int[] twoSumHashMap(int[] nums, int target) {

        Map<Integer, Integer> array = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            array.put(nums[i], i);
        }

        for (int i = 0; i < nums.length; i++) {
            int remain = target - nums[i];
            if (array.containsKey(remain) && array.get(remain) != i) {
                return new int[]{i, array.get(remain)};
            }
        }

        return null;
    }

    public int[] twoSumOneSideHash(int[] nums, int target) {

        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            int remain = target - nums[i];
            if (map.containsKey(remain)) {
                return new int[]{i, map.get(remain)};
            }
            map.put(nums[i], i);
        }
        return null;
    }

}
