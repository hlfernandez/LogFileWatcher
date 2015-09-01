package es.uvigo.ei.sing.hlfernandez.logfilewatcher.listener;

import java.util.EventListener;

public interface FileListener extends EventListener {
	public void onFileCreation(FileEvent event);
	public void onFileRemove(FileEvent event);
	public void onLogFileModification(FileEvent event);
}
