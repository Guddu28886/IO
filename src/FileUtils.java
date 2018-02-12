package dpk_mos;
import static dpk_mos.VariablesUtils.*;
import static dpk_mos.EngineUtils.*;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import javax.swing.JOptionPane;

public class FileUtils {

	static List<String> tempLine=new ArrayList<String>();
	//static Boolean transferBySFTP=false;
	static double folderSize_Win=0;
	static BigDecimal bdSize_Win;
	//static Boolean changeAppFolderName=false;

	protected static void createDirectory (String path) throws Exception{
		File file = new File(path);
		if (!file.exists()) {
			if (file.mkdirs()) return;
		}
		return;
	}

	protected static void createFile(String filePath,String fileName, String command) throws IOException, InterruptedException{
		batFile=new File(filePath+"\\"+fileName);
		FileOutputStream fos=new FileOutputStream(batFile);
		DataOutputStream dos=new DataOutputStream(fos);
		dos.writeBytes("");
		dos.writeBytes(command);
		dos.close();
	}

	public static void fileReader(String filePath,String logFile) throws Exception {
		System.out.println("Relevant file is: "+filePath+"\\"+logFile);
		File file=new File(filePath+"\\"+logFile);
		Thread.sleep(6000);
		if(!(file.exists())){
			System.out.println("SAT_FAILED: Relevant File Does Not Exist At " +filePath+" Hence Exiting System");
			System.exit(1);
		}
		FileReader readLog = new FileReader(filePath+"\\"+logFile);
		System.out.println("Log is: "+ readLog.toString());
		String line=null;
		try {
			LineNumberReader lnr = new LineNumberReader(readLog);
			while (true){
				if(success) return;
				if(failure) return;
				line = lnr.readLine();					
				//                    System.out.println("Line is: "+ line);
				if (line == null) {
					System.out.println("FU59 waiting...");
					Thread.sleep(30*1000);
					continue;
				}
				else if((line.contains("Access denied"))||(line.contains("Timed out")) || (line.contains("FATAL ERROR"))||(line.contains("Upload location /ds2/dpk does not have enough free space"))){
					System.out.println("SAT_FAILED: Error FU 64: Please check the logs");
					FileUtils.deleteFiles();
					optionPane = new JOptionPane("Error: FU 64: Please check the logs",JOptionPane.ERROR_MESSAGE);
					dialog = optionPane.createDialog("Error!!");
					dialog.setAlwaysOnTop(true); 
					dialog.setVisible(true);
					FileUtils.deleteFile(mosFilePath,"transfer.bat");
					System.exit(1);
				}
				tempLine.add(line);
				//                    System.out.println("line is: "+line+" LogFile is: "+logFile);
				continuousFileReader(line,logFile);	
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private static void continuousFileReader(String line,String logFile) throws Exception
	{
		//            System.out.println(line + ". LogFile is: "+ logFile + " and type is: " + dpk_type);
		System.out.println(line);
		if(dpk_type.equalsIgnoreCase("windows") && !(logFile.equalsIgnoreCase("FolderSize_Lnx.txt") || logFile.equalsIgnoreCase("DirExists_Lnx.txt"))){
			if(line.contains("MPSA building ended")) {
				success=true;
				System.out.println("SAT_SUCCEEDED");
				return;
			}
			else if(line.contains("Mountain Daylight Time ptutils.py      ERROR")) {
				success=false;
				failure=true;;
				System.out.println("SAT_FAILED");
				return;
			}
		}
		if(line.contains("Using username")){
			for(int i=tempLine.size()-2;i>=0;i--){
				String hostName=hostname_Lnx.substring(0, hostname_Lnx.indexOf("."));
				if(!(tempLine.get(i).contains(hostName))){
					if(logFile.equals("FolderSize_Lnx.txt")){
						if(tempLine.get(i).contains("/ds2")) {
							/*if(tempLine.get(i).contains("No such file or directory")) {
								changeAppFolderName=true;
								DPK_MoSUtils.foldersize_Lnx= DPK_MoSUtils.getFolderSize_Lnx(mosFilePath,"MoS");
								break;
							}*/
							//else {
							folderSize_Win=Double.parseDouble((tempLine.get(i).substring(0, tempLine.get(i).indexOf("/"))).trim());
							folderSize_Win=folderSize_Win/1024/1024/1024;
							bdSize_Win = new BigDecimal(folderSize_Win);
							bdSize_Win = bdSize_Win.setScale(1, BigDecimal.ROUND_DOWN);
							success=true;
							//	FileUtils.transferBySFTP=false;
							return;	
						}
					}
					if(tempLine.get(i).contains("MPSA building ended")){
						success=true;
						break;
					}
					else if(tempLine.get(--i).contains("MOS upload process ended")){
						success=true;
						break;
					}
					else {
						success=false;
						failure=true;;
						break;
					}
				}
			}
		}
	}

	public static void writeProperties() throws Exception{
		input.close();
		FileOutputStream output=new FileOutputStream(f);
		prop.setProperty("encryptedPassword_Lnx", encryptedPassword_Lnx);
		prop.setProperty("password_Lnx", "*****");
		/*prop.setProperty("encryptedPassword_Win", encryptedPassword_Win);
		prop.setProperty("password_Win", "*****");*/
		prop.setProperty("encryptedEmailPassword", encryptedEmailPassword);
		prop.setProperty("emailPassword", "*****");
		prop.store(output, null);
		output.close();
	}

	public static void deleteFile(String filePath,String fileName){
		File fileToDelete=new File(filePath+"\\"+fileName);
		try{
			if(fileToDelete.exists()){
				fileToDelete.delete();
				Thread.sleep(2000);
			}
		}
		catch(Exception e){
			System.out.println("File does not exist");
		}
	}

	protected static double getFolderSize_Win(File directory) {
		double length = 0;
		BigDecimal bdSize = null;
		try {
			for (File file : directory.listFiles()) {
				if (file.isFile())
					length += file.length();
			}
			double size=length/1024/1024/1024;

			bdSize = new BigDecimal(size);
			bdSize = bdSize.setScale(1, BigDecimal.ROUND_DOWN);		
		}
		catch(Exception e){			
		}
		return bdSize.doubleValue();
	}

	protected static void deleteFiles() throws Exception {
		deleteFile(dpkFilePath,"winDPKCmd.bat");
		deleteFile(dpkFilePath, "connect.bat");
		deleteFile(dpkFilePath, "run.txt");
		deleteFile(dpkFilePath,"CreateLnxDir.txt");
		deleteFile(dpkFilePath,"LnxDirCreatelog.txt");
		deleteFile(dpkFilePath,"CreateLnxDir.bat");
		deleteFile(mosFilePath,"FolderSize.bat");
		deleteFile(mosFilePath,"FolderSize_Lnx.txt");
		deleteFile(mosFilePath, "FolderSize.txt");
		deleteFile(mosFilePath, "CreateLnxDir.txt");
		deleteFile(mosFilePath, "CreateLnxDir.bat");
		deleteFile(mosFilePath, "LnxDirCreatelog.txt");
		deleteFile(mosFilePath, "connect.bat");
		deleteFile(mosFilePath, "run.txt");	
	}

	protected static void renameFiles(String path) throws InterruptedException{
		for(int i=1;i<=11;i++) {			
			Thread.sleep(1000);
			File oldFileName=new File(DPK_MoSUtils.windowsSharedPath+"//ova//"+VariablesUtils.mosFileName+"-OVA_"+i+"of15.zip");
			File newFileName=new File(DPK_MoSUtils.windowsSharedPath+"//ova//"+VariablesUtils.mosFileName+"-OVA_"+i+"of11.zip");																			  						
			oldFileName.renameTo(newFileName);
		}
	}

	protected static void deleteZipFiles(String dirName,boolean delUpgImg) {
		File imageDir = new File(dirName);
		File[] fList = imageDir.listFiles();
		if(app_build.contains("GLD")){
			if(delUpgImg){
				for (File file : fList) {
					if (file.isDirectory() && file.getAbsolutePath().contains("UPG") ){
						deleteZipFiles(file.getAbsolutePath(),true);
					}
					if (file.isFile()) {
						String str= file.toString();
						if(str.endsWith(".zip")){	
							System.out.println(file.getAbsolutePath());
							//file.delete();
							System.out.println("DPK files deleted");
						}
					} 
				}
			}
			else{
				for (File file : fList) {
					if (file.isDirectory() && file.getAbsolutePath().contains("UPD") ){
						deleteZipFiles(file.getAbsolutePath(),false);
					}
					if (file.isFile()) {
						String str= file.toString();
						if(str.endsWith(".zip")){	
							System.out.println(file.getAbsolutePath());
							//file.delete();
							System.out.println("DPK files deleted");
						}
					} 
				}
			}
		}
		else{
			for (File file : fList) {
				if (file.isDirectory() ){
					deleteZipFiles(file.getAbsolutePath(),false);
				}
				if (file.isFile()) {
					String str= file.toString();
					if(str.endsWith(".zip")){	
						System.out.println(file.getAbsolutePath());
						//file.delete();
						System.out.println("DPK files deleted");
					}
				} 
			}
		}
	}	

	protected static void getImageToDel(File image,String imgNum, boolean upgImgChk) {
		if(app_build.contains("R1") || dpk_ToolsRel.equalsIgnoreCase("y"))
		{
			FileUtils.deleteZipFiles(image.toString(),false);
		}
		else {
			File[] imgTypedir=image.listFiles();
			int dirSize=0,updCount=0;
			while( dirSize<imgTypedir.length){
				if(upgImgChk){
					if(imgTypedir[dirSize].toString().contains("UPG"))
						upgCount++;
				}
				else{
					if(imgTypedir[dirSize].toString().contains("UPG"))
						upgCount++;
					else if(imgTypedir[dirSize].toString().contains("UPD"))
						updCount++;
				}
				dirSize++;
			}
			if(upgCount>1) {
				FileUtils.deleteZipFiles(image.toString(),true);
			}	
			if(VariablesUtils.app_build.contains("GLD")) {
				if(Integer.parseInt(imgNum)==imageToDel){
					if(updCount>=1)
						FileUtils.deleteZipFiles(image.toString(),false);
				}
			}
			else{
				if(!image.equals(firstImageFile))
					if(updCount>=1)
						FileUtils.deleteZipFiles(image.toString(),false);
			}
		}
	}

	protected static File getpreviousImage(File path ) {
		int i=1,count=0;
		File[] dir=path.listFiles();
		Arrays.sort(dir, new Comparator<File>() {
			public int compare(File f1, File f2) {
				return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified()); } });
		while(i>=0 && i<dir.length){
			if(dpk_ToolsRel.equalsIgnoreCase("y")){
				if((dir[(dir.length-i)].toString().contains("PT855")) && !(dir[(dir.length-i)].toString().contains(tools_version.toUpperCase()))){		
					count++;
					if(count==1) break;		
				}
				else
					i++;
			}
			else if((dir[(dir.length-i)].toString().contains("R1") && !(dir[(dir.length-i)].toString().contains(app_build)))){				
				count++;
				if(count==1) break;
			}
			else i++;
		}
		return dir[(dir.length-i)];
	}
}
