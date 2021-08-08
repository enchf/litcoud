package gugul;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import java.util.stream.Collectors;

public class OddEvenJump {

    public static class Node {
        int value;
        int index;

        // incoming
        Set<Node> odds = new HashSet<>();
        Set<Node> evens = new HashSet<>();

        public String toString() {
            return String.format("\n[%d at %d] - Incoming odds: [%s] - Incoming evens: [%s]", 
                                 value, index, print(odds), print(evens));
        }

        public static String print(Set<Node> nodes) {
            return nodes.stream().map((node) -> String.valueOf(node.value)).collect(Collectors.joining(","));
        }
    }

    public static class Jump {
        Node node;
        boolean odd;

        public Jump(Node node, boolean odd) {
            this.node = node;
            this.odd = odd;
        }

        @Override
        public boolean equals(Object o) {
            if (o == this)
                return true;
            if (!(o instanceof Jump))
                return false;
            Jump other = (Jump)o;
            return node == other.node && odd == other.odd;
        }

        @Override
        public int hashCode() {
            int result = 17;
            result = 31 * result + node.hashCode();
            return 31 * result + (odd ? 1 : 0);
        }
    }

    public static int oddEvenJump(int[] input) {
        Node[] graph = new Node[input.length];
        TreeSet<Node> pool = new TreeSet<>((a, b) -> a.value == b.value ? a.index - b.index : a.value - b.value);
        
        // graph vertex init, sort values keeping index.
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
                nextOdd.odds.add(current);
            }

            if (nextEven != null) {
                nextEven.evens.add(current);
            }
        }

        // path building from backwards.
        boolean[] odds = new boolean[graph.length];
        boolean[] evens = new boolean[graph.length];
        Set<Jump> paths = new HashSet<>();
        Jump next;

        int lastIndex = graph.length - 1;
        Node last = graph[lastIndex];
        
        odds[lastIndex] = true;
        evens[lastIndex] = true;
        
        for (Node node : last.odds) paths.add(new Jump(node, true));
        for (Node node : last.evens) paths.add(new Jump(node, false));

        while (!paths.isEmpty()) {
            next = paths.iterator().next();
            paths.remove(next);

            if (next.odd && !evens[next.node.index]) {
                evens[next.node.index] = true;
                for (Node node : next.node.evens) paths.add(new Jump(node, false));
            } else if (!next.odd && !odds[next.node.index]) {
                odds[next.node.index] = true;
                for (Node node : next.node.odds) paths.add(new Jump(node, true));
            }
        }

        // count nodes that reach the end.
        int count = 0;
        for (int i = 0; i < odds.length; i++) count += odds[i] || evens[i] ? 1 : 0;

        return count;
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
