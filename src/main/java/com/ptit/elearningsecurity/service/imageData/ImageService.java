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

    public ImageLesson saveImage(String imageFolder, MultipartFile image) throws IOException {
        Path staticPath = Paths.get("static");
        Path imagePath = Paths.get("images");
        Path lessonImage = Paths.get("lessonImage");
        Path folderName = Paths.get(imageFolder);

        if (!Files.exists(CURRENT_FOLDER.resolve(staticPath).resolve(imagePath).resolve(lessonImage).resolve(folderName))) {
            Files.createDirectories(CURRENT_FOLDER.resolve(staticPath).resolve(imagePath).resolve(lessonImage).resolve(folderName));
        }

        String timeStamp = new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date());
        String imageName = timeStamp.concat(Objects.requireNonNull(image.getOriginalFilename()));
        Path imageFilePath = CURRENT_FOLDER.resolve(staticPath)
                .resolve(imagePath).resolve(lessonImage).resolve(folderName)
                .resolve(imageName);
        try(OutputStream os = Files.newOutputStream(imageFilePath)){
            os.write(image.getBytes());
        }
        ImageLesson imageData = new ImageLesson();
        imageData.setImageName(imageName)
                .setType(image.getContentType())
                .setImageUrl("/images/lessonImage/" + imageFolder + "/" + imageName);
        return imageDataRepository.save(imageData);
    }

    public void updateImage(int imageDataID, String imageFolder, MultipartFile image) throws IOException, ImageDataCustomException {
        Optional<ImageLesson> imageDataOptional = imageDataRepository.findById(imageDataID);
        if (imageDataOptional.isEmpty()) {
            throw new ImageDataCustomException("Image Not Found", DataUtils.ERROR_IMAGE_DATA_NOT_FOUND);
        }
        ImageLesson imageData = imageDataOptional.get();

        Path staticPath = Paths.get("static");
        Path imagePath = Paths.get("images");
        Path lessonImage = Paths.get("lessonImage");
        Path folderName = Paths.get(imageFolder);
        String timeStamp = new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date());
        String imageName = timeStamp.concat(Objects.requireNonNull(image.getOriginalFilename()));

        Path imageFilePath = CURRENT_FOLDER.resolve(staticPath)
                .resolve(imagePath).resolve(lessonImage).resolve(folderName)
                .resolve(imageName);
        try(OutputStream os = Files.newOutputStream(imageFilePath)){
            os.write(image.getBytes());
        }
        imageData.setImageUrl("/images/lessonImage/" + imageFolder + "/" + imageName);
        imageData.setImageName(imageName);
        imageDataRepository.save(imageData);
    }

    public List<ImageLesson> saveAllImages(String imageFolder, List<MultipartFile> images) throws IOException {
        List<ImageLesson> imageDataList = new ArrayList<>();
        Path staticPath = Paths.get("static");
        Path imagePath = Paths.get("images");
        Path lessonImage = Paths.get("lessonImage");
        Path folderName = Paths.get(imageFolder);

        if (!Files.exists(CURRENT_FOLDER.resolve(staticPath).resolve(imagePath).resolve(lessonImage).resolve(folderName))) {
            Files.createDirectories(CURRENT_FOLDER.resolve(staticPath).resolve(imagePath).resolve(lessonImage).resolve(folderName));
        }
        for (MultipartFile image : images) {
            String timeStamp = new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date());
            String imageName = timeStamp.concat(Objects.requireNonNull(image.getOriginalFilename()));
            Path imageFilePath = CURRENT_FOLDER.resolve(staticPath)
                    .resolve(imagePath).resolve(lessonImage).resolve(folderName)
                    .resolve(imageName);
            try(OutputStream os = Files.newOutputStream(imageFilePath)){
                os.write(image.getBytes());
            }
            ImageLesson imageData = new ImageLesson();
            imageData.setImageName(imageName)
                    .setType(image.getContentType())
                    .setImageUrl("/images/lessonImage/" + imageFolder + "/" + imageName);
            imageDataList.add(imageData);
        }
        return imageDataRepository.saveAll(imageDataList);
    }

    public void deleteImageResource(String imageUrl) throws IOException {
        Path staticPath = Paths.get("static");
        String imageUrlPath = CURRENT_FOLDER.resolve(staticPath).toString() + imageUrl;
        FileUtils.delete(new File(imageUrlPath));
    }

    public void deleteImageDirectory(String imageFolder) throws IOException {
        Path staticPath = Paths.get("static");
        Path imagePath = Paths.get("images");
        Path lessonImage = Paths.get("lessonImage");
        Path folderName = Paths.get(imageFolder);
        String pathDirectoryDelete = CURRENT_FOLDER.resolve(staticPath)
                .resolve(imagePath).resolve(lessonImage).resolve(folderName).toString();
        FileUtils.deleteDirectory(new File(pathDirectoryDelete));
    }

    public void deleteImageByID(int imageID) {
        imageDataRepository.deleteById(imageID);
    }

    public void updateImageDirectoryName(String oldName, String newName) {
        Path staticPath = Paths.get("static");
        Path imagePath = Paths.get("images");
        Path lessonImage = Paths.get("lessonImage");
        Path sourceFolderName = Paths.get(oldName);
        Path destFolderName = Paths.get(newName);
        File sourceDirectory = new File(CURRENT_FOLDER.resolve(staticPath).resolve(imagePath)
                .resolve(lessonImage).resolve(sourceFolderName).toString());
        File destDirectory = new File(CURRENT_FOLDER.resolve(staticPath).resolve(imagePath)
                .resolve(lessonImage).resolve(destFolderName).toString());
        sourceDirectory.renameTo(destDirectory);
    }

    public ImageLesson renameImageFolder(int imageDataID, String newFolder) throws ImageDataCustomException {
        Optional<ImageLesson> imageDataOptional = imageDataRepository.findById(imageDataID);
        if (imageDataOptional.isEmpty()) {
            throw new ImageDataCustomException("Image Not Found", DataUtils.ERROR_IMAGE_DATA_NOT_FOUND);
        }
        ImageLesson imageData = imageDataOptional.get();
        String[] resultSplit = imageData.getImageUrl().split("/");
        String newImageUrl = "/images/lessonImage/" + newFolder + "/" + resultSplit[resultSplit.length - 1];
        imageData.setImageUrl(newImageUrl);
        return imageDataRepository.save(imageData);
    }

}
