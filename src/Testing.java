import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;


public class Testing {

	public static void main(String[] args) throws IOException {
		/*File file=new File("D:\\testing.txt");
		PrintWriter out=new PrintWriter(file);
		out.println("1st line");
		out.println("1st line");
		out.close(); */
		try {
		    String command = "C:\\Program Files (x86)\\PuTTY\\putty.exe -ssh slc06vze.us.oracle.com -l gisahoo -pw Nuapatana@86";
		   // String command = "C:\\Program Files (x86)\\PuTTY\\plink.exe -ssh slc06vyk.us.oracle.com -P 22 -l gisahoo -pw Nuapatana@86";
		    Runtime r = Runtime.getRuntime ();
		    Process p = r.exec (command);
		    
		    InputStream std = p.getInputStream ();
		    OutputStream out = p.getOutputStream ();
		    InputStream err = p.getErrorStream ();
		    p.wait(3000);
		    System.out.println("putty");
		    out.write("y\n".getBytes());
		    out.write ("ls -l\n".getBytes ());
		    out.flush ();

		    Thread.sleep (1000);

		    int value = 0;
		    if (std.available () > 0) {
		        System.out.println ("STD:");
		        value = std.read ();
		        System.out.print ((char) value);

		        while (std.available () > 0) {
		            value = std.read ();
		            System.out.print ((char) value);
		        }
		    }

		    if (err.available () > 0) {
		        System.out.println ("ERR:");
		        value = err.read ();
		        System.out.print ((char) value);

		        while (err.available () > 0) {
		            value = err.read ();
		            System.out.print ((char) value);
		        }
		    }

		    //p.destroy ();
		}
		catch (Exception e) {
		    e.printStackTrace ();
		}
	}
}
	
	

