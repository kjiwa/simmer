package ca.crimsonglow.simmer.exception;

/**
 * An exception representing that a bad request was initiated by the client.
 */
public class BadRequestException extends Throwable {
  private static final long serialVersionUID = 1L;

  /**
   * Creates a new BadRequest exception.
   * 
   * @param message
   *          The exception message.
   * @param cause
   *          The exception that caused this exception to be raised.
   */
  public BadRequestException(String message, Throwable cause) {
    super(message, cause);
  }
}
