
public class ExceptionHandling {

	public static void main(String[] args) 
	{
		try
		{
			int i=10/0;
		}
		catch(Exception e)
		{
			System.out.println("Exception1");
		}
		try
		{
			int j=10/0;
		}
		catch(Exception e)
		{
			System.out.println("Exception2");
		}

	}

}
