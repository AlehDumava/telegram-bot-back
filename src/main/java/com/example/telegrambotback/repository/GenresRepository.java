package com.example.telegrambotback.repository;

import com.example.telegrambotback.entity.Genres;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenresRepository extends JpaRepository<Genres, Long> {

}
