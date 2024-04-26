package com.gogroups.service;

import java.io.IOException;
import java.util.Base64;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageService {

	public boolean fileIsAnImage(MultipartFile file) {
		if (file.getContentType().equals("image/jpeg") || file.getContentType().equals("image/jpg")
				|| file.getContentType().equals("image/png"))
			return true;
		else
			return false;
	}

	public byte[] storeImage(MultipartFile image) throws IOException {
		return image.getBytes();
	}

	public byte[] convertToBase64(byte[] blobData) {
		return Base64.getEncoder().encode(blobData);
	}
}
