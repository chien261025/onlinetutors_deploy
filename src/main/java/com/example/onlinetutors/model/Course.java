package com.example.onlinetutors.model;

import com.example.onlinetutors.util.enumclass.StatusCourseEnum;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Entity
@Table(name = "courses")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Course extends AbstractEntity  {

    @Column(name = "name_course")
    private String nameCourse;

    @Column(name = "key_course")
    private String keyCourse;

    @Column(name = "description_course")
    private String descriptionCourse;

    @Column(name = "price_course")
    private Double priceCourse;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "status_course")
    private StatusCourseEnum statusCourse;

    @Column(name = "author")
    private String author;

    @Column(name = "subject")
    private String subject;

    @OneToMany(mappedBy = "course")
    private List<Order> order;

    @OneToOne(mappedBy = "course", cascade = CascadeType.ALL)
    private Event event;

    @OneToMany(mappedBy = "course")
    private List<Comment> comments;

}
