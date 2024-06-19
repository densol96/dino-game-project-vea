package lv.vea_dino_game.back_end.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lv.vea_dino_game.back_end.model.Announcement;
import lv.vea_dino_game.back_end.model.dto.BasicMessageResponse;
import lv.vea_dino_game.back_end.service.IAnnouncementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/announcements")
@RequiredArgsConstructor
public class AnnouncementController {
    private final IAnnouncementService announcementService;

    @PostMapping
    public ResponseEntity<BasicMessageResponse> addAnnouncement(@Valid @RequestBody Announcement announcement) {
        return new ResponseEntity<BasicMessageResponse>(announcementService.addAnnouncement(announcement),HttpStatus.CREATED);
    }

    @GetMapping("/find-by-player/{userId}")
    public ResponseEntity<List<Announcement>> getAnnouncement(@PathVariable("userId") Integer userId){
        return new ResponseEntity<List<Announcement>>(announcementService.getAnnouncementByUser(userId), HttpStatus.OK);
    }

    @GetMapping("/find-by-clan")
    public ResponseEntity<List<Announcement>> getAnnouncementByClan( Integer clanId){
        return new ResponseEntity<List<Announcement>>(announcementService.getAnnouncementByClan(clanId), HttpStatus.OK);
    }

    @PutMapping("/{announcementId}")
    public ResponseEntity<BasicMessageResponse> updateAnnouncement(@PathVariable("announcementId") Integer announcementId, @RequestBody Announcement upadatedAnnouncement){
        return new ResponseEntity<BasicMessageResponse>(announcementService.updateAnnouncementByAnnouncementId(announcementId, upadatedAnnouncement),HttpStatus.CREATED);

    }

    @DeleteMapping("/delete/{announcementId}")
    public ResponseEntity<BasicMessageResponse> deleteAnnouncement(@PathVariable("announcementId") Integer announcementId){
        return new ResponseEntity<BasicMessageResponse>(announcementService.deleteAnnouncement(announcementId),HttpStatus.CREATED);
    }


}
