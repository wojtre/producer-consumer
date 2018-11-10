import java.util.LinkedList;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Thread safe producer - consumer pattern where consumer does not keep up with producer
 * (due to different sleep time) thus the queue must be limited by maxItems
 */
public class ProducerConsumer {

  private LinkedList<String> queue = new LinkedList<>();
  private int maxItems = 1;
  private Consumer<String> consumer = s -> System.out.println("Consuming " + s);
  private Function<Integer, String> producer = s -> "element " + s;


  public ProducerConsumer() {
  }

  public ProducerConsumer(int maxItems) {
    if (maxItems < 1) {
      throw new IllegalArgumentException("Must be positive number");
    }
    this.maxItems = maxItems;
  }


  public void consume() throws InterruptedException {
    while (true) {
      synchronized (this) {
        while (queue.size() <= 0) {
          wait();
        }
        consumer.accept(queue.removeFirst());
        notify();
      }
      Thread.sleep(1000);
    }
  }

  public void producer() throws InterruptedException {
    int productNumber = 0;
    while (true) {
      synchronized (this) {
        while (queue.size() == maxItems) {
          wait();
        }
        System.out.println("Producing element " + productNumber);
        queue.add(producer.apply(productNumber++));
        notify();
      }
      Thread.sleep(500);
    }
  }
}
