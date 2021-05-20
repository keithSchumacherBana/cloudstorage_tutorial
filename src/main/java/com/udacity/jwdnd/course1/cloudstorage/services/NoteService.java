package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {
    private final NoteMapper noteMapper;
    private final UserMapper userMapper;

    public NoteService(NoteMapper noteMapper, UserMapper userMapper) {
        this.noteMapper = noteMapper;
        this.userMapper = userMapper;
    }

    public int createNote(String notetitle, String notedescription, Integer userid) {
        Note note = new Note();
        note.setNotetitle(notetitle);
        note.setNotedescription(notedescription);
        note.setUserid(userid);
        return noteMapper.insert(note);
    }

    public List<Note> getNotes(Integer userid) {
        return noteMapper.getNotes(userid);
    }

    public void deleteNote(Integer noteid) {noteMapper.deleteNote(noteid);}

    public int updateNote( Integer noteid, String notetitle, String notedescription) {
        return noteMapper.updateNote(noteid, notetitle, notedescription);
    }
}