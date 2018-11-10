public class Main {

  public static void main(String[] args) throws InterruptedException {

    ProducerConsumer pc = new SimpleProducerConsumer(5);

    Thread consumerT = new Thread(() -> {
      try {
        pc.consume();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    });

    Thread producerT = new Thread(() -> {
      try {
        pc.produce();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    });

    producerT.start();
    consumerT.start();

    producerT.join();
    consumerT.join();
  }

}
