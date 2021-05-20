package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
public class CredentialService {
        private final CredentialMapper credentialMapper;
        private final UserMapper userMapper;
        private final EncryptionService encryptionService;

        public CredentialService(CredentialMapper credentialMapper, UserMapper userMapper,
                                 EncryptionService encryptionService) {
            this.credentialMapper = credentialMapper;
            this.userMapper = userMapper;
            this.encryptionService = encryptionService;
        }

        public int createCredential(String url, String username, String password, Integer userid) {
            Credential credential = new Credential();
            SecureRandom random = new SecureRandom();
            byte[] key = new byte[16];
            random.nextBytes(key);
            String encodedKey = Base64.getEncoder().encodeToString(key);
            String encryptedPassword = encryptionService.encryptValue(password, encodedKey);
            credential.setUrl(url);
            credential.setUsername(username);
            credential.setKey(encodedKey);
            credential.setPassword(encryptedPassword);
            credential.setUserid(userid);
            return credentialMapper.insert(credential);
        }

        public List<Credential> getCredentials(Integer userid) {

            List<Credential> credentials = credentialMapper.getCredentials(userid);
            for (Credential credential : credentials) {
                credential.setUnencryptedPassword(encryptionService.decryptValue(
                        credential.getPassword(),
                        credential.getKey()));
            }
            return credentials;
        }

        public Credential getCredential(Integer credentialid) {return credentialMapper.getCredential(credentialid);}

        public void deleteCredential(Integer credentialid) {credentialMapper.deleteCredential(credentialid);}

        public int updateCredential(Integer credentialid, String url, String username, String password) {
            return credentialMapper.updateCredential(credentialid, url, username,
                    encryptionService.encryptValue(password, getCredential(credentialid).getKey()));
        }
    }


