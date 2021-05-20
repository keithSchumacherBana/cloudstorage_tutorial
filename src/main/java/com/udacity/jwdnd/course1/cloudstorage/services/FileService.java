package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FileService {
    private final FileMapper fileMapper;
    private final UserMapper userMapper;

    public FileService(FileMapper fileMapper, UserMapper userMapper) {
        this.fileMapper = fileMapper;
        this.userMapper = userMapper;
    }

    public String isFileValid(MultipartFile webfile, String username) {
        for (File file : fileMapper.getFiles(userMapper.getUser(username).getUserid())) {
            if (webfile.isEmpty()) {return "No file was selected for upload.";}
            else if (file.getFilename().equals(webfile.getOriginalFilename())) {return "Uploaded file name is in use.";}
        }
        return null;
    }

    public int createFile(MultipartFile fileData, Integer userid) {
        File file = new File();
        file.setFilename(fileData.getOriginalFilename());
        file.setContenttype(fileData.getContentType());
        try {
            file.setFileData(fileData.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        file.setFilesize(fileData.getSize());
        file.setUserid(userid);
        return fileMapper.insert(file);
    }

    public List<File> getFiles(Integer userid) {
        return fileMapper.getFiles(userid);
    }

    public File getFile(Integer fileId) { return fileMapper.getFile(fileId); }

    public void deleteFile(Integer fileId) {fileMapper.deleteFile(fileId);}
}
