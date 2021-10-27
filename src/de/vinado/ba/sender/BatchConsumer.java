package de.vinado.ba.sender;

import java.util.concurrent.BlockingQueue;

/**
 * An executable consumer which takes batches from a queue and sends messages using the given sender.
 *
 * @author Vincent Nadoll
 */
class BatchConsumer implements Runnable {

    private final BlockingQueue<Batch> queue;
    private final MessageSender sender;

    public BatchConsumer(BlockingQueue<Batch> queue, MessageSender sender) {
        this.queue = queue;
        this.sender = sender;
    }

    /**
     * Dequeues batches and sends batched messages via the given {@link MessageSender} indefinitely, unless an {@link
     * InterruptedException} occurs.
     */
    @Override
    public void run() {
        while (true) {
            try {
                Batch batch = queue.take();
                System.out.printf("[%s] Dequeueing %s%n", Thread.currentThread().getName(), batch);
                sender.send(batch.getMessages());
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
