package com.mybook.bookstore.service.impl;

import com.mybook.bookstore.dto.BookDto;
import com.mybook.bookstore.dto.CheckoutRequestDto;
import com.mybook.bookstore.exception.EmptyRequestException;
import com.mybook.bookstore.exception.InvalidRequestException;
import com.mybook.bookstore.model.Book;
import com.mybook.bookstore.repository.BookRepository;
import com.mybook.bookstore.service.BookService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Override
    public String addBook(BookDto bookDto) {
        if (bookDto != null && StringUtils.isEmpty(bookDto.getIsbn())) {
            throw new InvalidRequestException("Invalid Request");
        }
        if (bookRepository.getBookByIsbn(bookDto.getIsbn()) != null) {
            throw new InvalidRequestException("Book is already added");
        }
        Book book = getBook(bookDto);
        Book bookCreated = bookRepository.save(book);
        return bookCreated.getIsbn();
    }

    @Transactional
    @Override
    public void updateBook(BookDto bookDto) {
        if(bookDto != null && StringUtils.isEmpty(bookDto.getIsbn())) {
            throw new InvalidRequestException("Invalid Request");
        }
        Optional<Book> bookFromDb = Optional.ofNullable(bookRepository.getBookByIsbn(bookDto.getIsbn()));
        if (bookFromDb.isPresent()) {
            bookRepository.setBookInfoByIsbn(bookFromDb.get().getIsbn(),
                    bookDto.getName(), bookDto.getAuthor(), bookDto.getType(), bookDto.getPrice());
        } else {
            throw new EmptyRequestException("No book found for given isbn");
        }
    }

    @Override
    public BookDto getBookByIsbn(String isbn) {
        if (StringUtils.isEmpty(isbn)) {
            throw new InvalidRequestException("Invalid Request");
        }
        Optional<Book> book = Optional.ofNullable(bookRepository.getBookByIsbn(isbn));
        if (!book.isPresent()) {
            throw new EmptyRequestException("Book is not found");
        }
        return getBookDto(book.get());
    }

    @Override
    public List<BookDto> getBooks() {
        List<BookDto> bookDtoList = new ArrayList<>();
        List<Book> books = bookRepository.findAll();
        if (books == null) {
            return bookDtoList;
        }
        books.stream().forEach(book -> bookDtoList.add(getBookDto(book)));
        return bookDtoList;
    }

    @Transactional
    @Override
    public String deleteBookByIsbn(String isbn) {
        if (StringUtils.isEmpty(isbn)) {
            throw new InvalidRequestException("Invalid Request");
        }
        return bookRepository.removeBookByIsbn(isbn);
    }

    @Override
    public BigDecimal checkout(CheckoutRequestDto checkoutRequestDto) {
        if (checkoutRequestDto != null && checkoutRequestDto.getIsbnlist() == null) {
            throw new InvalidRequestException("ISBN list is empty");
        }
        List<String> isbnList = new ArrayList<>();
        checkoutRequestDto.getIsbnlist().stream().forEach(s -> isbnList.add(s));
        List<Book> bookList = bookRepository.getBooksByIsbnList(isbnList);
        return calculateTotalPriceWithDiscount(bookList, checkoutRequestDto.getPromocode());
    }

    private BigDecimal calculateTotalPriceWithDiscount(List<Book> bookList, String promoCode) {
        BigDecimal totalAmt = BigDecimal.ZERO;
        for (Book book : bookList) {
            switch (book.getType()) {
                case "fiction" :
                    if (StringUtils.isEmpty(promoCode)) {
                        totalAmt = totalAmt.add(book.getPrice());
                    } else {
                        BigDecimal discount = new BigDecimal("0.9");
                        BigDecimal discountedPrice = book.getPrice().multiply(discount);
                        totalAmt = totalAmt.add(discountedPrice);
                    }
                    break;
                default:
                    totalAmt = totalAmt.add(book.getPrice());
            }
        }
        return totalAmt;
    }

    private Book getBook(BookDto bookDto) {
        Book book = new Book();
        book.setIsbn(bookDto.getIsbn());
        book.setName(bookDto.getName());
        book.setAuthor(bookDto.getAuthor());
        book.setType(bookDto.getType());
        book.setPrice(bookDto.getPrice());
        return book;
    }

    private BookDto getBookDto(Book book) {
        BookDto bookDto = new BookDto();
        bookDto.setIsbn(book.getIsbn());
        bookDto.setName(book.getName());
        bookDto.setAuthor(book.getAuthor());
        bookDto.setType(book.getType());
        bookDto.setPrice(book.getPrice());
        return bookDto;
    }
}
