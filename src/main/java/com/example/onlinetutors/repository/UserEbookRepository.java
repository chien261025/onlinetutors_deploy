package com.example.onlinetutors.repository;

import com.example.onlinetutors.model.Ebook;
import com.example.onlinetutors.model.UserEbook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserEbookRepository extends JpaRepository<UserEbook, Long> {

    @Query("SELECT ue.ebook FROM UserEbook ue WHERE ue.user.id = :userId")
    List<Ebook> findEbooksByUserId(@Param("userId") Long userId);

    @Query("SELECT ue.ebook FROM UserEbook ue")
    List<Ebook> findAllUserEbooks();

}
