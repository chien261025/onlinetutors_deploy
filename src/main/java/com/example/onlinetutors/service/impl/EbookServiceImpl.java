package com.example.onlinetutors.service.impl;

import com.example.onlinetutors.model.Ebook;
import com.example.onlinetutors.model.User;
import com.example.onlinetutors.model.UserEbook;
import com.example.onlinetutors.repository.EbookRepository;
import com.example.onlinetutors.repository.UserEbookRepository;
import com.example.onlinetutors.service.EBookService;
import com.example.onlinetutors.util.enumclass.StatusEbookEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class EbookServiceImpl implements EBookService {

    private final FileService uploadFileService;
    private final EbookRepository ebookRepository;
    private final UserEbookRepository userEbookRepository;

    @Override
    public Ebook handleGetEbookById(Long ebookId) {
        return this.ebookRepository.findById(ebookId).orElse(null);

    }

    @Override
    public void handleEbookCreate(User user,Ebook ebook, MultipartFile imgEbookFile, MultipartFile ebookFile) {

        if (!imgEbookFile.isEmpty()) {
            String uploadImgEbookFile = this.uploadFileService.handleSaveUploadFile(imgEbookFile, "uploads/ebook/images/");
            ebook.setImageEbook(uploadImgEbookFile);
        }

        if (!ebookFile.isEmpty()) {
            String uploadEbookFile = this.uploadFileService.handleSaveUploadFile(ebookFile, "uploads/ebook/files/");
            ebook.setDownloadLink(uploadEbookFile);
        }
        ebook.setStatusEbook(StatusEbookEnum.ACTIVE);
        this.ebookRepository.save(ebook);
        this.handleCreateUserAndEbook(user, ebook);
    }

    @Override
    public List<Ebook> handleGetEbookByUserId(Long userId) {
        return this.userEbookRepository.findEbooksByUserId(userId);
    }

    @Override
    public void handleEbookDeleteById(Long ebookId) {
        Ebook ebook = this.ebookRepository.findById(ebookId).orElse(null);
        if (ebook != null) {
            ebook.setStatusEbook(StatusEbookEnum.INACTIVE);
            this.ebookRepository.save(ebook);
        } else {
            log.warn("Ebook with id {} not found", ebookId);
        }
    }

    @Override
    public void handleEbookEdit( Ebook ebook, MultipartFile imgEbookFile, MultipartFile ebookFile) {
        Ebook existingEbook = this.ebookRepository.findById(ebook.getId()).orElse(null);
        if (existingEbook != null) {

            existingEbook.setTitle(ebook.getTitle());
            existingEbook.setAuthor(ebook.getAuthor());

            if (!imgEbookFile.isEmpty()) {
                String uploadImgEbookFile = this.uploadFileService.handleSaveUploadFile(imgEbookFile, "uploads/ebook/images/");
                existingEbook.setImageEbook(uploadImgEbookFile);
            }

            if (!ebookFile.isEmpty()) {
                String uploadEbookFile = this.uploadFileService.handleSaveUploadFile(ebookFile, "uploads/ebook/files/");
                existingEbook.setDownloadLink(uploadEbookFile);
            }
            existingEbook.setIsbn(ebook.getIsbn());
            existingEbook.setStatusEbook(ebook.getStatusEbook());
            this.ebookRepository.save(existingEbook);
        } else {
            log.warn("Ebook with id {} not found for editing", ebook.getId());
        }
    }

    @Override
    public void handleCreateUserAndEbook(User user, Ebook ebook) {
        UserEbook userEbook = new UserEbook();
        userEbook.setUser(user);
        userEbook.setEbook(ebook);
        this.userEbookRepository.save(userEbook);
    }

    @Override
    public List<Ebook> handleGetAllEbooks() {
        return this.ebookRepository.findAll();
    }
}
