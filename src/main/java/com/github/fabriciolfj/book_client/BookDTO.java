package com.github.fabriciolfj.book_client;

public record BookDTO(
        int id,
        String title,
        String author,
        String isbn,
        String category,
        double price
) {
    public static BookDTO fromProto(com.example.grpc.book.Book book) {
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