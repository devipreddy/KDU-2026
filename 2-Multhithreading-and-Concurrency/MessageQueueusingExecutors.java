import java.time.Instant;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

class MessageQueueusingExecutors{

    private String[] messages;
    private int capacity;
    private int size;
    private int front;
    private int rear;

    public MessageQueueusingExecutors(int capacity) {
        this.capacity = capacity;
        this.messages = new String[capacity];
        this.size = 0;
        this.front = 0;
        this.rear = 0;
    }

    ReentrantLock lock = new ReentrantLock();
    Condition notFull = lock.newCondition();
    Condition notEmpty = lock.newCondition();

    public void put(String message) throws InterruptedException {
        lock.lock();
        try {
            while (size == capacity) {
                notFull.await();
            }
            messages[rear] = message;
            rear = (rear + 1) % capacity;
            size++;
            notEmpty.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public String get() throws InterruptedException {
        lock.lock();
        try {
            while (size == 0) {
                notEmpty.await();
            }
            String message = messages[front];
            front = (front + 1) % capacity;
            size--;
            notFull.signalAll();   // <-- FIXED
            return message;
        } finally {
            lock.unlock();
        }
    }

    Runnable MessageSender = () -> {
        try {
            for (int i = 1; i <= 5; i++) {
                String message = "Message " + i + " at " + Instant.now();
                put(message);
                System.out.println("Sent: " + message);
                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    };

    Runnable MessageReceiver = () -> {
        try {
            for (int i = 1; i <= 5; i++) {
                String message = get();
                System.out.println("Received: " + message);
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    };

    public static void main(String[] args) {

        MessageQueueusingExecutors queue = new MessageQueueusingExecutors(3);

        ExecutorService executor = Executors.newFixedThreadPool(3);
        ExecutorService executor2 = Executors.newFixedThreadPool(3);

        executor.submit(queue.MessageSender);
        executor.submit(queue.MessageSender);
        executor.submit(queue.MessageSender);

        executor2.submit(queue.MessageReceiver);
        executor2.submit(queue.MessageReceiver);
        executor2.submit(queue.MessageReceiver);

        executor.shutdown();
        executor2.shutdown();

        try {
            executor.awaitTermination(1, java.util.concurrent.TimeUnit.MINUTES);
            executor2.awaitTermination(1, java.util.concurrent.TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("All tasks finished.");
    }
}
