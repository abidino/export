package dev.abidino.export.kafka;

import com.google.gson.Gson;
import dev.abidino.export.export.ExportApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Consumer {

    private final ExportApplicationService exportApplicationService;

    @KafkaListener(topics = "test", groupId = "test")
    public void consumeMessage(String message) {
        System.out.println("Your message is : " + message);
        Gson gson = new Gson();
        ExportEvent exportEvent = gson.fromJson(message, ExportEvent.class);
        exportApplicationService.exportWithAsync(exportEvent);
    }

}
