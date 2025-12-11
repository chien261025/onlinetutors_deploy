package com.example.onlinetutors.model;

import com.example.onlinetutors.util.enumclass.StatusEbookEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ebooks")
public class Ebook extends AbstractEntity {

    @Column(name = "image_ebook")
    private String imageEbook;

    @Column(name = "title")
    private String title;

    @Column(name = "author")
    private String author;

    @Column(name = "isbn")
    private String isbn;

    @Column(name = "download_link")
    private String downloadLink;

    @Column(name = "status_ebook")
    private StatusEbookEnum statusEbook;

    @OneToMany(mappedBy = "ebook")
    private List<UserEbook> userEbook;
}
