import java.util.Random;
import java.util.stream.IntStream;
import java.util.List;
import java.util.stream.Collectors;

class Callable3B{
    public static void main (String[] args) {

        long starttime, endtime, start2, end2;

        Random random = new Random();

        int count = 1000000;

        IntStream randomInts = random.ints(count,1, 345);
        List<Integer> list = randomInts.boxed().collect(Collectors.toList());

        starttime = System.currentTimeMillis();

        int sum1 = list.stream().mapToInt(Integer::intValue).sum();

        endtime = System.currentTimeMillis();

        System.out.println("Sum of random integers(Sequentially): " + sum1);

        System.out.println("Time taken in sequential stream: " + (endtime - starttime) + " ms");

        start2 = System.currentTimeMillis();

        int sum = list.parallelStream().mapToInt(Integer::intValue).sum();

        end2 = System.currentTimeMillis();
        System.out.println("Sum of random integers(Parallely): " + sum);
        System.out.println("Time taken in parallel stream: " + (end2 - start2) + " ms");
        System.out.println("Hello from Callable3B");
    }
}