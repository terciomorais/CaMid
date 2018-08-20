package br.ufpe.cin.middleware.app.statistics;

import java.util.Iterator;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

import br.ufpe.cin.camid.distribution.stub.DelayStub;

public class JMeterStatisticsClient extends AbstractJavaSamplerClient{

	public static String SERVICE_TIME_PARAMETER = "ServiceTime";
	
	private int port;
	private String host;
	private long delay;
	
	public static final long DEFAULT_DELAY = 100;
	public static final String DEFAULT_HOST = "127.0.0.1";
	public static final int DEFAULT_PORT = 5000;
	
	public JMeterStatisticsClient()
	{
		super();
		getLogger().debug(whoAmI());
	}
	
	private static JMeterLoader loader = null;
	private DelayStub delayService;
	
	
	private synchronized JMeterLoader getLoader(String host, int port)
	{
		if(loader == null)
		{
			loader = new JMeterLoader(host, port);
		}
		return loader;
	}
	
	@Override
	public SampleResult runTest(JavaSamplerContext samplerContext) {
		SampleResult result = new SampleResult();
		
		try
		{
			result.sampleStart();
			delayService.delay(delay);
			result.setSuccessful(true);
		}
		catch(Exception e)
		{
//			e.printStackTrace();
			result.setSuccessful(false);
		}
		finally
		{
			result.sampleEnd();
		}
		
		return result;
	}

	private String whoAmI() {
        StringBuilder sb = new StringBuilder();
        sb.append(Thread.currentThread().toString());
        sb.append("@");
        sb.append(Integer.toHexString(hashCode()));
        return sb.toString();
    }
	
	@Override
    public Arguments getDefaultParameters() {
        Arguments params = new Arguments();
        params.addArgument("delay", String.valueOf(DEFAULT_DELAY));
        params.addArgument("host", "127.0.0.1");
        params.addArgument("port", "5000");
        return params;
    }
	
	public void setupTest(JavaSamplerContext context) {
        getLogger().debug(whoAmI() + "\tsetupTest()");
        listParameters(context);

        host = context.getParameter("host", DEFAULT_HOST);
        port = context.getIntParameter("port", DEFAULT_PORT);
        delay = context.getLongParameter("delay", DEFAULT_DELAY);
        
        JMeterLoader loader = getLoader(host, port);
        delayService = loader.createStub();
    }
	
	@Override
    public void teardownTest(JavaSamplerContext context) {
        getLogger().debug(whoAmI() + "\tteardownTest()");
//        listParameters(context);
    }
	
	private void listParameters(JavaSamplerContext context) {
        if (getLogger().isDebugEnabled()) {
            Iterator<String> argsIt = context.getParameterNamesIterator();
            while (argsIt.hasNext()) {
                String name = argsIt.next();
                getLogger().debug(name + "=" + context.getParameter(name));
            }
        }
    }
}
