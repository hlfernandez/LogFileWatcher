package es.uvigo.ei.sing.hlfernandez.logfilewatcher.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import es.uvigo.ei.sing.hlfernandez.filechooser.JFileChooserPanel;
import es.uvigo.ei.sing.hlfernandez.logfilewatcher.LogFileWatcher;
import es.uvigo.ei.sing.hlfernandez.logfilewatcher.listener.FileEvent;
import es.uvigo.ei.sing.hlfernandez.logfilewatcher.listener.FileListener;

public class LogFileWatcherPanel extends JPanel implements FileListener {
	private static final long serialVersionUID = 1L;
	private LogFileWatcher fileWatcher;
	private JTextArea textArea;
	private boolean fixTextAreaScroll = false;
	private JPanel buttonsPane;
	private JFileChooserPanel fileChooser;
	
	public LogFileWatcherPanel(File file) {
		super();
		updateLogFileWatcher(file);
		init();
		this.fileWatcher.start();
		textArea.select(0, 0);
	}

	private void updateLogFileWatcher(File file) {
		if(this.fileWatcher != null) {
			this.fileWatcher.stop();
			this.fileWatcher.removeLogFileWatcherListener(this);
		}
		this.fileWatcher = new LogFileWatcher(file);
		this.fileWatcher.addLogFileWatcherListener(this);
	}

	private void init() {
		this.setLayout(new BorderLayout());
		this.add(getNorthPane(), BorderLayout.NORTH);
		this.add(new JScrollPane(getTextArea()), BorderLayout.CENTER);
	}

	private JPanel getNorthPane() {
		if(this.buttonsPane == null) {
			this.buttonsPane = new JPanel(new BorderLayout());
			this.buttonsPane.add(getFileChooserPanel(), BorderLayout.CENTER);
		}
		return buttonsPane;
	}

	private Component getFileChooserPanel() {
		fileChooser = new JFileChooserPanel(JFileChooserPanel.Mode.OPEN);
		fileChooser.getComponentFileName().setText(
			this.fileWatcher.getFile().getAbsolutePath());
		fileChooser.getComponentFileName().getDocument().addDocumentListener(
				new DocumentListener() {
				
				@Override
				public void removeUpdate(DocumentEvent e) {}
				
				@Override
				public void insertUpdate(DocumentEvent e) {
					selectedFileChanged();
				}
				
				@Override
				public void changedUpdate(DocumentEvent e) {}
			});
		return fileChooser;
	}

	protected void selectedFileChanged() {
		updateLogFileWatcher(fileChooser.getSelectedFile());
		askForLogClearing();
		appendNewLinesToTextArea();
		this.fileWatcher.start();
	}

	private void askForLogClearing() {
		if(shouldClearLog()) {
			this.textArea.setText("");
		}
	}

	private boolean shouldClearLog() {
		int option = JOptionPane.showConfirmDialog(
			this, "Do you want to clear the log?", "Clear log", 
			JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE
		);
		
		return option == JOptionPane.YES_OPTION;
	}

	private Component getTextArea() {
		if (textArea == null) {
			textArea = new JTextArea();
			textArea.setEditable(false);
			textArea.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));
			appendNewLinesToTextArea();
		}
		return textArea;
	}

	private void appendNewLinesToTextArea() {
		try {
			textArea.append(getNewFileText());
			if(!fixTextAreaScroll) {
				textArea.setCaretPosition(textArea.getText().length());
			}
		} catch (IOException e) {
		}
	}

	private String getNewFileText() throws IOException {
		StringBuilder sb = new StringBuilder();
		this.fileWatcher.getNewLines().stream().forEach(
			l -> sb.append(l).append("\n")
		);
		return sb.toString();
	}

	@Override
	public void onFileCreation(FileEvent event) {
		textArea.append("[" + getTimestamp()  + "]" + "\tFile created\n");
		appendNewLinesToTextArea();
	}

	@Override
	public void onFileRemove(FileEvent event) {
		textArea.append("[" + getTimestamp()+ "]" + "\tFile deleted\n");
		askForLogClearing();
	}

	private Timestamp getTimestamp() {
		return new Timestamp(Calendar.getInstance().getTime().getTime());
	}

	@Override
	public void onLogFileModification(FileEvent event) {
		appendNewLinesToTextArea();
	}
	
	@Override
	public void finalize() {
		if (this.fileWatcher != null) {
			this.fileWatcher.stop();
		}
	}

}
