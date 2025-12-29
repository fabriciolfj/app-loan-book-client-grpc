package com.github.fabriciolfj.book_client;

import com.github.fabriciolfk.book_server.grpc.*;
import com.google.protobuf.Empty;
import io.grpc.ManagedChannel;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class BookClient {

    private final BookServiceGrpc.BookServiceBlockingStub blockingStub;
    private final BookServiceGrpc.BookServiceStub asyncStub;

    public BookClient(ManagedChannel channel) {
        this.blockingStub = BookServiceGrpc.newBlockingStub(channel);
        this.asyncStub = BookServiceGrpc.newStub(channel);
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

    public void searchBooksAsync(String query, int maxResults)
            throws InterruptedException {
        log.info("Calling searchBooks async: query={}", query);

        SearchRequest request = SearchRequest.newBuilder()
                .setQuery(query)
                .setMaxResults(maxResults)
                .build();

        CountDownLatch latch = new CountDownLatch(1);
        List<Book> results = new ArrayList<>();

        asyncStub.searchBooks(request, new StreamObserver<>() {
            @Override
            public void onNext(Book book) {
                log.info("Received book: {}", book.getTitle());
                results.add(book);
            }

            @Override
            public void onError(Throwable t) {
                log.error("Error in searchBooks", t);
                latch.countDown();
            }

            @Override
            public void onCompleted() {
                log.info("Search completed. Total: {}", results.size());
                latch.countDown();
            }
        });

        latch.await(30, TimeUnit.SECONDS);
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
