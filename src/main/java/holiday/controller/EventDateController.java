package holiday.controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import holiday.entity.EventDates;
import holiday.services.EventsDatesService;

@RestController
public class EventDateController {

    private final EventsDatesService eventsDatesService;

    public EventDateController(EventsDatesService eventsDatesService) {
        this.eventsDatesService = eventsDatesService;
    }

    @GetMapping("/writeexeventdates")
    public ResponseEntity<String> writeExEventDatesToFile() {
        List<EventDates> eventDateList = eventsDatesService.findAllByOrderByDate();
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("exEventDatesList.bin"))) {
            oos.writeObject(eventDateList);
            return ResponseEntity.ok("Exeventdata has been written");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error writing to file: " + e.getMessage());
        }
    }

    @GetMapping("/readexeventdates")
    public ResponseEntity<String> readExEventDatesFromFile() {
        List<EventDates> eventDateList = new ArrayList<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("exEventDatesList.bin"))) {
            Object entity = ois.readObject();
            if (entity instanceof List<?> tempList) {
                for (Object obj : tempList) {
                    if (obj instanceof EventDates dates) {
                        eventDateList.add(dates);
                    } else {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid file content");
                    }
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid file content");
            }
        } catch (IOException | ClassNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error reading from file: " + e.getMessage());
        }

        for (EventDates event : eventDateList) {
            if (eventsDatesService.findFirstByDate(event.getDate()) == null) {
                eventsDatesService.save(event);
            }
        }

        return ResponseEntity.ok("Exeventdata has been read and saved");
    }
}
