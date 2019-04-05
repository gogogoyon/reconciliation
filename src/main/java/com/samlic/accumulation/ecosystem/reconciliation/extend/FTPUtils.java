package com.samlic.accumulation.ecosystem.reconciliation.extend;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

/**
 * Ftp tool.
 */
public class FTPUtils {

	private FTPClient ftpClient;
	
	private String url;			// 服务器地址
	private String port; 		// 服务器端口
	private String username; 	// 用户登录名
	private String password; 	// 用户登录密码
	private String remotePath;	// 文件保存路径
	
	/**
	 * 初始化FTPUtils
	 * @param url
	 * @param port
	 * @param username	
	 * @param remotePath
	 */
	public FTPUtils(String url, String port, String username, String password, String remotePath){
	    this.url = url;
	    this.port = port;
	    this.password = password;
	    this.username = username;
	    this.remotePath = remotePath;
	    this.ftpClient = new FTPClient();
	}

	/**
	 * 连接（配置通用连接属性）至服务器
	 * 
	 * @param serverName
	 *            服务器名称
	 * @param remotePath
	 *            当前访问目录
	 * @return <b>true</b>：连接成功 <br/>
	 *         <b>false</b>：连接失败
	 * @throws IOException 
	 */
	private boolean connectToTheServer() throws IOException {
		// 定义返回值
		boolean result = false;
		
		// 连接至服务器，端口默认为21时，可直接通过URL连接
		ftpClient.connect(url, Integer.parseInt(port));
		// 登录服务器
		ftpClient.login(username, password);
		
		ftpClient.enterLocalActiveMode();
		
		// 判断返回码是否合法
		if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
			// 不合法时断开连接
			ftpClient.disconnect();
			// 结束程序
			return result;
		}
		// 设置文件操作目录
		result = ftpClient.changeWorkingDirectory(remotePath);
		// 设置文件类型，二进制
		result = ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
		// 设置缓冲区大小
		ftpClient.setBufferSize(3072);
		// 设置字符编码
		ftpClient.setControlEncoding("UTF-8");
		
		return result;
	}
	
	public boolean makeDirectory(String remotePath,String pathname ) throws IOException{
		boolean result = false;
		try {
			// 连接至服务器
			result = connectToTheServer();
			// 判断服务器是否连接成功
			if (result) {
				result = ftpClient.changeWorkingDirectory(remotePath);
				String[] names =  ftpClient.listNames();
				for(int i = 0 ; i < names.length ; i++){
					if(StringUtils.equals(pathname, names[i])){
						return true;
					}
				}
				result = ftpClient.makeDirectory(pathname);
			}		
		}finally {
			// 登出服务器并断开连接
			logout();
		}
		return result;
	}

	/**
	 * 上传文件至FTP服务器
	 * 
	 * @param serverName
	 *            服务器名称
	 * @param storePath
	 *            上传文件存储路径
	 * @param fileName
	 *            上传文件存储名称
	 * @param is
	 *            上传文件输入流
	 * @return <b>true</b>：上传成功 <br/>
	 *         <b>false</b>：上传失败
	 * @throws IOException 
	 */
	public boolean storeFile(String fileName, InputStream is) throws IOException {
		boolean result = false;
		try {
			// 连接至服务器
			result = connectToTheServer();
			// 判断服务器是否连接成功
			if (result) {
				// 上传文件
				result = ftpClient.storeFile(fileName, is);
			}
			// 关闭输入流
			is.close();		
		} finally {
			IOUtils.closeQuietly(is);
			// 登出服务器并断开连接
			logout();
		}
		return result;
	}

	/**
	 * 下载FTP服务器文件至本地<br/>
	 * @throws IOException 
	 */
	public File[] retrieveFiles(String path,String suffix) throws IOException {
		OutputStream out = null;
		InputStream is = null; 	
		List<File> fileList = new ArrayList<File>();
		try {
			boolean result = false;
			// 连接至服务器
			result = connectToTheServer();
			// 判断服务器是否连接成功
			if (result) {
				// 获取文件输入流
				FTPFile[] ff = ftpClient.listFiles();				
				for(FTPFile file : ff){
					String fileName = file.getName();
					File localFile =  new File(path+fileName);
					if(fileName.endsWith(suffix)){						
						is = ftpClient.retrieveFileStream(fileName);
					    out = new FileOutputStream(localFile);
						int len = 0;
						byte[] b = new byte[1024];
						while((len=is.read(b))!=-1){
							out.write(b, 0, len);
						}						
						out.close();						
					
						ftpClient.deleteFile(fileName);
						fileList.add(localFile);
					}
				}		
			}		
			
			return fileList.toArray(new File[0]);
		} finally {
			IOUtils.closeQuietly(is);
			IOUtils.closeQuietly(out);
			// 登出服务器并断开连接
			logout();
		}
	}
	
	public File retrieveFile(String path, String fileName) throws IOException {
		OutputStream out = null;
		InputStream is = null; 	
		File file = new File(path+fileName);
		try {
			boolean result = false;
			// 连接至服务器
			result = connectToTheServer();
			// 判断服务器是否连接成功
			if (result) {									
				is = ftpClient.retrieveFileStream(fileName);
			    out = new FileOutputStream(file);
				int len = 0;
				byte[] b = new byte[1024];
				while((len=is.read(b))!=-1){
					out.write(b, 0, len);
				}						
				out.close();						
			
				ftpClient.deleteFile(fileName);					
			}
			
			return file;
		} finally {
			IOUtils.closeQuietly(is);
			IOUtils.closeQuietly(out);
			// 登出服务器并断开连接
			logout();
		}
	}

	/**
	 * 删除FTP服务器文件
	 * 
	 * @param serverName
	 *            服务器名称
	 * @param remotePath
	 *            当前访问目录
	 * @param fileName
	 *            文件存储名称
	 * @return <b>true</b>：删除成功 <br/>
	 *         <b>false</b>：删除失败
	 * @throws IOException 
	 */
	public boolean deleteFile(String fileName) throws IOException {
		boolean result = false;
		// 连接至服务器
		result = connectToTheServer();
		// 判断服务器是否连接成功
		if (result) {
			try {
				// 删除文件
				result = ftpClient.deleteFile(fileName);			
			} finally {
				// 登出服务器并断开连接
				logout();
			}
		}
		return result;
	}

	/**
	 * 检测FTP服务器文件是否存在
	 * 
	 * @param serverName
	 *            服务器名称
	 * @param remotePath
	 *            检测文件存储路径
	 * @param fileName
	 *            检测文件存储名称
	 * @return <b>true</b>：文件存在 <br/>
	 *         <b>false</b>：文件不存在
	 * @throws IOException 
	 */
	public boolean checkFile(String fileName) throws IOException {
		boolean result = false;
		try {
			// 连接至服务器
			result = connectToTheServer();
			// 判断服务器是否连接成功
			if (result) {
				// 默认文件不存在
				result = false;
				// 获取文件操作目录下所有文件名称
				String[] remoteNames = ftpClient.listNames();
				// 循环比对文件名称，判断是否含有当前要下载的文件名
				for (String remoteName : remoteNames) {
					if (fileName.equals(remoteName)) {
						result = true;
					}
				}
			}	
		} finally {
			// 登出服务器并断开连接
			logout();
		}
		return result;
	}
	/**
     * 移动FTP服务器文件
     * 
     * @param serverName
     *            服务器名称
     * @param oldfileName
     *            旧文件名及路径(aa/bb.txt)
     * @param newfileName
     *            新文件名及路径(cc/bb.txt)
     * @return <b>true</b>：移动成功 <br/>
     *         <b>false</b>：移动失败
     * oldfileName和newfileName
	 * @throws IOException 
     */
    public boolean moveFile(String oldfileName,String newfileName) throws IOException {
        boolean result = false;
        // 连接至服务器
        result = connectToTheServer();
        // 判断服务器是否连接成功
        if (result) {
            try {
                // 移动文件
                result = ftpClient.rename(oldfileName, newfileName);
            } finally {
                logout();
            }
        }
        return result;
    }
    

	/**
	 * 登出服务器并断开连接
	 * 
	 * @param ftp
	 *            FTPClient对象实例
	 * @return <b>true</b>：操作成功 <br/>
	 *         <b>false</b>：操作失败
	 */
    private boolean logout() {
		boolean result = false;
		if (null != ftpClient) {
			try {
				// 登出服务器
				result = ftpClient.logout();
			} catch (IOException e) {
				
			} finally {
				// 判断连接是否存在
				if (ftpClient.isConnected()) {
					try {
						// 断开连接
						ftpClient.disconnect();
					} catch (IOException e) {
						
					}
				}
			}
		}
		return result;
	}
}
