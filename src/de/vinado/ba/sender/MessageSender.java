package de.vinado.ba.sender;

/**
 * A mock for Spring's JavaMailSender interface.
 *
 * @author Vincent Nadoll
 */
public interface MessageSender {

    void send(String... messages);
}
