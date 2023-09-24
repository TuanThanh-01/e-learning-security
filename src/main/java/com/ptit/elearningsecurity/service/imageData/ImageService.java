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
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageDataRepository imageDataRepository;
    private static final Path CURRENT_FOLDER = Paths.get(System.getProperty("user.dir"));

    public ImageData saveImage(String imageFolder, MultipartFile image) throws IOException {
        Path staticPath = Paths.get("static");
        Path imagePath = Paths.get("images");
        Path folderName = Paths.get(imageFolder);

        if (!Files.exists(CURRENT_FOLDER.resolve(staticPath).resolve(imagePath).resolve(folderName))) {
            Files.createDirectories(CURRENT_FOLDER.resolve(staticPath).resolve(imagePath).resolve(folderName));
        }

        Path imageFilePath = CURRENT_FOLDER.resolve(staticPath)
                .resolve(imagePath).resolve(folderName)
                .resolve(Objects.requireNonNull(image.getOriginalFilename()));
        try(OutputStream os = Files.newOutputStream(imageFilePath)){
            os.write(image.getBytes());
        }
        ImageData imageData = new ImageData();
        imageData.setImageName(image.getOriginalFilename())
                .setType(image.getContentType())
                .setImageUrl("/images/" + imageFolder + "/" + image.getOriginalFilename());
        return imageDataRepository.save(imageData);
    }

    public void updateImage(int imageDataID, String imageFolder, MultipartFile image) throws IOException, ImageDataCustomException {
        Optional<ImageData> imageDataOptional = imageDataRepository.findById(imageDataID);
        if (imageDataOptional.isEmpty()) {
            throw new ImageDataCustomException(
                    "Image Not Found With ID: " + imageDataID,
                    DataUtils.ERROR_IMAGE_DATA_NOT_FOUND
            );
        }
        ImageData imageData = imageDataOptional.get();

        Path staticPath = Paths.get("static");
        Path imagePath = Paths.get("images");
        Path folderName = Paths.get(imageFolder);
        Path imageFilePath = CURRENT_FOLDER.resolve(staticPath)
                .resolve(imagePath).resolve(folderName)
                .resolve(Objects.requireNonNull(image.getOriginalFilename()));
        try(OutputStream os = Files.newOutputStream(imageFilePath)){
            os.write(image.getBytes());
        }
        imageData.setImageUrl("/images/" + imageFolder + "/" + image.getOriginalFilename());
        imageData.setImageName(image.getOriginalFilename());
        imageDataRepository.save(imageData);
    }

    public List<ImageData> saveAllImages(String imageFolder, List<MultipartFile> images) throws IOException {
        List<ImageData> imageDataList = new ArrayList<>();
        for (MultipartFile image : images) {
            imageDataList.add(saveImage(imageFolder, image));
        }
        return imageDataList;
    }

    public void deleteImageResource(String imageUrl) throws IOException {
        Path staticPath = Paths.get("static");
        String imageUrlPath = CURRENT_FOLDER.resolve(staticPath).toString() + imageUrl;
        FileUtils.delete(new File(imageUrlPath));
    }

    public void deleteImageDirectory(String imageFolder) throws IOException {
        Path staticPath = Paths.get("static");
        Path imagePath = Paths.get("images");
        Path folderName = Paths.get(imageFolder);
        String pathDirectoryDelete = CURRENT_FOLDER.resolve(staticPath).resolve(imagePath).resolve(folderName).toString();
        FileUtils.deleteDirectory(new File(pathDirectoryDelete));
    }

    public void deleteImageByID(int imageID) {
        imageDataRepository.deleteById(imageID);
    }

    public void updateImageDirectoryName(String oldName, String newName) {
        Path staticPath = Paths.get("static");
        Path imagePath = Paths.get("images");
        Path sourceFolderName = Paths.get(oldName);
        Path destFolderName = Paths.get(newName);
        File sourceDirectory = new File(CURRENT_FOLDER.resolve(staticPath).resolve(imagePath).resolve(sourceFolderName).toString());
        File destDirectory = new File(CURRENT_FOLDER.resolve(staticPath).resolve(imagePath).resolve(destFolderName).toString());
        sourceDirectory.renameTo(destDirectory);
    }

    public ImageData renameImageFolder(int imageDataID, String newFolder) throws ImageDataCustomException {
        Optional<ImageData> imageDataOptional = imageDataRepository.findById(imageDataID);
        if (imageDataOptional.isEmpty()) {
            throw new ImageDataCustomException(
                    "Image Not Found With ID: " + imageDataID,
                    DataUtils.ERROR_IMAGE_DATA_NOT_FOUND
            );
        }
        ImageData imageData = imageDataOptional.get();
        String[] resultSplit = imageData.getImageUrl().split("/");
        String newImageUrl = "/images/" + newFolder + "/" + resultSplit[resultSplit.length - 1];
        imageData.setImageUrl(newImageUrl);
        return imageDataRepository.save(imageData);
    }

}
