package com.ptit.elearningsecurity.service.imageData;

import com.ptit.elearningsecurity.common.DataUtils;
import com.ptit.elearningsecurity.entity.ImageData;
import com.ptit.elearningsecurity.exception.ImageDataCustomException;
import com.ptit.elearningsecurity.repository.ImageDataRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageDataRepository imageDataRepository;

    public String getImgUrlById(int imageDataID) throws ImageDataCustomException {
        Optional<ImageData> imageDataOptional = imageDataRepository.findById(imageDataID);
        if (imageDataOptional.isEmpty()) {
            throw new ImageDataCustomException(
                    "Image Not Found With ID: " + imageDataID,
                    DataUtils.ERROR_IMAGE_DATA_NOT_FOUND
            );
        }
        return imageDataOptional.get().getImageUrl();
    }


    public ImageData saveImage(String lessonTitle, MultipartFile image) throws IOException {
        Path uploadDirectory = Paths.get(DataUtils.IMAGE_DIRECTORY);
        Path folderName = Paths.get(lessonTitle);

        if (!Files.exists(uploadDirectory.resolve(folderName))) {
            Files.createDirectories(uploadDirectory.resolve(folderName));
        }

        Path imagePath = uploadDirectory.resolve(folderName).resolve(image.getName());
        OutputStream os = Files.newOutputStream(imagePath);
        os.write(image.getBytes());
        ImageData imageData = new ImageData();
        imageData.setImageName(image.getName())
                .setType(image.getContentType())
                .setImageUrl(imagePath.toString());
        return imageDataRepository.save(imageData);
    }

    public List<ImageData> saveAllImages(String lessonTitle, List<MultipartFile> images) throws IOException {
        List<ImageData> imageDataList = new ArrayList<>();
        for (MultipartFile image : images) {
            imageDataList.add(saveImage(lessonTitle, image));
        }
        return imageDataList;
    }

    public void deleteImageResource(String imageUrl) throws IOException {
        FileUtils.delete(new File(imageUrl));
    }

    public void deleteImageByID(int imageID) {
        imageDataRepository.deleteById(imageID);
    }
}
