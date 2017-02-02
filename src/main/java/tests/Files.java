package tests;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Files {

	public static void main(String[] args) {
		File folder = new File("config/");
		System.out.println("files number: " + folder.getAbsolutePath());
		File[] files = folder.listFiles();
		System.out.println("files number: " + files.length);
		String pack = null;
		for(int idx = 0; idx < files.length; idx++){
			System.out.println("file #" + idx + ": " + files[idx].getName());
			BufferedReader br;
			try {
				br = new BufferedReader(new FileReader(files[idx]));
				pack = br.readLine();
				pack = pack.substring(pack.indexOf('=') + 1, pack.length());
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			System.out.println("Class full name: " + pack);
		}
	}

}
