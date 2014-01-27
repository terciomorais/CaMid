package br.ufpe.cin.middleware.app.statistics;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

public class StatFileWriter {
	
	private File file;
	
	private PrintStream ps;
	
	private AtomicLong counter;
	
	private final static String SEPARATOR = " ";
	
	private ExecutorService executor;
	
	static class WriteTask implements Runnable{

		private PrintStream ps;
		private String str;
		
		public WriteTask(PrintStream ps, String str)
		{
			this.ps = ps;
			this.str = str;
		}
		
		@Override
		public void run() {
			ps.println(str);
			ps.flush();
		}
		
	}
	
	public StatFileWriter(File file) throws IOException
	{
		this.file = file;
		long lines = -1;
		if(!file.exists())
		{
			file.createNewFile();
			lines = 0;
		}
		lines = lines == 0 ? 0 : countLines();
		
		counter = new AtomicLong(lines);
		initializeStream();
		executor = Executors.newSingleThreadExecutor();
	}

	private void initializeStream() throws FileNotFoundException {
		FileOutputStream fos = new FileOutputStream(file,true);
		ps = new PrintStream(fos);
		
		if(counter.get() == 0)
		{
			ps.printf("\"duration\"%s\"workload\"%s\"delay\"%s\"success\"\n",SEPARATOR, SEPARATOR, SEPARATOR);
		}
	}

	private long countLines() {
		Scanner scanner = null;
		long lines = 0;
		try {
			scanner = new Scanner(file);
			while(scanner.hasNext())
			{
				scanner.nextLine();
				lines++;
			}
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
		return lines-1;
	}

	public void write(long duration, boolean success, int workload, long delay )
	{
		long line = counter.incrementAndGet();
		String str = String.format("\"%d\"%s%d%s\"%d\"%s\"%d\"%s%s", 
				line, 
				SEPARATOR, duration, 
				SEPARATOR, workload,
				SEPARATOR, delay,
				SEPARATOR, Boolean.toString(success).toUpperCase());
		
		executor.execute(new WriteTask(ps, str)); 
	}
	
	public void close()
	{
		ps.close();
		executor.shutdown();
	}
	
	

}
