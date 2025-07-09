package com.ecommerce.studentmarket.common.cloudinary.services;

import com.ecommerce.studentmarket.common.cloudinary.domains.ImageDomain;
import com.ecommerce.studentmarket.common.cloudinary.dtos.ImageDto;
import com.ecommerce.studentmarket.common.cloudinary.enums.ChuSoHuu;
import com.ecommerce.studentmarket.common.cloudinary.enums.LoaiAnh;
import com.ecommerce.studentmarket.common.cloudinary.repositories.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private CloudinaryService cloudinaryService;


//    Lấy ảnh
    public List<ImageDomain> getImage(Long maSH, ChuSoHuu chuSoHuu, LoaiAnh loaiAnh){
        return imageRepository.findByIdChuSoHuuAndChuSoHuuAndLoaiAnhAndDaXoaFalseOrderByThuTu(maSH, chuSoHuu, loaiAnh);
    }

//    Lấy tất cả ảnh sở hữu
public List<ImageDomain> getAllImage(Long maSH, ChuSoHuu chuSoHuu){
    return imageRepository.findByIdChuSoHuuAndChuSoHuuAndDaXoaFalseOrderByThuTu(maSH, chuSoHuu);
}


    //    Đăng tải ảnh lên cloudinary
    @Transactional(rollbackFor = Exception.class)
    public ImageDomain uploadImage(MultipartFile file, ImageDto image, Integer thuTu) {
        try {
        Map<String, Object> uploadResult = cloudinaryService.uploadImage(file);

        ImageDomain imageDomain = createImageDomain(uploadResult, image, thuTu);

        return imageRepository.save(imageDomain);}
        catch (Exception e){
            throw new RuntimeException("Lỗi khi upload ảnh: "+ e.getMessage());
        }
    }

//    Thêm nhiều ảnh lên cloudinary
    @Transactional(rollbackFor = Exception.class)
    public List<ImageDomain> uploadMultipleImage (List<MultipartFile> files, List<ImageDto> imageDtos){
        List<ImageDomain> images = new ArrayList<>();

        for (int i = 0; i< files.size(); i++){
            ImageDomain imageDomain = uploadImage(files.get(i), imageDtos.get(i), i+1);
            images.add(imageDomain);
        }
        return images;
    }
//  Xóa mềm ảnh
    public  String deleteImage(Long idImg){
        ImageDomain image = imageRepository.findById(idImg).orElseThrow(() -> new RuntimeException("Không tìm thấy ảnh"));
        image.setDaXoa(true);
        imageRepository.save(image);
        return "Xóa thành công";
    }

//    Xóa vĩnh viễn ảnh

    @Transactional(rollbackFor = Exception.class)
    public String hardDelete(Long idImg){
        ImageDomain imageDomain = imageRepository.findById(idImg).orElseThrow(
                () -> new RuntimeException("Không tìm thấy ảnh")
        );
        try {
            Map<String, Object> deleteResult = cloudinaryService.deleteImage(imageDomain.getPublicId());

            if(!"ok".equals(deleteResult.get("result"))){
                throw new RuntimeException("Không thể xóa ảnh từ Cloudinary");
            }
            imageRepository.delete(imageDomain);
            return "Xóa ảnh thành công";
        }
        catch (Exception e){
            throw new RuntimeException("Lỗi khi xóa ảnh: "+ e.getMessage(), e);
        }
    }

//    Cập nhật ảnh
    @Transactional(rollbackFor = Exception.class)
    public ImageDomain updateImage(Long idImg, MultipartFile newFile, ImageDto imageDto){
        ImageDomain existingImage = imageRepository.findById(idImg).orElseThrow(
                () -> new RuntimeException("Không tìm thấy ảnh")
        );
        try {
            cloudinaryService.deleteImage(existingImage.getPublicId());

            Map<String, Object> uploadResult = cloudinaryService.uploadImage(newFile);

            existingImage.setPublicId(uploadResult.get("public_id").toString());

            existingImage.setUrl(uploadResult.get("secure_url").toString());

            Optional.ofNullable(imageDto.getChuSoHuu()).ifPresent(existingImage::setChuSoHuu);
            Optional.ofNullable(imageDto.getLoaiAnh()).ifPresent(existingImage::setLoaiAnh);
             return imageRepository.save(existingImage);
        }catch (Exception e){
            throw new RuntimeException("Lỗi khi cập nhật ảnh: "+ e.getMessage(), e);
        }
    }

//    Chuyển dữ liệu Map bắt được sang ImageDomain
    public ImageDomain createImageDomain(Map<String, Object> data, ImageDto image, Integer thuTu){
        ImageDomain imageDomain = new ImageDomain();

        imageDomain.setPublicId(data.get("public_id").toString());
        imageDomain.setUrl(data.get("secure_url").toString());
        imageDomain.setThuTu(thuTu);
        imageDomain.setChuSoHuu(image.getChuSoHuu());
        imageDomain.setIdChuSoHuu(image.getIdChuSoHuu());
        imageDomain.setLoaiAnh(image.getLoaiAnh());

        return imageDomain;
    }
}
