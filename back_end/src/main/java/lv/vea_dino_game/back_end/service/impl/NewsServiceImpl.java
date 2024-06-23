package lv.vea_dino_game.back_end.service.impl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lv.vea_dino_game.back_end.exceptions.EmptyNewsException;
import lv.vea_dino_game.back_end.exceptions.InvalidNewsIdException;
import lv.vea_dino_game.back_end.exceptions.InvalidUserInputException;
import lv.vea_dino_game.back_end.model.News;
import lv.vea_dino_game.back_end.model.Player;
import lv.vea_dino_game.back_end.model.dto.BasicMessageResponse;
import lv.vea_dino_game.back_end.model.dto.NewsDto;
import lv.vea_dino_game.back_end.model.dto.NewsWithoutTimeDto;
import lv.vea_dino_game.back_end.model.enums.*;
import lv.vea_dino_game.back_end.repo.INewsRepo;
import lv.vea_dino_game.back_end.service.INewsService;
import lv.vea_dino_game.back_end.service.helpers.Mapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements INewsService {

    private final INewsRepo newsRepo;

    private final Mapper mapper;

    private static final Integer RESULTS_PER_PAGE = 5;

    @Override
    public List<NewsDto> getNews(Integer page, String sortBy, String sortDirection) {
        if (page == null || page < 1)
            throw new InvalidUserInputException("Invalid user input for the page of " + page);

        NewsSortByEnum sortByEnum = extractSortByEnum(sortBy);
        SortDirectionEnum sortDirectionEnum = extractSortDirectionEnum(sortDirection);

        Sort sort = Sort.by(sortByEnum.toString().toLowerCase());
        Pageable pageable = PageRequest.of(page - 1, RESULTS_PER_PAGE,
                sortDirectionEnum == SortDirectionEnum.DESC ? sort.descending() : sort.ascending());

        List<News> results = newsRepo.findAll(pageable).getContent();

        return results.stream().map(news -> mapper.fromNewsToNewsDto(news)).toList();
    }

    @Override
    public Integer getNumberOfPages() {
        int resultsTotal = (int) newsRepo.count();
        return (int) Math.ceil((double) resultsTotal / RESULTS_PER_PAGE);
    }

    @Override
    public BasicMessageResponse createNews(NewsWithoutTimeDto newsDto) {
        LocalDateTime time = LocalDateTime.now();
        News news = new News(newsDto.title(), newsDto.title(), time);
        newsRepo.save(news);
        return new BasicMessageResponse("News with title "+ newsDto.title()+ " successfully created.");
    }

    @Override
    public BasicMessageResponse updateNews(NewsWithoutTimeDto updatedNewsDto, Integer id) {
        if (id < 0) throw new InvalidNewsIdException("Invalid id must be positive");
        Optional<News> newsThatWeCheck = newsRepo.findById(id);
        if (newsThatWeCheck.isEmpty()) throw new EmptyNewsException("No news with id "+ id);
        News news = newsThatWeCheck.get();
        if (updatedNewsDto.title() != null)
            news.setTitle(updatedNewsDto.title());
        if (updatedNewsDto.content()!= null)
            news.setContent(updatedNewsDto.content());
        newsRepo.save(news);
        return new BasicMessageResponse("News with title "+ updatedNewsDto.title()+ " successfully updated.");
    }

    @Override
    public BasicMessageResponse deleteNews(Integer id) {
        if (id < 0) throw new InvalidNewsIdException("Invalid id must be positive");
        Optional<News> newsThatWeCheck = newsRepo.findById(id);
        if (newsThatWeCheck.isEmpty()) throw new EmptyNewsException("No news with id "+ id);
        News news = newsThatWeCheck.get();
        newsRepo.delete(news);
        return new BasicMessageResponse("News with title "+ news.getTitle()+ " successfully deleted.");
    }

    private NewsSortByEnum extractSortByEnum(String sortBy) {
        try {
            return NewsSortByEnum.valueOf(sortBy.toUpperCase());
        } catch (Exception e) {
            throw new InvalidUserInputException("Invalid sortBy input of " + sortBy);
        }
    }

    private SortDirectionEnum extractSortDirectionEnum(String sortDirection) {
        try {
            return SortDirectionEnum.valueOf(sortDirection.toUpperCase());
        } catch (Exception e) {
            throw new InvalidUserInputException("Invalid sortDirection input of " + sortDirection);
        }
    }
}
