import java.util.concurrent.Semaphore;

/***
 * Consumer - producer without queue, communication through semaphores
 */
public class SemaphoreProducerConsumer implements ProducerConsumer {

  private final Semaphore producer = new Semaphore(1);
  private final Semaphore consumer = new Semaphore(0);

  @Override
  public void consume() throws InterruptedException {
    while (true) {
      consumer.acquire();
          System.out.println("consuming");
      producer.release();
      Thread.sleep(1000);
    }

  }

  @Override
  public void produce() throws InterruptedException {
    while (true) {
      producer.acquire();
          System.out.println("producing");
      consumer.release();
      Thread.sleep(500);
    }
  }

  public static void main(String[] args) throws InterruptedException {
    ProducerConsumer spc = new SemaphoreProducerConsumer();

    Thread prodT = new Thread(() -> {
      try {
        spc.produce();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    });

    Thread consT = new Thread(() -> {
      try {
        spc.consume();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    });
    prodT.start();
    consT.start();
    prodT.join();
    consT.join();
  }
}
