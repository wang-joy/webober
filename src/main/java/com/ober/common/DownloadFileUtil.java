package com.ober.common;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DownloadFileUtil {
	/**
	 * 下载文件
	 * 
	 * @param request
	 * @param response
	 * @param filePath
	 *            文件的相对路径
	 * @throws FileNotFoundException
	 */
	public static void downloadFile(HttpServletRequest request, HttpServletResponse response, String fileUrl) throws FileNotFoundException {
		// 获取文件真实的路径
		String realPath = request.getSession().getServletContext().getRealPath("/" + fileUrl);
		File file = new File(realPath);
		if (file.exists()) {
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
			BufferedOutputStream bos = null;
			try {
				bos = new BufferedOutputStream(response.getOutputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			long fileLength = file.length();
			String fileName = file.getName();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("multipart/form-data");
			// 解决各浏览器的中文乱码问题
			String userAgent = request.getHeader("User-Agent");
			byte[] bytes = null;
			try {
				bytes = userAgent.contains("MSIE") ? fileName.getBytes() : fileName.getBytes("UTF-8");// fileName.getBytes("UTF-8")处理safari的乱码问题
				fileName = new String(bytes, "ISO-8859-1"); // 各浏览器基本都支持ISO编码
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			response.setHeader("Content-disposition", String.format("attachment; filename=\"%s\"", fileName));
			response.setHeader("Content-Length", String.valueOf(fileLength));
			byte[] buff = new byte[2048];
			int bytesRead;
			try {
				while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
					bos.write(buff, 0, bytesRead);
				}
				bos.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (bis != null && bos != null) {
					try {
						bis.close();
						bos.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		} else {
			// 如果文件未找到，抛出异常信息
			throw new FileNotFoundException();
		}
	}
}
