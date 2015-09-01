package es.uvigo.ei.sing.hlfernandez.logfilewatcher.gui;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;

import javax.swing.JFrame;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

public class Launcher {
	
	public static void main(String[] args) 
	throws URISyntaxException, FileNotFoundException 
	{
		CommandLineValues values = parseArgs(args);
		    
		JFrame frame = new JFrame("Log file watcher");
		final LogFileWatcherPanel lfPanel = 
			new LogFileWatcherPanel(values.getLogFile());
		frame.add(lfPanel);
		frame.setMinimumSize(new Dimension(600, 600));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				lfPanel.finalize();
			}
		});
		frame.setVisible(true);      
	}

	private static CommandLineValues parseArgs(String[] args) {
		CommandLineValues values = new CommandLineValues(args);
	    CmdLineParser parser = new CmdLineParser(values);

	    try {
	        parser.parseArgument(args);
	    } catch (CmdLineException e) {
	        System.exit(1);
	    }
	    return values;
	}
}
