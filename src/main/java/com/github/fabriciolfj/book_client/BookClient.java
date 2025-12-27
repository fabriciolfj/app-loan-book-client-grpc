package com.github.fabriciolfj.book_client;

import com.example.grpc.book.*;
import com.google.protobuf.Empty;
import io.grpc.ManagedChannel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookClient {

    private final BookServiceGrpc.BookServiceBlockingStub blockingStub;

    public BookClient(final ManagedChannel channel) {
        this.blockingStub = BookServiceGrpc.newBlockingStub(channel);
    }

    public Book getBook(int id) {
        final var request = BookRequest.newBuilder()
                .setId(id)
                .build();

        final BookResponse response = blockingStub.getBook(request);
        return response.getBook();
    }

    public List<Book> listAllBooks() {
        var response = blockingStub.listBooks(Empty.getDefaultInstance());
        return response.getBooksList();
    }

    public List<Book> findBooksByCategory(BookCategory category) {
        CategoryRequest request = CategoryRequest.newBuilder()
                .setCategory(category)
                .build();

        BookListResponse response = blockingStub.findBooksByCategory(request);
        return response.getBooksList();
    }

    public Book createBook(String title, String author, String isbn,
                           BookCategory category, double price) {
        CreateBookRequest request = CreateBookRequest.newBuilder()
                .setTitle(title)
                .setAuthor(author)
                .setIsbn(isbn)
                .setCategory(category)
                .setPrice(price)
                .build();

        BookResponse response = blockingStub.createBook(request);
        return response.getBook();
    }
}
