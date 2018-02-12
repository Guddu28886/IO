import java.io.InputStream;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;


public class LinuxConnect {

	/**
	 * JSch Example Tutorial
	 * Java SSH Connection Program
	 */
	public static void main(String[] args) {
	    String host="slc06vyk.us.oracle.com";
	    String user="gisahoo";
	    String password="Nuapatana@86";
	    String backupFileName=null;    
	    String constantPath="/apps_autofs/recovery/ENTERPRISE/opsdba/";
	    String backupFilePath=null;
		String activity="PS91";
		String build_Id="C436-R1";
		String build_Num=build_Id.substring(0, 4);
		String [] allbackupfilenames;
		
		System.out.println(build_Num);
		if (activity.equalsIgnoreCase("HR92"))
	    	backupFilePath=constantPath+"hrms/hrms92/zip/hc920mst*";
		else if(activity.equalsIgnoreCase("FSCM92"))
			backupFilePath=constantPath+"fscm/fscm92/zip/ep920mst*";
		else if(activity.equalsIgnoreCase("CRM92"))
			backupFilePath=constantPath+"crm/crm92/zip/cr920mst*";
		else if(activity.equalsIgnoreCase("ELS92"))
			backupFilePath=constantPath+"els/els92/zip/lm920mst*";
		else if(activity.equalsIgnoreCase("CS92"))
			backupFilePath=constantPath+"cs/cs92/zip/cs920mst*";
		else if(activity.equalsIgnoreCase("PS91"))
			backupFilePath=constantPath+"ps/ps91/zip/pa910mst*";
	    String command1="find "+backupFilePath+" -type f -mtime -3 | sort -r";
	    System.out.println(command1);
	    
	    
	    //String command1="find /apps_autofs/recovery/ENTERPRISE/opsdba/hrms/hrms92/zip/hc920mst* -type f -mtime -1";
	    try{
	    	
	    	java.util.Properties config = new java.util.Properties(); 
	    	config.put("StrictHostKeyChecking", "no");
	    	JSch jsch = new JSch();
	    	Session session=jsch.getSession(user, host, 22);
	    	session.setPassword(password);
	    	session.setConfig(config);
	    	session.connect();
	    	System.out.println("Connected to: "+host);
	    	Channel channel=session.openChannel("exec");
	        ((ChannelExec)channel).setCommand(command1);
	        channel.setInputStream(null);
	        InputStream in=channel.getInputStream();
	        channel.connect();
	    		    	
	       //((ChannelExec)channel).setErrStream(System.err);
	        System.out.println(System.err);
	        
	        
	        //String bckupFileName;
	        byte[] tmp=new byte[1024];
	        while(true)
	        {
	          while(in.available()>0)
	          {
	        	//System.out.println("exit-status1: "+channel.getExitStatus());  
	            int i=in.read(tmp, 0, 1024);
	            if(i<0)break;
	            String backupFileNames=new String(tmp, 0, i);
	            backupFileName=backupFileNames;
	            //System.out.print(new String(tmp, 0, i));
	            //System.out.println("exit-status2: "+channel.getExitStatus());
	          }
	          
	          if(channel.isEOF())
	          {
	            //System.out.println("exit-status: "+channel.getExitStatus());
	            break;
	          }
	          try{Thread.sleep(1000);}catch(Exception ee){}
	        }
	        //System.out.println("Back up file name : "+backupFileName);
	        channel.disconnect();
	        session.disconnect();
	        //System.out.println("DONE");
	        
	        
	        		
	       if (backupFileName.contains("pa910mst")&backupFileName.contains("C436"))
	        {
	        	allbackupfilenames=backupFileName.split("\n");
	        //for(int i=0;i<allbackupfilenames.length;i++)
	        	System.out.println(allbackupfilenames[0]);
	        	System.out.println("ok");
	        }
	        else if(backupFileName.contains("No such file or directory"))
	        {
	        	System.out.println("wrong command");
	        } 
	    }
	    catch(JSchException jsch)
	    {
	    	System.out.println("Failed:Linux User Id & Password is wrong, please check in SAT properties files....");
	    	
	    }
	    catch(Exception e){
	    	e.printStackTrace();
	    }

	}

}