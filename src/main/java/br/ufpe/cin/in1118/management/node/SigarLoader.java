package br.ufpe.cin.in1118.management.node;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.hyperic.sigar.Sigar;

public class SigarLoader {
	private static final String SIGAR_PATH = "libs/sigar/";
	private static final String[] LIBS = new String [] {
		"sigar-amd64-winnt.dll",
		"libsigar-sparc64-solaris.so",
		"libsigar-amd64-linux.so",
		"libsigar-sparc-solaris.so",
		"libsigar-amd64-solaris.so",
		"libsigar-x86-freebsd-5.so",
		"libsigar-x86-freebsd-6.so",
		"libsigar-x86-solaris.so",
		"libsigar-x86-linux.so",
		"libsigar-ia64-linux.so",
		"libsigar-ppc64-aix-5.so",
		"libsigar-ppc64-linux.so",
		"libsigar-ppc-aix-5.so",
		"libsigar-ppc-linux.so",
		"libsigar-s390x-linux.so",
		"libsigar-amd64-freebsd-6.so",
		"sigar-x86-winnt.dll"
	};

	private File tmpDir;
	private Sigar sigar;
	private static SigarLoader INSTANCE;

	public synchronized static SigarLoader getInstance(){
		if(INSTANCE == null){
			INSTANCE = new SigarLoader();
		}
		return INSTANCE; 
	}

	private SigarLoader(){
		String tmpParent	= System.getProperty("java.io.tmpdir");
		String osName		= System.getProperty("os.name").toLowerCase();
		
		char sep = (osName.contains("windows") || osName.contains("winnt")) ? ';' : ':';

		tmpDir = new File(tmpParent, "middleware/sigar-" + UUID.randomUUID().toString());
		tmpDir.mkdirs();
		this.extractToDir();
		System.setProperty("java.library.path", tmpDir.getAbsolutePath()
													+ sep
													+ System.getProperty("java.library.path"));
		this.loadLibs();
		this.markForDelete();
		sigar = new Sigar();
	}

	private void markForDelete(){
		this.deleteDir(tmpDir);
		tmpDir.getParentFile().deleteOnExit();
	}

	private void deleteDir(File dir) {
		for(File file : dir.listFiles()) {
			if(file.isFile()) {
				file.deleteOnExit();
			}
			if(file.isDirectory()) {
				deleteDir(file);
			}
		}
		dir.deleteOnExit();
	}

	private void extractToDir() {
		for(String libname : LIBS) {
			if(this.matchOsArch(libname)) {
				InputStream is = null;
				FileOutputStream fos = null;

				try {
					FileUtils.copyFileToDirectory(new File(SIGAR_PATH + libname), this.tmpDir);
				}
				catch(IOException e) {
					e.printStackTrace();
				}
				finally	{
					if(is != null) {
						try {
							is.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					if(fos != null)	{
						try {
							fos.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				break;
			}
		}
	}

	private void loadLibs(){
		for(String libname : LIBS){
			if(this.matchOsArch(libname)){
				File libfile = new File(tmpDir, libname);
				try{
					Runtime.getRuntime().load(libfile.getAbsolutePath());
					//break;
				}
				catch(Throwable t){
					//					t.printStackTrace();
				}
			}
		}

	}

	private boolean matchOsArch(String libname){
		boolean flag = false;
		String osName = (System.getProperty("os.name").toLowerCase().contains("windows")) ? "winnt" : System.getProperty("os.name").toLowerCase();
		String archName	= System.getProperty("os.arch").toLowerCase();
		if( libname.contains(osName)&& libname.contains(archName))
			flag = true;
		return flag;
	}

	public Sigar getSigar(){
		return this.sigar;
	}
}
