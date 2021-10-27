package de.vinado.ba.sender;

/**
 * a simple factory interface for decorating {@link MessageSender}s.
 *
 * @author Vincent Nadoll
 * @see MessageSenderDecorator
 */
@FunctionalInterface
public interface MessageSenderDecoratorFactory {

    /**
     * Decorates the given delegate with a new {@link MessageSender}.
     *
     * @param delegate the sender to be decorated
     * @return a new {@link MessageSender} instance
     */
    MessageSender decorate(MessageSender delegate);
}
