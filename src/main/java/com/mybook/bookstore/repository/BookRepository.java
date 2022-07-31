package com.mybook.bookstore.repository;

import com.mybook.bookstore.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, String> {

    Book getBookByIsbn(String isbn);
    String removeBookByIsbn(String isbn);

    @Modifying
    @Query("update Book b set b.name = ?2, b.author = ?3, b.type = ?4, b.price = ?5 where b.isbn = ?1")
    void setBookInfoByIsbn(String isbn, String name, String author, String type, BigDecimal price);

    @Query("select b from Book b where b.isbn in (:isbnList)")
    List<Book> getBooksByIsbnList(List<String> isbnList);
}
