package com.ptit.elearningsecurity.service.imageData;

import com.ptit.elearningsecurity.entity.discuss.ImagePost;
import com.ptit.elearningsecurity.repository.ImagePostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ImagePostService {

    private final ImagePostRepository imagePostRepository;
    private final Path CURRENT_FOLDER = Paths.get(System.getProperty("user.dir"));
    private final Path STATIC_PATH = Paths.get("static");
    private final Path IMAGE_PATH = Paths.get("images");
    private final Path POST_PATH = Paths.get("post");

    public List<ImagePost> saveImage(String postTitle, List<MultipartFile> listImages) throws IOException {
        Path folderName = Paths.get(postTitle);
        if(!Files.exists(CURRENT_FOLDER.resolve(STATIC_PATH).resolve(IMAGE_PATH).resolve(POST_PATH).resolve(folderName))) {
            Files.createDirectories(CURRENT_FOLDER.resolve(STATIC_PATH).resolve(IMAGE_PATH).resolve(POST_PATH).resolve(folderName));
        }
        List<ImagePost> imagePosts = new ArrayList<>();
        for(MultipartFile image : listImages) {
            Path imageFilePath = CURRENT_FOLDER.resolve(STATIC_PATH)
                    .resolve(IMAGE_PATH).resolve(POST_PATH).resolve(folderName)
                    .resolve(Objects.requireNonNull(image.getOriginalFilename()));
            try(OutputStream os = Files.newOutputStream(imageFilePath)){
                os.write(image.getBytes());
            }
            ImagePost imagePost = new ImagePost();
            imagePost.setImageName(image.getOriginalFilename())
                    .setType(image.getContentType())
                    .setImageUrl("/images/post/" + postTitle + "/" + image.getOriginalFilename());
            imagePosts.add(imagePost);
        }
        return imagePostRepository.saveAll(imagePosts);
    }
}
