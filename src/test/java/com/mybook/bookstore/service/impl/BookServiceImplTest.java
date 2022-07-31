package com.mybook.bookstore.service.impl;

import com.mybook.bookstore.dto.BookDto;
import com.mybook.bookstore.dto.CheckoutRequestDto;
import com.mybook.bookstore.exception.EmptyRequestException;
import com.mybook.bookstore.exception.InvalidRequestException;
import com.mybook.bookstore.model.Book;
import com.mybook.bookstore.repository.BookRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class BookServiceImplTest {

    @InjectMocks
    private BookServiceImpl bookService;
    @Mock
    private BookRepository bookRepository;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void addBook_shouldSuccess_whenNoExistingRecordInDb() {
        Book book = getSampleBook("comic");
        BookDto bookDto = new BookDto();
        bookDto.setIsbn("test-isbn");
        Mockito.when(bookRepository.getBookByIsbn(Mockito.anyString())).thenReturn(null);
        Mockito.when(bookRepository.save(Mockito.any(Book.class))).thenReturn(book);
        bookService.addBook(bookDto);
    }

    @Test (expected = InvalidRequestException.class)
    public void addBook_shouldThrowInvalidRequestException_whenIsbnIsNotProvided() {
        BookDto bookDto = new BookDto();
        bookService.addBook(bookDto);
    }

    @Test (expected = InvalidRequestException.class)
    public void addBook_shouldThrowInvalidRequestException_whenIsbnAlreadyInDb() {
        Book book = getSampleBook("comic");
        BookDto bookDto = new BookDto();
        Mockito.when(bookRepository.getBookByIsbn(Mockito.anyString())).thenReturn(book);
        bookService.addBook(bookDto);
    }

    @Test
    public void updateBook_shouldSuccess_whenRecordInDb() {
        Book book = getSampleBook("comic");
        BookDto bookDto = new BookDto();
        bookDto.setIsbn("test-isbn");
        Mockito.when(bookRepository.getBookByIsbn(Mockito.anyString())).thenReturn(book);
        Mockito.doNothing().when(bookRepository).setBookInfoByIsbn(Mockito.anyString(),
                Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),Mockito.any(BigDecimal.class));
        bookService.updateBook(bookDto);
    }

    @Test (expected = InvalidRequestException.class)
    public void updateBook_shouldThrowInvalidRequestException_whenIsbnNotProvided() {
        BookDto bookDto = new BookDto();
        bookService.updateBook(bookDto);
    }

    @Test (expected = EmptyRequestException.class)
    public void updateBook_shouldThrowEmptyRequestException_whenNoDbRecordFound() {
        BookDto bookDto = new BookDto();
        bookDto.setIsbn("test-isbn");
        Mockito.when(bookRepository.getBookByIsbn(Mockito.anyString())).thenReturn(null);
        bookService.updateBook(bookDto);
    }

    @Test
    public void getBookByIsbn_shouldSuccess_whenRecordsInDb() {
        Book book = getSampleBook("comic");
        Mockito.when(bookRepository.getBookByIsbn(Mockito.anyString())).thenReturn(book);
        bookService.getBookByIsbn("test-isbn");
    }

    @Test (expected = InvalidRequestException.class)
    public void getBookByIsbn_shouldThrowInvalidException_whenIsbnNotProvided() {
        bookService.getBookByIsbn(null);
    }

    @Test (expected = EmptyRequestException.class)
    public void getBookByIsbn_shouldThrowEmptyException_whenNoRecordsInDb() {
        Mockito.when(bookRepository.getBookByIsbn(Mockito.anyString())).thenReturn(null);
        bookService.getBookByIsbn("test-isbn");
    }

    @Test
    public void getBooks_shouldSuccess_whenRecordsInDb() {
        Mockito.when(bookRepository.findAll()).thenReturn(getSampleBookList());
        bookService.getBooks();
    }

    @Test
    public void getBooks_shouldReturnEmptyList_whenNoRecordsInDb() {
        Mockito.when(bookRepository.findAll()).thenReturn(null);
        Assert.assertTrue(bookService.getBooks().size() == 0);
    }

    @Test
    public void deleteBookByIsbn_shouldSuccess_whenRecordInDb() {
        bookService.deleteBookByIsbn("test-isbn");
    }

    @Test (expected = InvalidRequestException.class)
    public void deleteBookByIsbn_shouldThrowInvalidRequestException_whenIsbnNotProvided() {
        bookService.deleteBookByIsbn(null);
    }

    @Test
    public void checkout_shouldSuccess_whenRecordInDb() {
        CheckoutRequestDto checkoutRequestDto = getSampleCheckoutRequestDto();
        Mockito.when(bookRepository.getBooksByIsbnList(Mockito.anyList())).thenReturn(getSampleBookList());
        Assert.assertEquals( new BigDecimal("110.0"), bookService.checkout(checkoutRequestDto));
    }

    @Test (expected = InvalidRequestException.class)
    public void checkout_shouldThrowInvalidRequestException_whenNoIsbnListProvided() {
        CheckoutRequestDto checkoutRequestDto = new CheckoutRequestDto();
        bookService.checkout(checkoutRequestDto);
    }

    private CheckoutRequestDto getSampleCheckoutRequestDto() {
        CheckoutRequestDto checkoutRequestDto = new CheckoutRequestDto();
        List<String> isbnList = new ArrayList<>();
        isbnList.add("test-isbn-1");
        isbnList.add("test-isbn-2");
        String promoCode = "test-promo-code";
        checkoutRequestDto.setIsbnlist(isbnList);
        checkoutRequestDto.setPromocode(promoCode);
        return checkoutRequestDto;
    }

    private List<Book> getSampleBookList() {
        List<Book> bookList = new ArrayList<>();
        Book book1 = new Book();
        book1.setIsbn("test-isbn-1");
        book1.setName("test-name-1");
        book1.setAuthor("test-auther-1");
        book1.setType("fiction");
        book1.setPrice(new BigDecimal("100"));

        Book book2 = new Book();
        book2.setIsbn("test-isbn-2");
        book2.setName("test-name-2");
        book2.setAuthor("test-auther-2");
        book2.setType("comic");
        book2.setPrice(new BigDecimal("20"));

        bookList.add(book1);
        bookList.add(book2);
        return bookList;
    }

    private Book getSampleBook(String type) {
        Book book = new Book();
        book.setIsbn("test-isbn");
        book.setName("test-name");
        book.setAuthor("test-author");
        book.setType(type);
        book.setPrice(new BigDecimal("12.4"));
        return book;
    }
}