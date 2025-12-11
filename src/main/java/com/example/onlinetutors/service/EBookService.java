package com.example.onlinetutors.service;

import com.example.onlinetutors.model.Ebook;
import com.example.onlinetutors.model.User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface EBookService {

    Ebook handleGetEbookById(Long ebookId);
    void handleEbookCreate(User user, Ebook ebook, MultipartFile imgEbookFile, MultipartFile ebookFile);
    List<Ebook> handleGetEbookByUserId(Long userId);
    void handleEbookDeleteById(Long ebookId);
    void handleEbookEdit(Ebook ebook, MultipartFile imgEbookFile, MultipartFile ebookFile);
    void handleCreateUserAndEbook(User user, Ebook ebook);
    List<Ebook> handleGetAllEbooks();

}
