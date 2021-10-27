package de.vinado.ba.sender;

/**
 * POJO containing properties for configuring the {@link AsyncMessageSender}.
 *
 * @author Vincent Nadoll
 */
public class AsyncMessageSenderProperties {

    private int chunkSize;
    private int cooldownMillis;

    public int getChunkSize() {
        return chunkSize;
    }

    public void setChunkSize(int chunkSize) {
        this.chunkSize = chunkSize;
    }

    public int getCooldownMillis() {
        return cooldownMillis;
    }

    public void setCooldownMillis(int cooldownMillis) {
        this.cooldownMillis = cooldownMillis;
    }
}
