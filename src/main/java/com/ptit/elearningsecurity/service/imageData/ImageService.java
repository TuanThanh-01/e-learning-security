package com.ptit.elearningsecurity.service.imageData;

import com.ptit.elearningsecurity.common.DataUtils;
import com.ptit.elearningsecurity.entity.lecture.ImageLesson;
import com.ptit.elearningsecurity.exception.ImageDataCustomException;
import com.ptit.elearningsecurity.repository.ImageLessonRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageLessonRepository imageDataRepository;
    private static final Path CURRENT_FOLDER = Paths.get(System.getProperty("user.dir"));

    public void updateImage(int imageDataID, MultipartFile image) throws IOException, ImageDataCustomException {
        Optional<ImageLesson> imageDataOptional = imageDataRepository.findById(imageDataID);
        if (imageDataOptional.isEmpty()) {
            throw new ImageDataCustomException("Image Not Found", DataUtils.ERROR_IMAGE_DATA_NOT_FOUND);
        }
        ImageLesson imageData = imageDataOptional.get();

        Path staticPath = Paths.get("static");
        Path imagePath = Paths.get("images");
        Path lessonImage = Paths.get("lessonImage");
        String timeStamp = new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date());
        String imageName = timeStamp.concat(Objects.requireNonNull(image.getOriginalFilename()));

        Path imageFilePath = CURRENT_FOLDER.resolve(staticPath)
                .resolve(imagePath).resolve(lessonImage)
                .resolve(imageName);
        try(OutputStream os = Files.newOutputStream(imageFilePath)){
            os.write(image.getBytes());
        }
        imageData.setImageUrl("/images/lessonImage/" + imageName);
        imageData.setImageName(imageName);
        imageDataRepository.save(imageData);
    }

    public List<ImageLesson> saveAllImages(List<MultipartFile> images) throws IOException {
        List<ImageLesson> imageDataList = new ArrayList<>();
        Path staticPath = Paths.get("static");
        Path imagePath = Paths.get("images");
        Path lessonImage = Paths.get("lessonImage");

        if (!Files.exists(CURRENT_FOLDER.resolve(staticPath).resolve(imagePath).resolve(lessonImage))) {
            Files.createDirectories(CURRENT_FOLDER.resolve(staticPath).resolve(imagePath).resolve(lessonImage));
        }
        for (MultipartFile image : images) {
            String timeStamp = new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date());
            String imageName = timeStamp.concat(Objects.requireNonNull(image.getOriginalFilename()));
            Path imageFilePath = CURRENT_FOLDER.resolve(staticPath)
                    .resolve(imagePath).resolve(lessonImage)
                    .resolve(imageName);
            try(OutputStream os = Files.newOutputStream(imageFilePath)){
                os.write(image.getBytes());
            }
            ImageLesson imageData = new ImageLesson();
            imageData.setImageName(imageName)
                    .setType(image.getContentType())
                    .setImageUrl("/images/lessonImage/" + imageName);
            imageDataList.add(imageData);
        }
        return imageDataRepository.saveAll(imageDataList);
    }


    public void deleteImageByID(int imageID) {
        imageDataRepository.deleteById(imageID);
    }


    public String uploadImage(MultipartFile image) throws IOException {
        Path staticPath = Paths.get("static");
        Path imagePath = Paths.get("images");
        Path commentPath = Paths.get("lessonImage");

        if (!Files.exists(CURRENT_FOLDER.resolve(staticPath).resolve(imagePath).resolve(commentPath))) {
            Files.createDirectories(CURRENT_FOLDER.resolve(staticPath).resolve(imagePath).resolve(commentPath));
        }

        String timeStamp = new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date());
        String imageName = timeStamp.concat(Objects.requireNonNull(image.getOriginalFilename()));
        Path imageFilePath = CURRENT_FOLDER.resolve(staticPath)
                .resolve(imagePath).resolve(commentPath)
                .resolve(imageName);
        try(OutputStream os = Files.newOutputStream(imageFilePath)){
            os.write(image.getBytes());
        }
        return "/images/lessonImage/" + imageName;
    }

    public void deleteImage(String imageUrl) throws IOException {
        Path staticPath = Paths.get("static");
        String imageUrlPath = CURRENT_FOLDER.resolve(staticPath) + imageUrl;
        FileUtils.delete(new File(imageUrlPath));
    }
}
