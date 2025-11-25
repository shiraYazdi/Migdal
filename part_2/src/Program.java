import java.util.ArrayList;
import java.util.List;

public class Program {
    public static List<List<Integer>> getSubsequence(int[] arr){
        List<List<Integer>> res = new ArrayList<>();
        List<Integer> current = new ArrayList<>();

        current.add(arr[0]);

        //Compares each element with the element before it
        for (int i = 1; i < arr.length; i++) {
            //In the event that the element is continuous with the element before it
            if (arr[i] -1 == arr[i-1] ){
                current.add(arr[i]);
            //In case it doesn't, the sequence resets
            } else {
                if (current.size() > 1){
                    res.add(new ArrayList<>(current));
                }
                current.clear();
                current.add(arr[i]);
            }
        }
        //Another check on the last element
        if (current.size() > 1){
            res.add(new ArrayList<>(current));
        }
        return res;
    }


    public static void main(String[] args) {
        int [] arr = {1, 2, 3, 1, 2};
        List<List<Integer>> subSe = getSubsequence(arr);
        for (List<Integer> seq: subSe){
            for (int num: seq){
                System.out.print(num);
            }
            System.out.println();
        }
    }
}