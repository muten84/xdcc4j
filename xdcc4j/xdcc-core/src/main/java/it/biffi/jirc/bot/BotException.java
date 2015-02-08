package it.biffi.jirc.bot;

public class BotException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8957378429747823851L;

	public BotException(String message, Exception e) {
		super(message, new Exception(e));
	}

}
