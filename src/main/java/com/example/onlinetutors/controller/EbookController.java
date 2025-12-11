package com.example.onlinetutors.controller;

import com.example.onlinetutors.model.Ebook;
import com.example.onlinetutors.model.User;
import com.example.onlinetutors.service.EBookService;
import com.example.onlinetutors.service.UserService;
import com.example.onlinetutors.service.impl.FileService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class EbookController {

    private final EBookService ebookService;
    private final UserService userService;
    private final FileService fileService;

    @GetMapping("/ebooks")
    public String getEbookPage(Model model,
                               HttpServletRequest request
                               ) {
        log.info("Accessing ebook page");
        HttpSession session = request.getSession(false);
        if (session != null) {
            Long userId = (Long) session.getAttribute("id");
            List<Ebook> ebooks = this.ebookService.handleGetEbookByUserId(userId);
            model.addAttribute("ebooks", ebooks);
            model.addAttribute("role", "TUTOR");
        }
        return "ebook/ebookPage";
    }

    @GetMapping("/home-ebook")
    public String getHomeEbookPage(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        List<Ebook> ebooks = this.ebookService.handleGetAllEbooks();
        if(session == null) {
            model.addAttribute("ebooks", ebooks);
            return "ebook/homeEbookPage";
        }
        String roleName = (String) session.getAttribute("role");
        model.addAttribute("role", roleName);
        log.info("Accessing home ebook page");
        model.addAttribute("ebooks", ebooks);
        return "ebook/homeEbookPage";
    }

    @GetMapping("/ebooks/create")
    public String getCreateEbookPage(Model model) {
        Ebook ebook = new Ebook();
        model.addAttribute("ebook", ebook);
        return "ebook/createEbook";
    }

    @PostMapping("/ebooks/create")
    public String handleCreateEbook(Model model,
                                    HttpServletRequest request,
                                    @ModelAttribute("ebook") Ebook ebook,
                                    @RequestParam("imageEbookFile") MultipartFile imgEbookFile,
                                    @RequestParam("linkDownloadFile") MultipartFile ebookFile
                                    ) {
        HttpSession session = request.getSession(false);
        String email = (String) session.getAttribute("email");
        User user = this.userService.getUserByEmail(email);
        this.ebookService.handleEbookCreate(user, ebook, imgEbookFile, ebookFile);
    return "redirect:/ebooks";
    }

    @GetMapping("/ebooks/edit")
    public String getEditEbookPage(@RequestParam("id") Long ebookId, Model model
                                   ) {
        Ebook ebook = this.ebookService.handleGetEbookByUserId(ebookId).stream().findFirst().orElse(null);
        model.addAttribute("ebook", ebook);
        return "ebook/editEbook";
    }

    @PostMapping("/ebooks/edit")
    public String handleEditEbook(@RequestParam("id") Long ebookId,
                                  @ModelAttribute("ebook") Ebook ebook,
                                  @RequestParam("imageEbookFile") MultipartFile imgEbookFile,
                                  @RequestParam("linkDownloadFile") MultipartFile ebookFile
                                  ) {
        this.ebookService.handleEbookEdit(ebook, imgEbookFile, ebookFile);
        return "redirect:/ebooks";
    }

    @GetMapping("/ebooks/delete")
    public String handleDeleteEbook(@RequestParam("id") Long ebookId) {
        this.ebookService.handleEbookDeleteById(ebookId);
        return "redirect:/ebooks";
    }

    @GetMapping("/admin/ebooks")
    public String getAdminEbookPage(Model model) {
        List<Ebook> ebooks = this.ebookService.handleGetAllEbooks();
        model.addAttribute("ebooks", ebooks);
        return "ebook/adminEbookPage";
    }

    @GetMapping("/ebook/download/{id}")
    public ResponseEntity<Resource> downloadEbook(@PathVariable Long id) {
        Ebook ebook = this.ebookService.handleGetEbookById(id);
        if(ebook == null) {
            return ResponseEntity.notFound().build();
        }

        String filePath = ebook.getDownloadLink();  // lưu trong DB

        return this.fileService.downloadFile(filePath);
    }


}
