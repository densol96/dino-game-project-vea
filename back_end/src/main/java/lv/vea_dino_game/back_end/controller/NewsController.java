package lv.vea_dino_game.back_end.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lv.vea_dino_game.back_end.model.dto.*;
import lv.vea_dino_game.back_end.service.INewsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/v1/news")
public class NewsController {

    private final INewsService newsService;

    @GetMapping("/all")
    public List<NewsDto> getNews(
            @RequestParam("page") Integer page,
            @RequestParam("sortBy") String sortBy,
            @RequestParam("sortDirection") String sortDirection
    ) {
        return newsService.getNews(page, sortBy, sortDirection);

    }

    @GetMapping("sl")
    public Integer getPagesTotal(
    ) {
        return newsService.getNumberOfPages();
    }

    @PostMapping("/create")
    public ResponseEntity<BasicMessageResponse> createNews(@Valid @RequestBody NewsWithoutTimeDto newsDto) {
        return new ResponseEntity<>(newsService.createNews(newsDto),HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<BasicMessageResponse> updateNews(@PathVariable("id") Integer id, @RequestBody NewsWithoutTimeDto updatedNewsDto){
        return new ResponseEntity<>(newsService.updateNews(updatedNewsDto, id), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<BasicMessageResponse> deleteNews(@PathVariable("id") Integer id){
        return new ResponseEntity<>(newsService.deleteNews(id), HttpStatus.OK);
    }



}
