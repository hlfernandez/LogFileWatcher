package es.uvigo.ei.sing.hlfernandez.logfilewatcher.listener;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class FileListenerList {

	private final CopyOnWriteArrayList<FileListener> listeners;
	
	/**
	 * Constructs a new instance of {@link EntityChangeListenerList}.
	 */
	public FileListenerList() {
		this.listeners = new CopyOnWriteArrayList<FileListener>();
	}


	/**
	 * Adds a new change listener to this list
	 * 
	 * @param listener the new change listener.
	 * @return {@code true} if the listener was effectively added to this
	 * listeners list. {@code false} otherwise. Note that the same listener can
	 * not be added twice as listener. 
	 */
	public boolean add(FileListener listener) {
		return listeners.addIfAbsent(listener);
	}
	
	/**
	 * Lists the change listeners that belong to this list.
	 * @return a lists with the change listeners that belong to this list.
	 */
	public List<? extends FileListener> list() {
		return Collections.unmodifiableList(this.listeners);
	}

	/**
	 * Checks if the provided change listener belongs to this list.
	 * 
	 * @param listener the listener to be checked.
	 * @return {@code true} if the listener belongs to this list. {@code false}
	 * otherwise.
	 */
	public boolean contains(FileListener listener) {
		return listeners.contains(listener);
	}

	/**
	 * Removes a change listener from this list.
	 * 
	 * @param listener the listener to be removed.
	 * @return {@code true} if the listener was effectively removed from this
	 * list. {@code false} otherwise.
	 */
	public boolean remove(FileListener listener) {
		return listeners.remove(listener);
	}

	/**
	 * Clears this list by removing all the listeners.
	 */
	public void clear() {
		this.listeners.clear();
	}

	/**
	 * Notifies all the listeners of this list with the provided event.
	 * 
	 * @param event the event that will be passed to every listener of this
	 * list.
	 */
	public void onLogFileCreation(FileEvent event) {
		this.listeners.forEach(l -> l.onFileCreation(event));
	}

	/**
	 * Notifies all the listeners of this list with the provided event.
	 * 
	 * @param event the event that will be passed to every listener of this
	 * list.
	 */
	public void onLogFileRemove(FileEvent event) {
		this.listeners.forEach(l -> l.onFileRemove(event));
	}

	/**
	 * Notifies all the listeners of this list with the provided event.
	 * 
	 * @param event the event that will be passed to every listener of this
	 * list.
	 */
	public void onLogFileModification(FileEvent event) {
		this.listeners.forEach(l -> l.onLogFileModification(event));
	}
}
