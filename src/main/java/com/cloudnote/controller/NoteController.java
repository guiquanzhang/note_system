package com.cloudnote.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudnote.common.Result;
import com.cloudnote.dto.NoteDTO;
import com.cloudnote.entity.Note;
import com.cloudnote.service.NoteService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 笔记控制器
 */
@RestController
@RequestMapping("/note")
public class NoteController {

    @Resource
    private NoteService noteService;

    @PostMapping
    public Result<Void> createNote(@Valid @RequestBody NoteDTO noteDTO,
                                    @RequestAttribute Integer userId) {
        noteService.createNote(noteDTO, userId);
        return Result.success();
    }

    @PutMapping("/{noteId}")
    public Result<Void> updateNote(@PathVariable Integer noteId,
                                    @Valid @RequestBody NoteDTO noteDTO,
                                    @RequestAttribute Integer userId) {
        noteService.updateNote(noteId, noteDTO, userId);
        return Result.success();
    }

    @DeleteMapping("/{noteId}")
    public Result<Void> deleteNote(@PathVariable Integer noteId,
                                    @RequestAttribute Integer userId) {
        noteService.deleteNote(noteId, userId);
        return Result.success();
    }

    @GetMapping("/{noteId}")
    public Result<Note> getNoteById(@PathVariable Integer noteId,
                                     @RequestAttribute Integer userId) {
        Note note = noteService.getNoteById(noteId, userId);
        return Result.success(note);
    }

    @GetMapping("/list")
    public Result<Page<Note>> getNoteList(@RequestAttribute Integer userId,
                                           @RequestParam(defaultValue = "1") Integer pageNum,
                                           @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<Note> page = noteService.getNoteList(userId, pageNum, pageSize);
        return Result.success(page);
    }

    @GetMapping("/search")
    public Result<Page<Note>> searchNotes(@RequestAttribute Integer userId,
                                           @RequestParam String keyword,
                                           @RequestParam(defaultValue = "1") Integer pageNum,
                                           @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<Note> page = noteService.searchNotes(userId, keyword, pageNum, pageSize);
        return Result.success(page);
    }
}
