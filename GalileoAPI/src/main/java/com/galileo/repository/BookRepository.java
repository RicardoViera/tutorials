package com.galileo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.galileo.model.Book;

public interface BookRepository extends JpaRepository<Book, String> {

	
}
