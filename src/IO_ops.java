import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.*;
public class IO_ops {

	public static String getCurrentDateTime(String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        Date date = new Date();
        return dateFormat.format(date);
    } 
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		//Scanner s=new Scanner(System.in);
		/*System.out.println("Enter a number:-");
		int a=s.nextInt();
		System.out.println("The num is : "+a);
		
		System.out.println("Enter a string :");
		String b=s.next();
		System.out.println("The string :"+b);
		
		System.out.println("Enter a string :");
		String c=s.nextLine();
		System.out.println("The string :"+c);
		
		System.out.println("Enter a string :");
		char d=s.next().charAt(4);
		System.out.println("The string :"+d);
		
		File fo=new File("C:/Users/gisahoo/Desktop/selenium");
		fo.mkdir(); */
		System.out.println(getCurrentDateTime("dd-MM-yyyy HH_mm_ss"));
		System.out.println(getCurrentDateTime("yyyyMMddHHmm"));
		//System.out.println("Test"+".txt");
		FileWriter FW = new FileWriter("D:\\Guddu.txt",true);
		BufferedWriter BW = new BufferedWriter(FW);
		BW.write("1st line \n");
		BW.write("2nd line \n");
		BW.write("3rd error.Stop here \n");
		BW.write("4th line \n");
		BW.close();
		BufferedReader in = new BufferedReader(new FileReader("D:\\Guddu.txt"));
        String str;
        
        while ((str = in.readLine()) != null) 
        {
        	if (str.contains("Error")||str.contains("Failed")||str.contains("error")||str.contains("failed"))
        		{
        		System.out.println(str);
        		
						InputStream inStream = null;
		            	OutputStream outStream = null;
		        		
		            	try{
		            		
		            	    File afile =new File("D:\\Guddu.txt");
		            	    File bfile =new File("D:\\folderB\\Afile.error.201703021242.Validated");
		            		
		            	    inStream = new FileInputStream(afile);
		            	    outStream = new FileOutputStream(bfile);
		                	
		            	    byte[] buffer = new byte[1024];
		            		
		            	    int length;
		            	    //copy the file content in bytes 
		            	    while ((length = inStream.read(buffer)) > 0){
		            	  
		            	    	outStream.write(buffer, 0, length);
		            	 
		            	    }
		            	    
		            	    inStream.close();
		            	    outStream.close();
		            	    in.close();
		            	    //delete the original file
		            	    System.out.println(afile.delete());
		            	    
		            	    System.out.println("File is copied successful!");
            	    
			            	}
		            	catch(IOException e)
			            	{
			            	    e.printStackTrace();						
			            	}
        		
        		System.exit(1);
        		}
        }
        
		
	}

}
