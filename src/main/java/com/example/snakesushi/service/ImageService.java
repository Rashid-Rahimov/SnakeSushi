package com.example.snakesushi.service;

import com.example.snakesushi.Repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository repository;

    public byte[] getImage(long id) {
        return repository.exists(id) ? repository.getImage(id) : null;
    }

    public MediaType getMediaType(long id) {
        return repository.exists(id) ? new MediaType("image", repository.getType(id)) : null;
    }
}
