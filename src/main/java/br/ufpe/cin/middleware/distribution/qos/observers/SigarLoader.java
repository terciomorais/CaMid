package br.ufpe.cin.middleware.distribution.qos.observers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

public class SigarLoader 
{
	private static final String SIGAR_PATH = "/sigar-lib/";
	private static final String[] LIBS = new String [] 
	{
		"libsigar-amd64-freebsd-6.so",
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
		"sigar-amd64-winnt.dll",
		"sigar-x86-winnt.dll"
	};
	
	private File tmpDir;
	
	private String arch;
	private String os;
	private Sigar sigar;
	
	private static SigarLoader INSTANCE;
	
	
	
	public synchronized static SigarLoader getInstance()
	{
		if(INSTANCE == null)
		{
			INSTANCE = new SigarLoader();
		}
		return INSTANCE; 
	}
	
	private SigarLoader()
	{
		String tmp = System.getProperty("java.io.tmpdir");
		tmpDir = new File(tmp, "middleware/sigar-" + UUID.randomUUID().toString());
		tmpDir.mkdirs();
		os = System.getProperty("os.name");
		arch = System.getProperty("os.arch");
		extractToDir();
		loadLibs();
		markForDelete();
		sigar = new Sigar();
	}
	
	
	
	private void markForDelete()
	{
		deleteDir(tmpDir);
		tmpDir.getParentFile().deleteOnExit();
	}
	
	private void deleteDir(File dir)
	{
		for(File file : dir.listFiles())
		{
			if(file.isFile())
			{
				file.deleteOnExit();
			}
			if(file.isDirectory())
			{
				deleteDir(file);
			}
		}
		dir.deleteOnExit();
	}
	
	private void extractToDir()
	{
		byte buffer[] = new byte[512];
		for(String libname : LIBS)
		{
			if(this.matchOsArch(libname))
			{
				InputStream is = null;
				FileOutputStream fos = null;
				try
				{
					is = this.getClass().getResourceAsStream(SIGAR_PATH + libname);
					fos = new FileOutputStream(new File(tmpDir, libname));
					for(int len = is.read(buffer); len >= 0; len = is.read(buffer))
					{
						fos.write(buffer, 0, len);
					}
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
				finally
				{
					if(is != null)
					{
						try {
							is.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					if(fos != null)
					{
						try {
							fos.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				
			}
		}
	}
	
	private void loadLibs()
	{
		for(String libname : LIBS)
		{
			if(this.matchOsArch(libname))
			{
				File libfile = new File(tmpDir, libname);
				try 
				{
					Runtime.getRuntime().load(libfile.getAbsolutePath());
				}
				catch(Throwable t)
				{
//					t.printStackTrace();
				}
			}
		}

	}
	
	private boolean matchOsArch(String libname)
	{
		return true;
	}
	
	public Sigar getSigar()
	{
		return this.sigar;
	}
	
	public static void main(String[] args) {
		System.out.println(System.getProperty("os.name"));
		System.out.println(System.getProperty("os.arch"));
		System.out.println(System.getProperty("os.version"));
		
		SigarLoader loader = SigarLoader.getInstance();
		
		Sigar sigar = loader.getSigar();
		try {
			System.out.println(sigar.getCpuPerc().getIdle());
			System.out.println(sigar.getMem().getFreePercent());
		} catch (SigarException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
