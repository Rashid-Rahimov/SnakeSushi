package com.example.snakesushi.Repository;

import com.example.snakesushi.ImageException;
import com.example.snakesushi.enums.ImgType;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

@Repository
public class ImageRepository {

    @Value("${app.upload.dir}")
    private String uploadDir;

    public byte[] getImage(long id) {
        if (!exists(id)) return null;
        Path path = Paths.get(String.format(
                "%s/%s",
                uploadDir,
                getImageNameById(id)
        ));
        try {

            return Files.readAllBytes(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void add(long id, ImgType type, byte[] data) {
        if (id <= 0) throw new ImageException("ID must be positive", 400);
        Path filePath = Paths.get(String.format(
                "%s/%d.%s",
                uploadDir,
                id,
                mapType(type)
        ));
        try {
            Files.write(filePath, data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(long id) {
        if (id <= 0) throw new ImageException("ID must be positive", 400);
        if (!exists(id)) {
            throw new ImageException("No image found for the given ID.", 404);
        }
        Path path = Paths.get(String.format(
                "%s/%s",
                uploadDir,
                getImageNameById(id)
        ));
        try {
            if (!Files.deleteIfExists(path)) {
                throw new ImageException("Unknown server error!", 500);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean exists(long id) {
        if (id <= 0) throw new ImageException("ID must be positive", 400);

        return getAllImageIds()
                .stream()
                .anyMatch(item -> item == id);
    }

    public String getImageNameById(long id) {
        if (id <= 0) throw new ImageException("ID must be positive", 400);

        return String.format("%d.%s", id, getType(id));
    }

    public String getType(long id) {
        if (id <= 0) throw new ImageException("ID must be positive", 400);

        return getAllImageNames()
                .stream()
                .filter(name -> Long.parseLong(
                        name.substring(0, name.lastIndexOf('.'))
                ) == id)
                .map(name -> name
                        .substring(name.lastIndexOf('.') + 1)
                )
                .findAny()
                .orElseThrow(() -> new ImageException("Unknown server error!", 500));
    }

    private List<Long> getAllImageIds() {

        return getAllImageNames()
                .stream()
                .map(name -> name.substring(0, name.lastIndexOf('.')))
                .map(Long::parseLong)
                .toList();
    }

    private List<String> getAllImageNames() {
        List<String> list = new LinkedList<>();
        Path dir = Paths.get(uploadDir);
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            for (Path path : stream) {
                list.add(path.getFileName().toString());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    public String mapType(@NonNull ImgType type) {
        return switch (type) {
            case PNG -> "png";
            case JPG -> "jpg";
            case WEBP -> "webp";
        };
    }

    public ImgType mapType(@NonNull String type) {
        String errorMessage = "Image file type does not support";
        return switch (type) {
            case "png" -> ImgType.PNG;
            case "jpg" -> ImgType.JPG;
            case "webp" -> ImgType.WEBP;
            default -> throw new ImageException(errorMessage, 400);
        };
    }
}
