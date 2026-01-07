import java.time.Instant;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

class Callable3A {

    public int sumOfNaturalNumbers(int n) {
        int sum = 0;
        for (int i = 1; i <= n; i++) {
            sum += i;
        }
        return sum;
    }

    public static void main(String[] args) {

        Callable3A callable3A = new Callable3A();
        Callable<Integer> task = () -> callable3A.sumOfNaturalNumbers(64);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Integer> future = executor.submit(task);
        try {
            Integer result = future.get();
            System.out.println("Sum of first 64 natural numbers is: " + result);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();        
        } finally {
            executor.shutdown();
        }
    }
}