package com.ober.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class UploadFileUtil {
	// 表单字段常量
	private static final String FORM_FIELDS = "from_fileds";
	// 文件域表单常量
	private static final String FILE_FIELDS = "file_fields";

	private static final String TEMP_PATH = "temp";
	// 默认最大文件大小
	private long maxSize = 1000000;
	// 文件的相对路径
	private String basePath = "upload";
	// 文件的目录名
	private String dirName;
	// 若不指定，则文件名默认为yyyyMMddHHmmss_xyz
	private String fileName;
	// 文件保存目录路径
	private String savePath;
	// 文件保存目录url
	private String saveUrl;
	// 文件最终的url包括文件名
	private String fileUrl;
	// 上传临时目录
	private String tempPath;

	public UploadFileUtil(String dirName) {
		this.dirName = dirName;
	}

	/**
	 * 上传
	 * 
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> upload(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		// 获取验证信息
		Map<String, Object> validateInfo = validateFields(request);
		map.put("validateInfo", validateInfo);
		boolean error = (boolean) validateInfo.get("error");
		// 如果验证通过，初始化表单元素
		Map<String, Object> fieldsMap = new HashMap<String, Object>();
		if (!error) {
			fieldsMap = this.initFields(request);
		}
		// 上传
		List<FileItem> fileItems = (List<FileItem>) fieldsMap.get(UploadFileUtil.FILE_FIELDS);
		if (fileItems != null) {
			for (FileItem fileItem : fileItems) {
				Map<String, Object> saveInfo = saveFile(fileItem);
				map.put("saveInfo", saveInfo);
			}
			map.put("savePath", getSavePath());
			map.put("saveUrl", saveUrl);
			map.put("fileUrl", fileUrl);
			map.put("fileName", getFileName());
		}
		return map;
	}

	/**
	 * 上传验证，并初始化文件目录
	 * 
	 * @param request
	 * @return
	 */
	private Map<String, Object> validateFields(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		String errorInfo = "";
		boolean error = true;
		// 获取内容类型
		String contenType = request.getContentType();
		int contentLength = request.getContentLength();
		// 文件保存目录路径
		String savePath = request.getSession().getServletContext().getRealPath("/") + basePath + File.separator;
		// 文件保存的目录URL
		String basePath = getBasePath();
		String saveUrl = basePath + "/";
		File uploadDir = new File(savePath);
		if (contenType == null || !contenType.startsWith("multipart")) {
			System.out.println("请求不包含multipart/form-data流");
			errorInfo = "请求不包含multipart/form-data流";
		} else if (maxSize < contentLength) {
			System.out.println("上传文件大小超出文件最大大小");
			errorInfo = "上传文件大小超出文件最大大小[" + maxSize + "]";
		} else if (!ServletFileUpload.isMultipartContent(request)) {
			errorInfo = "请选择文件";
		} else if (!uploadDir.isDirectory()) {
			errorInfo = "上传目录[" + savePath + "]不存在";
		} else if (!uploadDir.canWrite()) {
			errorInfo = "上传目录[" + savePath + "]没有写权限";
		} else {
			String dirName = getDirName();
			savePath += dirName + File.separator;
			saveUrl += dirName + "/";
			File saveDirFile = new File(savePath);
			if (!saveDirFile.exists()) {
				saveDirFile.mkdirs();
			}
			String ymd = DateUtil.formatDate(new Date(), "yyyyMMdd");
			savePath += ymd + File.separator;
			saveUrl += ymd + "/";
			File dirFile = new File(savePath);
			if (!dirFile.exists()) {
				dirFile.mkdirs();
			}
			// 获取临时上传目录
			String tempPath = request.getSession().getServletContext().getRealPath("/") + basePath + File.separator + UploadFileUtil.TEMP_PATH + File.separator;
			File file = new File(tempPath);
			if (!file.exists()) {
				file.mkdirs();
			}
			error = false;
			setSavePath(savePath);
			setSaveUrl(saveUrl);
			setTempPath(tempPath);
		}
		map.put("error", error);
		map.put("errorInfo", errorInfo);
		return map;
	}

	/**
	 * 处理上传内容
	 * 
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> initFields(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		// 第一步：判断request
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		// 第二步：解析request
		if (isMultipart) {
			DiskFileItemFactory factory = new DiskFileItemFactory();
			// 阀值,超过这个值才会写到临时目录,否则在内存中
			factory.setSizeThreshold(1024 * 1024 * 10);
			String tempPath = getTempPath();
			factory.setRepository(new File(tempPath));
			// Create a new file upload handler
			ServletFileUpload upload = new ServletFileUpload(factory);
			upload.setHeaderEncoding("UTF-8");
			// 最大上传限制
			upload.setSizeMax(maxSize);
			List<FileItem> items = null;
			try {
				items = upload.parseRequest(request);
			} catch (FileUploadException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (items != null && items.size() > 0) {
				Iterator<FileItem> iter = items.iterator();
				// 文件域对象
				List<FileItem> list = new ArrayList<FileItem>();
				// 表单字段
				Map<String, String> fields = new HashMap<String, String>();
				while (iter.hasNext()) {
					FileItem item = (FileItem) iter.next();
					if (item.isFormField()) {
						String name = item.getFieldName();
						String value = item.getString();
						fields.put(name, value);
					} else {
						list.add(item);
					}
				}
				map.put(UploadFileUtil.FORM_FIELDS, fields);
				map.put(UploadFileUtil.FILE_FIELDS, list);
			}
		}
		return map;
	}

	/**
	 * 保存文件
	 * 
	 * @param item
	 * @return
	 */
	private Map<String, Object> saveFile(FileItem item) {
		Map<String, Object> map = new HashMap<>();
		boolean error = true;
		String errorInfo = "";
		long maxSize = getMaxSize();
		long fileSize = item.getSize();
		String fileName = item.getName();
		String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
		if (fileSize > maxSize) {
			errorInfo = "上传文件大小超过限制";
		} else {
			String newFileName = "";
			if ("".equals(fileName.trim())) {
				newFileName = DateUtil.formatDate(new Date(), "yyyyMMddHHmmss") + "_" + new Random().nextInt(1000) + "." + fileExt;
			} else {
				newFileName = DateUtil.formatDate(new Date(), "yyyyMMddHHmmss") + "_" + fileName + "." + fileExt;
			}
			setFileName(newFileName);
			setFileUrl(getSaveUrl() + newFileName);
			String savePath = getSavePath();
			File uploadedFile = new File(savePath, newFileName);
			try {
				item.write(uploadedFile);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		map.put("error", error);
		map.put("errorInfo", errorInfo);
		return map;
	}

	public long getMaxSize() {
		return maxSize;
	}

	public void setMaxSize(long maxSize) {
		this.maxSize = maxSize;
	}

	public String getBasePath() {
		return basePath;
	}

	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}

	public String getDirName() {
		return dirName;
	}

	public void setDirName(String dirName) {
		this.dirName = dirName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getSavePath() {
		return savePath;
	}

	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}

	public String getSaveUrl() {
		return saveUrl;
	}

	public void setSaveUrl(String saveUrl) {
		this.saveUrl = saveUrl;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public String getTempPath() {
		return tempPath;
	}

	public void setTempPath(String tempPath) {
		this.tempPath = tempPath;
	}

}
