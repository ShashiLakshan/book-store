package com.mybook.bookstore.service;

import com.mybook.bookstore.dto.BookDto;
import com.mybook.bookstore.dto.CheckoutRequestDto;
import com.mybook.bookstore.model.Book;

import java.math.BigDecimal;
import java.util.List;

public interface BookService {
    String addBook(BookDto book);
    void updateBook(BookDto book);
    BookDto getBookByIsbn(String isbn);
    List<BookDto> getBooks();
    String deleteBookByIsbn(String isbn);
    BigDecimal checkout(CheckoutRequestDto checkoutRequestDto);
}
