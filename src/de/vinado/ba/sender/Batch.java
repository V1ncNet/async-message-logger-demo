package de.vinado.ba.sender;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.StringJoiner;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * A {@link Delayed} implementation encapsulating a subset of messages to be consumed by a {@link BatchConsumer}. A
 * {@link java.util.concurrent.DelayQueue} controls the timing on which a batch is consumed.
 *
 * @author Vincent Nadoll
 */
class Batch implements Delayed {

    private final String[] messages;
    private final long time;

    public Batch(String[] messages, long epochMillis) {
        this.messages = messages;
        this.time = epochMillis;
    }

    public String[] getMessages() {
        return messages;
    }

    /**
     * {@inheritDoc}
     *
     * @see <a href="https://www.baeldung.com/java-delay-queue">Guide to DelayQueue</a>
     */
    @Override
    public long getDelay(TimeUnit unit) {
        long diff = this.time - System.currentTimeMillis();
        return unit.convert(diff, TimeUnit.MILLISECONDS);
    }

    /**
     * {@inheritDoc}
     *
     * @see <a href="https://www.baeldung.com/java-delay-queue">Guide to DelayQueue</a>
     */
    @Override
    public int compareTo(Delayed other) {
        return saturatedCast(this.time - ((Batch) other).time);
    }

    /**
     * Casts the given long value to an integer. The integer will not exceed {@link Integer#MIN_VALUE} or {@link
     * Integer#MAX_VALUE}.
     *
     * @param value any long value
     * @return the same value cast to int if it's in range of the int type; otherwise {@link Integer#MIN_VALUE} if it's
     * too small, {@link Integer#MAX_VALUE} if it's too large
     * @see <a href="https://github.com/google/guava/blob/cd3b4197fb523bf26f9122492915006ad306b3f3/guava/src/com/google/common/primitives/Ints.java#L102">Google
     * Guava Commons</a>
     */
    private static int saturatedCast(long value) {
        if (value > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }
        if (value < Integer.MIN_VALUE) {
            return Integer.MIN_VALUE;
        }
        return (int) value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Batch)) return false;

        Batch batch = (Batch) o;

        if (time != batch.time) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(messages, batch.messages);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(messages);
        result = 31 * result + (int) (time ^ (time >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Batch.class.getSimpleName() + "[", "]")
                .add("messages=" + Arrays.toString(messages))
                .add("time=" + Instant.ofEpochMilli(time).atZone(ZoneId.systemDefault()).toLocalDateTime())
                .toString();
    }
}
