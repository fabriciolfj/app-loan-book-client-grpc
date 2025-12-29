package com.github.fabriciolfj.book_client;

import com.github.fabriciolfk.book_server.grpc.Book;

public record BookDTO(
        int id,
        String title,
        String author,
        String isbn,
        String category,
        double price
) {
    public static BookDTO fromProto(Book book) {
        return new BookDTO(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getIsbn(),
                book.getCategory().name(),
                book.getPrice()
        );
    }
}