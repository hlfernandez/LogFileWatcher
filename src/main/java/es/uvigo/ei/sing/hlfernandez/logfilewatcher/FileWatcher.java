package es.uvigo.ei.sing.hlfernandez.logfilewatcher;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.logging.Logger;

import es.uvigo.ei.sing.hlfernandez.logfilewatcher.listener.FileEvent;
import es.uvigo.ei.sing.hlfernandez.logfilewatcher.listener.FileListener;
import es.uvigo.ei.sing.hlfernandez.logfilewatcher.listener.FileListenerList;

public class FileWatcher {
	
	protected File file;
	private final FileListenerList listeners;
	private Thread watcherThread;

	public FileWatcher(File file) {
		this.file = file;
		this.listeners = new FileListenerList();
	}
	
	public void start() {
		watcherThread = new Thread(() -> {
			try (
				final WatchService watchService = 
					FileSystems.getDefault().newWatchService()
			) {
				file.getParentFile().toPath().register(
			    	watchService, 
			    	StandardWatchEventKinds.ENTRY_CREATE, 
			    	StandardWatchEventKinds.ENTRY_MODIFY,
			    	StandardWatchEventKinds.ENTRY_DELETE
			    );
				while (true) {
					final WatchKey wk = watchService.take();
			        for (WatchEvent<?> event : wk.pollEvents()) {
						final Path changed = (Path) event.context();
						if (changed.endsWith(this.file.getName())) {
				        	Kind<?> kind = event.kind();
				        	parseWatchEvent(kind);
						}
			        }
	
			        boolean valid = wk.reset();
			        if (!valid) {
			        	Logger.getLogger(this.getClass().getName())
			        		.warning("Key has been unregistered");
			        }
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch(InterruptedException e) {
				
			}
		});
		watcherThread.start();
	}

	private void parseWatchEvent(Kind<?> kind) {
		if(kind.equals(StandardWatchEventKinds.ENTRY_MODIFY)) {
			logFileModified();
		} else if(kind.equals(StandardWatchEventKinds.ENTRY_CREATE)) {
			logFileCreated();
		} else if(kind.equals(StandardWatchEventKinds.ENTRY_DELETE)) {
			logFileDeleted();
		}
	}
	
	public void stop() {
		if(watcherThread != null) {
			watcherThread.interrupt();
		}
	}
	
	protected void logFileCreated() {
		this.listeners.onLogFileCreation(new FileEvent(this,
				FileEvent.LogFileAction.CREATE));
	}

	protected void logFileDeleted() {
		this.listeners.onLogFileRemove(new FileEvent(this,
				FileEvent.LogFileAction.DELETE));
	}

	protected void logFileModified() {
		this.listeners.onLogFileModification(new FileEvent(this,
				FileEvent.LogFileAction.MODIFY));
	}

	public void addLogFileWatcherListener(FileListener listener) {
		this.listeners.add(listener);
	}

	public boolean containsLogFileWatcherListener(FileListener listener) {
		return this.listeners.contains(listener);
	}

	public boolean removeLogFileWatcherListener(FileListener listener) {
		return this.listeners.remove(listener);
	}
	
	public void clearListeners() {
		listeners.clear();
	}
	
	public static void main(String[] args) 
		throws IOException, InterruptedException 
	{
		final Path path = FileSystems.getDefault().getPath(
			System.getProperty("user.home"));
		FileWatcher fw = new FileWatcher(new File(path.toFile(), "file.txt"));
		fw.start();
		System.err.println("File watcher started");
		fw.addLogFileWatcherListener(new FileListener() {
			
			@Override
			public void onLogFileModification(FileEvent event) {
				System.err.println("File modified");
			}
			
			@Override
			public void onFileCreation(FileEvent event) {
				System.err.println("File created");
			}

			@Override
			public void onFileRemove(FileEvent event) {
				System.err.println("File deleted");
				fw.stop();
			}
		});
	}
}
