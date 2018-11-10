import java.util.LinkedList;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Thread safe producer - consumer pattern where consumer does not keep up with producer
 * (due to different sleep time) thus the queue must be limited by maxItems
 */
public class SimpleProducerConsumer implements ProducerConsumer {

  private final LinkedList<String> queue = new LinkedList<>();
  private final int maxItems;
  private final Consumer<String> consumer = s -> System.out.println("Consuming " + s);
  private final Function<Integer, String> producer = s -> "element " + s;


  public SimpleProducerConsumer() {
    this.maxItems = 1;
  }

  public SimpleProducerConsumer(int maxItems) {
    if (maxItems < 1) {
      throw new IllegalArgumentException("Must be positive number");
    }
    this.maxItems = maxItems;
  }


  @Override
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

  @Override
  public void produce() throws InterruptedException {
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
