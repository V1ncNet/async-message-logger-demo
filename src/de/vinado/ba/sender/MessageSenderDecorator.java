package de.vinado.ba.sender;

/**
 * Simple class implementing the decorator pattern for {@link MessageSender}.
 *
 * @author Vincent Nadoll
 */
public abstract class MessageSenderDecorator implements MessageSender {

    private final MessageSender delegate;

    public MessageSenderDecorator(MessageSender delegate) {
        this.delegate = delegate;
    }

    /**
     * Delegates the messages to the delegate.
     *
     * @param messages the messages to be sent
     */
    @Override
    public void send(String... messages) {
        delegate.send(messages);
    }
}
