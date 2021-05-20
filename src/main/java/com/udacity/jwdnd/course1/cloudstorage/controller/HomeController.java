package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@Controller
@RequestMapping("/")
public class HomeController {
    private FileService fileService;
    private UserService userService;
    private NoteService noteService;
    private CredentialService credentialService;

    public HomeController(FileService fileService, UserService userService, NoteService noteService,
                          CredentialService credentialService) {
        this.userService = userService;
        this.fileService = fileService;
        this.noteService = noteService;
        this.credentialService = credentialService;
    }

    @GetMapping("/home")
    public String getHomePage(Authentication authentication, Model model) {
        model.addAttribute("files",
                this.fileService.getFiles(this.userService.getUserId(authentication.getName())));
        model.addAttribute("notes",
                this.noteService.getNotes(this.userService.getUserId(authentication.getName())));
        model.addAttribute("credentials",
                this.credentialService.getCredentials(this.userService.getUserId(authentication.getName())));
        return "home";
    }

    @GetMapping("/download")
    public ResponseEntity<InputStreamResource> downloadFile(Authentication authentication, RedirectAttributes redirectAttributes,
                                                            String fileId,
                                                            HttpServletResponse response) throws IOException {
            File file = this.fileService.getFile(Integer.valueOf(fileId));
            redirectAttributes.addFlashAttribute("success", "File was downloaded.");
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=" + file.getFilename())
                    .contentLength(file.getFilesize())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(new InputStreamResource(new ByteArrayInputStream(file.getFileData())));
        }

        @GetMapping("/delete_file")
        public String deleteFile(Authentication authentication, String fileId, RedirectAttributes redirectAttributes) {
        this.fileService.deleteFile(Integer.valueOf(fileId));
        redirectAttributes.addFlashAttribute("file_success", "File was deleted.");
        redirectAttributes.addFlashAttribute("tab", "file");
            return "redirect:/home";
        }

        @GetMapping("/delete_note")
        public String deleteNote(Authentication authentication, String noteid, RedirectAttributes redirectAttributes) {
        this.noteService.deleteNote(Integer.valueOf(noteid));
        redirectAttributes.addFlashAttribute("note_success", "Note was deleted.");
        redirectAttributes.addFlashAttribute("tab", "note");
        return "redirect:/home";
    }

        @GetMapping("/delete_credential")
        public String deleteCredential(Authentication authentication, String credentialid,
                                   RedirectAttributes redirectAttributes) {
        this.credentialService.deleteCredential(Integer.valueOf(credentialid));
            redirectAttributes.addFlashAttribute("credential_success", "Credential was deleted.");
        redirectAttributes.addFlashAttribute("tab", "credential");
        return "redirect:/home";
    }

    @PostMapping(path = "/upload", params = "upload-file")
    public String fileUpload(Authentication authentication,
                             @RequestParam("webfile") MultipartFile webfile, RedirectAttributes redirectAttributes) {
        String uploadError = null;
        uploadError = this.fileService.isFileValid(webfile, authentication.getName());
        if (uploadError == null) {
            int rowsAdded = this.fileService.createFile(webfile, this.userService.getUserId(authentication.getName()));
            if (rowsAdded < 0) {
                uploadError = "There was an error uploading the file. Please try again.";
            } else {
                redirectAttributes.addFlashAttribute("files",
                        this.fileService.getFiles(this.userService.getUserId(authentication.getName())));
                redirectAttributes.addFlashAttribute("file_success", "File was successfully uploaded.");
                redirectAttributes.addFlashAttribute("tab", "file");
            }
        }
        if (uploadError != null) {redirectAttributes.addFlashAttribute("uploadError", uploadError);}
            return "redirect:/home";
        }

    @PostMapping(path = "/create_note")
    public String createNote(Authentication authentication,
                             @RequestParam(name="noteId", required=false) Integer noteid,
                             @RequestParam("noteTitle") String notetitle,
                             @RequestParam("noteDescription") String notedescription,
                             RedirectAttributes redirectAttributes) {
        int rowsAdded = -1;
        if (noteid == null) {
            rowsAdded = this.noteService.createNote(notetitle, notedescription,
                    this.userService.getUserId(authentication.getName()));
            redirectAttributes.addFlashAttribute("note_success", "Note was created.");
        }
        else {
            rowsAdded = this.noteService.updateNote(noteid, notetitle, notedescription);
            redirectAttributes.addFlashAttribute("note_success", "Note was updated.");
        }
        if (rowsAdded < 0) {
            System.out.println("there was an error inserting the note");
        }  else {
            redirectAttributes.addFlashAttribute("notes",
                    this.noteService.getNotes(this.userService.getUserId(authentication.getName())));
            redirectAttributes.addFlashAttribute("tab", "note");
        }
        return "redirect:/home";
    }

    @PostMapping(path = "/create_credential")
    public String createCredential(Authentication authentication,
                             @RequestParam(name="credentialId", required=false) Integer credentialid,
                             @RequestParam("url") String url,
                             @RequestParam("username") String username,
                             @RequestParam("password") String password,
                             RedirectAttributes redirectAttributes) {
        int rowsAdded = -1;
        if (credentialid == null) {
            rowsAdded = this.credentialService.createCredential(url, username, password,
                    this.userService.getUserId(authentication.getName()));
            redirectAttributes.addFlashAttribute("credential_success", "Credential was created.");
        }
        else {
            rowsAdded = this.credentialService.updateCredential(credentialid, url, username, password);
            redirectAttributes.addFlashAttribute("credential_success", "Credential was updated.");
        }
        if (rowsAdded < 0) {
            System.out.println("there was an error inserting the credential");
        }  else {
            redirectAttributes.addFlashAttribute("credentials",
                    this.credentialService.getCredentials(this.userService.getUserId(authentication.getName())));
            redirectAttributes.addFlashAttribute("tab", "credential");
        }
        return "redirect:/home";
    }

    @GetMapping(path = {"*", "home/*" })
    public String defaultHome(){
        return "redirect:/home";
    }
}

