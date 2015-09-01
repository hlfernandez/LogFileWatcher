package es.uvigo.ei.sing.hlfernandez.logfilewatcher.listener;

import java.util.EventObject;

public class FileEvent extends EventObject {
	private static final long serialVersionUID = 1L;

	public static enum LogFileAction {
		CREATE,
		MODIFY,
		DELETE
	};

	private LogFileAction action;

	/**
	 * Constructs a {@link LogFileAction} object with the specified source 
	 * component and action type.
	 * 
	 * @param source the object that originated the event.
	 * @param action the action type.
	 */
	public FileEvent(Object source, LogFileAction action) {
		super(source);
		this.action = action;
	}

	/**
	 * Returns an {@link LogFileAction} that represents the action type.
	 * 
	 * @return an {@link LogFileAction} that represents the action type.
	 */
	public LogFileAction getAction() {
		return action;
	}
}