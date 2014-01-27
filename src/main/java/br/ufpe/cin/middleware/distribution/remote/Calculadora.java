package br.ufpe.cin.middleware.distribution.remote;

public class Calculadora {

	public static final String SERVICE_NAME = "Calculadora";
	
//	private static AtomicInteger counter = new AtomicInteger(0);
	
	public int soma(int a, int b)
	{
//		Random random = new Random();
//		if(true || random.nextInt(1000) < 10)
//			throw new RuntimeException("Excecao Aleatoria");
		
		return a + b;
	}
	
	public int sub(int a, int b)
	{
		return a - b;
	}
	
	
//	public String execute(ObjectDescriptor od) {
//		String methodName = od.getMethod().getValue();
//		List<Parameter> params = od.getParameters();
//
//		String r ="";
//		if (methodName.equals("soma")){
//			r = String.valueOf(this.soma(Integer.parseInt(params.get(0).getValue()), 
//					Integer.parseInt(params.get(1).getValue())));
//		}
//		
//		if( counter.getAndIncrement() % 1000 == 100 )
//		{
//			throw new RuntimeException("RandomException");
//		}
//		
//		System.out.println("O resultado no objeto remoto: " + r);
//		return r;
//	}

}
