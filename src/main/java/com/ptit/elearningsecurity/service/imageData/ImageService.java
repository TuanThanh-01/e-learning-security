package com.ptit.elearningsecurity.service.imageData;

import com.ptit.elearningsecurity.common.DataUtils;
import com.ptit.elearningsecurity.common.ImageUtils;
import com.ptit.elearningsecurity.entity.image.ImageData;
import com.ptit.elearningsecurity.exception.ImageDataCustomException;
import com.ptit.elearningsecurity.repository.ImageDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageDataRepository imageDataRepository;

    public ImageData uploadImage(MultipartFile file) throws IOException {
        ImageData imageData = new ImageData();
        imageData.setName(file.getName());
        imageData.setType(file.getContentType());
        imageData.setImageData(ImageUtils.compressImage(file.getBytes()));
        return imageDataRepository.save(imageData);
    }

    public List<ImageData> uploadListImage(List<MultipartFile> files) {
        List<ImageData> imageDataList = new ArrayList<>();
        files.forEach(file -> {
            ImageData imageData = new ImageData();
            imageData.setName(file.getName());
            imageData.setType(file.getContentType());
            try {
                imageData.setImageData(ImageUtils.compressImage(file.getBytes()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            imageDataList.add(imageData);
        });
        return imageDataRepository.saveAll(imageDataList);
    }

    public byte[] downloadImage(String fileName) throws ImageDataCustomException {
        Optional<ImageData> imageData = imageDataRepository.findByName(fileName);
        if (imageData.isEmpty()) {
            throw new ImageDataCustomException(
                    "Image Not Found",
                    DataUtils.ERROR_IMAGE_DATA_NOT_FOUND
            );
        }
        return ImageUtils.decompressImage(imageData.get().getImageData());
    }
}
