package es.uvigo.ei.sing.hlfernandez.logfilewatcher;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

import es.uvigo.ei.sing.hlfernandez.logfilewatcher.listener.FileEvent;
import es.uvigo.ei.sing.hlfernandez.logfilewatcher.listener.FileListener;

public class LogFileWatcher extends FileWatcher {
	
	private long lastLengh = 0;
	private RandomAccessFile randomAccessFile;
	
	public LogFileWatcher(File file) {
		super(file);
	}
	
	@Override
	protected void logFileCreated() {
		lastLengh = 0;
		super.logFileCreated();
	}
	
	public File getFile() {
		return this.file;
	}
	
	public synchronized List<String> getNewLines() throws IOException {
		List<String> newLines = new LinkedList<String>();
		if(this.file.lastModified() > 0 ){
			if(lastLengh < this.file.length()){
				randomAccessFile = new RandomAccessFile(this.file, "r");
				randomAccessFile.seek(lastLengh);
				String line;
				while ((line = randomAccessFile.readLine()) != null) {
					newLines.add(line);
				}
				this.lastLengh = this.file.length();
			}
		}
		return newLines;
	}
	
	public static void main(String[] args) 
		throws IOException, InterruptedException 
	{
		final Path path = FileSystems.getDefault().getPath(
			System.getProperty("user.home"));
		LogFileWatcher fw = new LogFileWatcher(
			new File(path.toFile(), "file.txt")
		);
		fw.start();
		System.err.println("[Log file watcher started]");
		fw.addLogFileWatcherListener(new FileListener() {
			
			@Override
			public void onLogFileModification(FileEvent event) {
				try {
					fw.getNewLines().stream().forEach(System.err::println);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public void onFileCreation(FileEvent event) {
				try {
					fw.getNewLines().stream().forEach(System.err::println);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFileRemove(FileEvent event) {
				System.err.println("[Log file has been deleted]");
			}
		});
	}
}
