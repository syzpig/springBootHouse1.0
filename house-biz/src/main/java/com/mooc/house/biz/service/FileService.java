package com.mooc.house.biz.service;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
/**
 *保存用户图片
 */
@Service
public class FileService {
	
	@Value("${file.path:}")
	private String filePath;//文件路径
	
	/**
	 *上传文件
	 */
	public List<String> getImgPaths(List<MultipartFile> files) {
	    if (Strings.isNullOrEmpty(filePath)) {
            filePath = getResourcePath();
        }
		List<String> paths = Lists.newArrayList();
		files.forEach(file -> {
			File localFile = null;
			if (!file.isEmpty()) {
				try {
					localFile =  saveToLocal(file, filePath);//上传文件保存早本地
					//获取路径。只存最后路径==相对路径
					String path = StringUtils.substringAfterLast(localFile.getAbsolutePath(), filePath);
					paths.add(path);
				} catch (IOException e) {
					throw new IllegalArgumentException(e);
				}
			}
		});
		return paths;
	}
	
	public static String getResourcePath(){
	  File file = new File(".");
	  String absolutePath = file.getAbsolutePath();
	  return absolutePath;
	}

	private File saveToLocal(MultipartFile file, String filePath2) throws IOException {
		//Instant.now().getEpochSecond() 使用新方法获取以秒为单位的时间戳
	 File newFile = new File(filePath + "/" + Instant.now().getEpochSecond() +"/"+file.getOriginalFilename());
	 if (!newFile.exists()) {
		 newFile.getParentFile().mkdirs();
		 newFile.createNewFile();
	 }
	 Files.write(file.getBytes(), newFile);
     return newFile;
	}

}
