package lv.vea_dino_game.back_end.controller;

import lombok.RequiredArgsConstructor;
import lv.vea_dino_game.back_end.model.Announcement;
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

    @PostMapping("/{userId}")
    public ResponseEntity<Void> addAnnouncement(@PathVariable("userId") Integer userId, @RequestBody Announcement announcement) {
        announcementService.addAnnouncement(userId, announcement);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/find-by-player/{userId}")
    public ResponseEntity<List<Announcement>> getAnnouncement(@PathVariable("userId") Integer userId){
        return new ResponseEntity<List<Announcement>>(announcementService.getAnnouncementByUser(userId), HttpStatus.OK);
    }

    @GetMapping("/find-by-clan/{clanId}")
    public ResponseEntity<List<Announcement>> getAnnouncementByClan(@PathVariable("clanId") Integer clanId){
        return new ResponseEntity<List<Announcement>>(announcementService.getAnnouncementByClan(clanId), HttpStatus.OK);
    }

    @PutMapping("/{playerId}/{announcementId}")
    public ResponseEntity<Announcement> updateAnnouncement(@PathVariable("playerId") Integer playerId, @PathVariable("announcementId") Integer announcementId, @RequestBody Announcement upadatedAnnouncement){
        Announcement announcement = announcementService.updateAnnouncementByAnnouncementId(playerId, announcementId, upadatedAnnouncement);
        return new ResponseEntity<Announcement>(announcement, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{playerId}/{announcementId}")
    public ResponseEntity<Announcement> deleteAnnouncement(@PathVariable("playerId") Integer playerId, @PathVariable("announcementId") Integer announcementId){
        Announcement announcement = announcementService.deleteAnnouncement(playerId, announcementId);
        return new ResponseEntity<Announcement>(announcement, HttpStatus.OK);
    }




}
