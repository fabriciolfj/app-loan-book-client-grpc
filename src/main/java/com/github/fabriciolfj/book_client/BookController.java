package com.github.fabriciolfj.book_client;

import com.example.grpc.book.BookCategory;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookClient bookClient;

    public BookController(BookClient bookClient) {
        this.bookClient = bookClient;
    }

    @GetMapping("/{id}")
    public BookDTO getBook(@PathVariable int id) {
        return BookDTO.fromProto(bookClient.getBook(id));
    }

    @GetMapping
    public List<BookDTO> listBooks() {
        return bookClient.listAllBooks().stream()
                .map(BookDTO::fromProto)
                .collect(Collectors.toList());
    }

    @GetMapping("/category/{category}")
    public List<BookDTO> findByCategory(@PathVariable String category) {
        BookCategory bookCategory = BookCategory.valueOf(category);
        return bookClient.findBooksByCategory(bookCategory).stream()
                .map(BookDTO::fromProto)
                .collect(Collectors.toList());
    }

    @PostMapping
    public BookDTO createBook(@RequestBody CreateBookDTO dto) {
        var book = bookClient.createBook(
                dto.title(),
                dto.author(),
                dto.isbn(),
                BookCategory.valueOf(dto.category()),
                dto.price()
        );
        return BookDTO.fromProto(book);
    }

    record CreateBookDTO(
            String title,
            String author,
            String isbn,
            String category,
            double price
    ) {}
}