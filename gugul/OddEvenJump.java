package gugul;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

public class OddEvenJump {

    public static class Node {
        int value;
        int index;

        // incoming
        List<Node> odds = new ArrayList<>();
        List<Node> evens = new ArrayList<>();
        
        // outcoming;
        Node odd;
        Node even;

        public String toString() {
            return String.format("[%d at %d]\nOdd: %d\nEven: %d", 
                                 value, index,
                                 odd != null ? odd.value : -1,
                                 even != null ? even.value : -1);
        }
    }

    public static int oddEvenJump(int[] input) {
        Node[] graph = new Node[input.length];
        TreeSet<Node> pool = new TreeSet<>((a, b) -> a.value == b.value ? a.index - b.index : a.value - b.value);
        
        for (int i = 0; i < input.length; i++) {
            graph[i] = new Node();
            graph[i].value = input[i];
            graph[i].index = i;

            pool.add(graph[i]);
        }

        // graph building starts.
        Node current, nextOdd, nextEven;

        for (int i = 0; i < graph.length; i++) {
            current = graph[i];
            pool.remove(current);

            nextOdd = pool.higher(current);
            nextEven = pool.lower(current);

            if (nextOdd != null) {
                current.odd = nextOdd;
                nextOdd.odds.add(current);
            }

            if (nextEven != null) {
                current.even = nextEven;
                nextEven.evens.add(current);
            }
        }

        System.out.println(Arrays.toString(graph));

        return 1;
    }

    public static void main(String...args) {
        int[][] cases = {
            {10,13,12,14,15},
            {2,3,1,1,4},
            {5,1,3,4,2}
        };
        int[] expecteds = {2,3,3};
        int result;

        for(int i = 0; i < cases.length; i++) {
            result = oddEvenJump(cases[i]);
            System.out.println(
                String.format("********\nInput: %s\nOutput: %d\nExpected: %d\nResult: %s\n********\n",
                              Arrays.toString(cases[i]),
                              result,
                              expecteds[i],
                              result == expecteds[i] ? "PASS" : "FAIL")
            );
        }
    }
}
