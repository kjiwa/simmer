package ca.crimsonglow.simmer.exception;

public class BadRequestException extends Throwable
{
	public BadRequestException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
