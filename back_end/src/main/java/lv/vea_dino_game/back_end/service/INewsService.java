package lv.vea_dino_game.back_end.service;

import jakarta.validation.Valid;
import lv.vea_dino_game.back_end.model.dto.BasicMessageResponse;
import lv.vea_dino_game.back_end.model.dto.NewsDto;
import lv.vea_dino_game.back_end.model.dto.NewsWithoutTimeDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface INewsService {
    List<NewsDto> getNews(Integer page, String sortBy, String sortDirection);

    Integer getNumberOfPages();

    BasicMessageResponse createNews(NewsWithoutTimeDto newsDto);

    BasicMessageResponse updateNews(@Valid NewsWithoutTimeDto updatedNewsDto, Integer id);

    BasicMessageResponse deleteNews(Integer id);
}
