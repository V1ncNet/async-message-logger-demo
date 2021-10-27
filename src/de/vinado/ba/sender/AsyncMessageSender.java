package de.vinado.ba.sender;

import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author Vincent Nadoll
 */
public class AsyncMessageSender extends MessageSenderDecorator {

    private final AsyncMessageSenderProperties properties;
    private final BlockingQueue<Batch> queue;
    private final AtomicLong delay;
    private final AtomicBoolean busy;

    AsyncMessageSender(ExecutorService threadPool,
                       AsyncMessageSenderProperties properties,
                       MessageSender delegate) {
        super(delegate);
        this.properties = properties;
        this.queue = new DelayQueue<>();
        this.delay = new AtomicLong();
        this.busy = new AtomicBoolean(false);

        startConsumer(threadPool, delegate);
    }

    /**
     * Creates and executes the {@link BatchConsumer}.
     *
     * @param threadPool the pool the executor is executed on
     * @param sender     the delegate that sends the actual messages
     */
    private void startConsumer(ExecutorService threadPool, MessageSender sender) {
        BatchConsumer consumer = new BatchConsumer(queue, sender);
        threadPool.execute(consumer);
    }

    @Override
    public void send(String... messages) {
        resetDelay();
        delayUndersized(messages);

        Batch[] batches = createBatches(messages);
        enqueue(batches);
    }

    /**
     * Compares array and chunk size to determine if an additional delay is necessary to ensure enqueue and dequeue
     * doesn't happen in the same instance.
     *
     * @param messages to be compared with the configured chunk size
     */
    private void delayUndersized(String[] messages) {
        int length = messages.length;
        int chunkSize = properties.getChunkSize();
        if (length <= chunkSize && !busy.get()) {
            delay.getAndAdd(properties.getCooldownMillis());
            busy.set(true);
        }
    }

    /**
     * Resets the global delay and busy flag in case the queue is empty.
     */
    private void resetDelay() {
        if (queue.isEmpty()) {
            delay.set(System.currentTimeMillis());
            busy.set(false);
        }
    }

    /**
     * Partitions and creates delayed batches of messages.
     *
     * @param messages the array to be partitioned and batched
     * @return an array of delayed batches
     */
    private Batch[] createBatches(String[] messages) {
        int cooldownMillis = properties.getCooldownMillis();
        return partition(messages, properties.getChunkSize())
                .map(strings -> new Batch(strings, delay.getAndAdd(cooldownMillis)))
                .toArray(Batch[]::new);
    }

    /**
     * Partitions the given array to a stream of arrays with the given size. The last partition may be smaller than the
     * given chunk size in case the given array does not contain enough elements.
     * <p/>
     * I can't find the Stackoverflow source I got this solution from.
     *
     * @param original  the array to be partitioned
     * @param chunkSize the amount of elements partitions can contain at max
     * @return a stream of partitioned string arrays
     */
    private static Stream<String[]> partition(String[] original, int chunkSize) {
        return IntStream.iterate(0, i -> i + chunkSize)
                .limit((long) Math.ceil((double) original.length / chunkSize))
                .mapToObj(i -> Arrays.copyOfRange(original, i, Math.min(i + chunkSize, original.length)));
    }

    /**
     * Puts each batch to the queue.
     *
     * @param batches the batches to be added to the queue
     */
    private void enqueue(Batch[] batches) {
        for (Batch batch : batches) {
            try {
                System.out.printf("[%s] Enqueueing %s%n", Thread.currentThread().getName(), batch);
                queue.put(batch);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
