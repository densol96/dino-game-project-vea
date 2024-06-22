package lv.vea_dino_game.back_end.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lv.vea_dino_game.back_end.model.dto.BasicMessageResponse;
import lv.vea_dino_game.back_end.model.dto.RequestLearnNewPlayerStats;
import lv.vea_dino_game.back_end.model.dto.RequestStartJob;
import lv.vea_dino_game.back_end.service.IPlayerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/v1/job")
@RequiredArgsConstructor
public class JobController {

    private final IPlayerService playerService;

    @PostMapping("/start")
    public ResponseEntity<BasicMessageResponse> postStartJob(@Valid @RequestBody RequestStartJob requestStartJob) {
        return new ResponseEntity<BasicMessageResponse>(playerService.startJob(requestStartJob), HttpStatus.OK);
    }

    @PostMapping("/finish/{id}")
    public ResponseEntity<BasicMessageResponse> postFinishJob(@PathVariable("id")  Integer id) {
        return new ResponseEntity<BasicMessageResponse>(playerService.finishJob(id), HttpStatus.OK);
    }
}
