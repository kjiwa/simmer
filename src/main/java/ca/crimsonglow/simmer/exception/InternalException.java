package ca.crimsonglow.simmer.exception;

/**
 * An exception representing that an internal error occurred.
 */
public class InternalException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  /**
   * Creates a new Internal exception.
   * 
   * @param message
   *          The exception message.
   * @param cause
   *          The exception that caused this exception to be raised.
   */
  public InternalException(String message, Throwable cause) {
    super(message, cause);
  }
}
