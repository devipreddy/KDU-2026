import java.time.Instant;
class MessageQueue{

    private String[] messages;
    private int capacity;
    private int size;
    private int front;
    private int rear;

    public MessageQueue(int capacity) {
        this.capacity = capacity;
        this.messages = new String[capacity];
        this.size = 0;
        this.front = 0;
        this.rear = 0;
    }

    public synchronized void put(String message) throws InterruptedException {
        while (size == capacity) {
            wait();
        }
        messages[rear] = message;
        rear = (rear + 1) % capacity;
        size++;
        notifyAll();
    }

    public synchronized String get() throws InterruptedException {
        while (size == 0) {
            wait();
        }
        String message = messages[front];
        front = (front + 1) % capacity;
        size--;
        notifyAll();
        return message;
    }

    Runnable MessageSender = () -> {
        try {
            for (int i = 1; i <= 5; i++) {
                String message = "Message " + i + " at " + Instant.now().toString();
                put(message);
                System.out.println("Sent: " + message);
                Thread.sleep(500); // Simulate time taken to produce a message
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
                Thread.sleep(1000); // Simulate time taken to process a message
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    };

    public static void main(String[] args) {
        MessageQueue queue = new MessageQueue(3);

        Thread senderThread = new Thread(queue.MessageSender);
        Thread senderThread2 = new Thread(queue.MessageSender);
        Thread senderThread3 = new Thread(queue.MessageSender);
        Thread receiverThread = new Thread(queue.MessageReceiver);
        Thread receiverThread2 = new Thread(queue.MessageReceiver);
        Thread receiverThread3 = new Thread(queue.MessageReceiver);

        senderThread.start();
        senderThread2.start();
        senderThread3.start();  
        receiverThread.start();
        receiverThread2.start();
        receiverThread3.start();

        try {
            senderThread.join();
            senderThread2.join();
            senderThread3.join();
            receiverThread2.join();
            receiverThread3.join();
            receiverThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}