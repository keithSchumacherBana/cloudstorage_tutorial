package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CredentialMapper {
        @Select("SELECT * FROM CREDENTIALS WHERE userid = #{userid}")
        List<Credential> getCredentials(Integer userid);

        @Select("SELECT * FROM CREDENTIALS WHERE credentialid = #{credentialid}")
        Credential getCredential(Integer credentialid);

        @Insert("INSERT INTO CREDENTIALS (url, username, key, password, userid) VALUES(#{url}, " +
                "#{username}, #{key}, #{password}, #{userid})")
        @Options(useGeneratedKeys = true, keyProperty = "credentialid")
        Integer insert(Credential credential);

        @Delete("DELETE FROM CREDENTIALS WHERE credentialid=#{credentialid}")
        void deleteCredential(Integer credentialid);

        @Update("UPDATE CREDENTIALS SET url=#{url}, username=#{username}, password=#{password} WHERE " +
                "credentialid=#{credentialid}")
        Integer updateCredential(Integer credentialid, String url, String username, String password);
    }