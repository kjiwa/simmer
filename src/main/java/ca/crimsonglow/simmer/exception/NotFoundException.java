package ca.crimsonglow.simmer.exception;

/**
 * An exception representing that an entity could not be found.
 */
public class NotFoundException extends Throwable {
  private static final long serialVersionUID = 1L;

  /**
   * Creates a new NotFound exception.
   * 
   * @param message
   *          The exception message.
   * @param cause
   *          The exception that caused this exception to be raised.
   */
  public NotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Creates a new NotFound exception.
   * 
   * @param message
   *          The exception message.
   */
  public NotFoundException(String message) {
    super(message);
  }
}
