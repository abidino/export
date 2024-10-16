package dev.abidino.export.export.service;

import dev.abidino.export.export.entities.Request;
import dev.abidino.export.export.repo.RequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RequestService {
    private final RequestRepository requestRepository;

    public Optional<Request> findById(Long requestId) {
        return requestRepository.findById(requestId);
    }

    public Request save(Request request) {
        return requestRepository.save(request);
    }

    public Request updateStatus(Long id, String status) {
        Optional<Request> optionalRequest = findById(id);
        if (optionalRequest.isEmpty()) {
            throw new RuntimeException("Request not found");
        }
        Request request = optionalRequest.get();
        request.setRequestStatus(status);
        return requestRepository.save(request);
    }
}
