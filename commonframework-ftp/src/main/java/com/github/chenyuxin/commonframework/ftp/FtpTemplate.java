package com.github.chenyuxin.commonframework.ftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.pool2.impl.GenericObjectPool;

import lombok.extern.slf4j.Slf4j;

/**
 * Ftp连接使用工具
 */
@Slf4j
public class FtpTemplate {
	
	/**
	 * 需要使用的ftp客户端集合
	 */
	private Map<String,GenericObjectPool<FTPClient>> ftpPool;
	
	private String defualtFtp; //FtpName
	
	public FtpTemplate(Map<String, GenericObjectPool<FTPClient>> ftpMap,String defualtFtpName) {
		this.ftpPool = ftpMap;
		if (null != defualtFtpName && !"".equals(defualtFtpName)
				&& null != ftpMap.get(defualtFtpName)
				) {
			this.defualtFtp = defualtFtpName;
		} else {
			if (ftpMap.size() > 0) {
				this.defualtFtp = String.valueOf(ftpMap.keySet().toArray()[0]);
			}
		}
	}
	
	/**
	 * 删除文件
	 * @param pathFile 要删除文件的路径
	 * @param options 配置 可选 (ftpSourceName ftp源名称,使用非默认ftp源)
	 */
	public boolean deleteFile(String pathFile,Object... options) {
		String ftpSourceName = defualtFtp;
		for(Object option : options) {
			if (option instanceof String ftpSourceNameO) {
				ftpSourceName = ftpSourceNameO;
			}
		}
		
		FTPClient ftpClient = null;
		try {
			ftpClient = ftpPool.get(ftpSourceName).borrowObject();
			return ftpClient.deleteFile(pathFile);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return false;
		} finally {
			if (null != ftpClient) {
				if (ftpClient.getReplyCode()>299) {
					log.warn(ftpSourceName + " ftp deleteFile:" + ftpClient.getReplyString());
				}
				ftpPool.get(ftpSourceName).returnObject(ftpClient);
			}
		}
	}
	
	/**
	 * 下载文件<br>
	 * @param pathFile ftp路径和文件名
	 * @param options 配置 [ ftpSourceName ftp源名称,使用非默认ftp源 (可选) <br>
	 *             	   		downloadFile 下载File类型的文件与outputStream只能二选一,同时存在首选outputStream <br>
	 *             	   		outputStream 下载OutputStream流式文件与downloadFile只能二选一,同时存在首选outputStream <br>
	 *                	  ]
	 * @return boolean
	 */
	public boolean downloadFile(String pathFile,Object... options) {
		String ftpSourceName = defualtFtp;
		OutputStream outputStreamA = null;
		for(Object option : options) {
			if (option instanceof String ftpSourceNameO) {
				ftpSourceName = ftpSourceNameO;
			} else if (option instanceof File file) {
				try {
					outputStreamA = new FileOutputStream(file);
				} catch (FileNotFoundException e) {
					log.error(e.getMessage(),e);
				}
			} else if (option instanceof OutputStream outputStreamO) {
				outputStreamA = outputStreamO;
			} 
		}
		
		FTPClient ftpClient = null;
		try ( OutputStream outputStream = outputStreamA ) {
			ftpClient = ftpPool.get(ftpSourceName).borrowObject();
		
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			return ftpClient.retrieveFile(pathFile, outputStream);
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return false;
		} finally {
			if (null != ftpClient) {
				if (ftpClient.getReplyCode()>299) {
					log.warn(ftpSourceName + " ftp downloadFile:" + ftpClient.getReplyString());
				}
				ftpPool.get(ftpSourceName).returnObject(ftpClient);
			}
		}	
	}
	
	/**
	 * 上传文件<br>
	 * 如果目录文件不存在，则先创建目录文件
	 * @param pathFile ftp路径和文件名
	 * @param options 配置 [ ftpSourceName ftp源名称,使用非默认ftp源 (可选) <br>
	 * 	  					originfile 上传File类型的文件与inputStream只能二选一,同时存在首选inputStream <br>
	 * 	  					inputStream 上传InputStream流式文件与originfile只能二选一,同时存在首选inputStream <br>
	 * 	  				  ]
	 * @return boolean
	 */
	public boolean uploadFile(String pathFile, Object... options) {
		String ftpSourceName = defualtFtp;
		InputStream inputStreamA = null;
		for(Object option : options) {
			if (option instanceof String ftpSourceNameO) {
				ftpSourceName = ftpSourceNameO;
			} else if (option instanceof File file) {
				try {
					inputStreamA = new FileInputStream(file);
				} catch (FileNotFoundException e) {
					log.error(e.getMessage(),e);
				}
			} else if (option instanceof InputStream inputStreamO) {
				inputStreamA = inputStreamO;
			} 
		}
		
		FTPClient ftpClient = null;
		try ( InputStream inputStream = inputStreamA ) {
			ftpClient = ftpPool.get(ftpSourceName).borrowObject();
			
			int endIndex = pathFile.lastIndexOf("/");
			String path = -1 == endIndex?pathFile:pathFile.substring(0,endIndex);
			if (ftpClient.getSystemType().toLowerCase().contains("windows")) {
				path = new String(path.getBytes("GBK"),"GBK");
				pathFile = new String(pathFile.getBytes("GBK"),"GBK");
			}
			makeDirectory(ftpClient,path);//创建多层目录文件
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			return ftpClient.storeFile(pathFile, inputStream);
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return false;
		} finally {
			if (null != ftpClient) {
				if (ftpClient.getReplyCode()>299) {
					log.warn(ftpSourceName + " ftp uploadFile:" + ftpClient.getReplyString());
				}
				ftpPool.get(ftpSourceName).returnObject(ftpClient);
			}
		}
	}
	
	/**
	 * 文件或目录是否已存在<br>
	 * 判断路径是否存在子项，或文件是否存在。
	 * @param path
	 * @param options 配置 可选 [ ftpSourceName ftp源名称,使用非默认ftp源 ]
	 * @return boolean
	 */
	public boolean existFile(String path,Object... options) {
		String ftpSourceName = defualtFtp;
		for(Object option : options) {
			if (option instanceof String ftpSourceNameO) {
				ftpSourceName = ftpSourceNameO;
			}
		}
		
		FTPClient ftpClient = null;
		try {
			ftpClient = ftpPool.get(ftpSourceName).borrowObject();
			return existFile(ftpClient,path);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return false;
		} finally {
			if (null != ftpClient) {
				ftpPool.get(ftpSourceName).returnObject(ftpClient);
			}
		}	
	}
	
	/**
	 * 创建目录<br>
	 * 创建多层目录文件，如果有ftp服务器已存在该目录，则不创建，如果无，则创建
	 * @param path Creates a new subdirectory on the FTP server in the current directory (if a relative pathname is given) or where specified (if an absolute pathname isgiven).
	 * @param options 配置 可选 [ ftpSourceName ftp源名称,使用非默认ftp源 ]
	 */
	public boolean makeDirectory(String path,Object... options) {
		String ftpSourceName = defualtFtp;
		for(Object option : options) {
			if (option instanceof String ftpSourceNameO) {
				ftpSourceName = ftpSourceNameO;
			}
		}
		
		FTPClient ftpClient = null;
		try {
			ftpClient = ftpPool.get(ftpSourceName).borrowObject();
			return makeDirectory(ftpClient,path);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return false;
		} finally {
			if (null != ftpClient) {
				ftpPool.get(ftpSourceName).returnObject(ftpClient);
			}
		}
	}

	
	/**
	 * 内部方法,传入的指定客户端创建目录<br>
	 * 创建多层目录文件，如果有ftp服务器已存在该目录，则不创建，如果无，则创建
	 * @param ftpClient ftp客户端
	 * @param path Creates a new subdirectory on the FTP server in the current directory (if a relative pathname is given) or where specified (if an absolute pathname isgiven).
	 * @throws IOException 
	 */
	private boolean makeDirectory(FTPClient ftpClient, String path) throws IOException {
		String currentPath;
		int fromIndex = 0,endIndex = 0;
		do {
			endIndex = path.indexOf("/",fromIndex) + 1;
			currentPath = path.substring(0, endIndex);
			if (!existFile(ftpClient, currentPath)) {
				ftpClient.makeDirectory(currentPath);
			}
			fromIndex = endIndex;
		} while (endIndex > 0); 
		
		return ftpClient.makeDirectory(path);
	}
	
	/**
	 * 内部方法,判断路径是否存在子项，或文件是否存在。
	 * @param ftpClient ftp客户端
	 * @param path 路径
	 * @return
	 * @throws IOException
	 */
	private boolean existFile(FTPClient ftpClient,String path) throws IOException {
        FTPFile[] ftpFileArr = ftpClient.listFiles(path);
        return ftpFileArr.length > 0;
	}
	

}
