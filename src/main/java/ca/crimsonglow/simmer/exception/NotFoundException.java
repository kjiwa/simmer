package ca.crimsonglow.simmer.exception;

public class NotFoundException extends Throwable
{
	public NotFoundException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public NotFoundException(String message)
	{
		super(message);
	}
}
