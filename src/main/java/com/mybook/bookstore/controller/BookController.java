package com.mybook.bookstore.controller;

import com.mybook.bookstore.dto.BookDto;
import com.mybook.bookstore.dto.CheckoutRequestDto;
import com.mybook.bookstore.model.Book;
import com.mybook.bookstore.service.BookService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;


@RestController
@RequestMapping("book-store/book")
public class BookController {

    @Autowired
    private BookService bookService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Object> getBook(@RequestParam(required = false) String isbn) {
        if(StringUtils.isEmpty(isbn)) {
            return ResponseEntity.status(HttpStatus.OK).body(bookService.getBooks());
        }
        return ResponseEntity.status(HttpStatus.OK).body(bookService.getBookByIsbn(isbn));
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<String> addBook(@RequestBody BookDto bookDto) {
        String isbn = bookService.addBook(bookDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(isbn);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<String> updateBook(@RequestBody BookDto bookDto) {
        bookService.updateBook(bookDto);
        return ResponseEntity.status(HttpStatus.OK).body("Successfully updated");
    }


    @RequestMapping(value = "/{isbn}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteBook(@PathVariable String isbn) {
        return ResponseEntity.status(HttpStatus.OK).body(bookService.deleteBookByIsbn(isbn));
    }

    @RequestMapping(value = "/checkout", method = RequestMethod.POST)
    public ResponseEntity<BigDecimal> checkout(@RequestBody CheckoutRequestDto checkoutRequestDto) {
        return ResponseEntity.status(HttpStatus.OK).body(bookService.checkout(checkoutRequestDto));
    }

}
