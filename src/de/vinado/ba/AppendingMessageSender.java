package de.vinado.ba;

import de.vinado.ba.sender.MessageSender;

import java.util.Arrays;
import java.util.Observable;

/**
 * An {@link Observable} implementation of {@link MessageSender}.
 *
 * @author Vincent Nadoll
 */
public class AppendingMessageSender extends Observable implements MessageSender {

    private final StringBuilder builder;

    public AppendingMessageSender() {
        this.builder = new StringBuilder();
    }

    /**
     * Appends the given messages to the previous ones and notifies any {@link java.util.Observer} afterwards.
     *
     * @param messages the messages to be appended
     */
    @Override
    public void send(String... messages) {
        append(messages);
        setChanged();
        notifyObservers(builder.toString());
    }

    /**
     * Joins, formats and appends the given array of messages to the internal builder.
     *
     * @param messages the message array to be joined, formatted and appended
     */
    private void append(String[] messages) {
        String joined = Arrays.toString(messages) + "\n";
        builder.append(joined);
    }
}
