package holiday.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import holiday.entity.EventDates;
import holiday.services.EventsDatesService;

@RestController
public class EventDateController {

    private EventsDatesService eventsDatesService;

    @Autowired
    public void setEventsDatesService(EventsDatesService eventsDatesService) {
        this.eventsDatesService = eventsDatesService;
    }

    @GetMapping("/writeexeventdates")
    public ResponseEntity<String> writeExeventDatesToFile() throws FileNotFoundException, IOException {
        List<EventDates> eventDateList = eventsDatesService.findAllByOrderByDate();
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("exEventDatesList.bin"))) {
            oos.writeObject(eventDateList);
        } catch (Exception e) {
            return ResponseEntity.ok(e.getMessage());
        }

        return ResponseEntity.ok("Exeventdata has been written");
    }

    @GetMapping("/readexeventdates")
    public ResponseEntity<String> readExeventDatesToFile() throws FileNotFoundException, IOException {
        List<EventDates> eventDateList = new ArrayList<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("exEventDatesList.bin"))) {
            Object entity = ois.readObject();
            if (entity instanceof List<?>)
                eventDateList = (List<EventDates>) entity;
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

        for (var event : eventDateList) {
            Boolean dateNotExists = eventsDatesService.findFirstByDate(event.getDate()) == null;
            if (dateNotExists)
                eventsDatesService.save(event);
        }
        return ResponseEntity.ok("Exeventdata has been read");
    }

}
