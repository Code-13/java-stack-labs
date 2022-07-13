/*
 * Copyright 2022-present the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.code13.javastack.libs.zxing;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.CharacterSetECI;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;
import javax.imageio.ImageIO;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

/**
 * SimpleQrCodeService.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/9/19 15:30
 */
public class SimpleQrCodeService implements QrCodeService {

  private static final Logger logger = LoggerFactory.getLogger(SimpleQrCodeService.class);

  @Override
  public byte[] create(String content, int width, int height, int margin) {

    Map<EncodeHintType, Object> hints = new EnumMap<>(EncodeHintType.class);

    // 设置编码
    hints.put(EncodeHintType.CHARACTER_SET, CharacterSetECI.UTF8);
    // 设置容错级别
    hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
    // 设置外边距
    hints.put(EncodeHintType.MARGIN, margin);

    try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

      BitMatrix bitMatrix =
          new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);

      // 将二维码写入图片
      BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
      for (int i = 0; i < width; i++) {
        for (int j = 0; j < height; j++) {
          bufferedImage.setRGB(
              i, j, bitMatrix.get(i, j) ? Color.black.getRGB() : Color.WHITE.getRGB());
        }
      }

      ImageIO.write(bufferedImage, "png", outputStream);

      return outputStream.toByteArray();
    } catch (IOException | WriterException e) {
      e.printStackTrace();
      throw new QrcodeException(e);
    }
  }

  @Override
  public <T> T create(
      String content, int width, int height, int margin, QrcodeConvertFunction<byte[], T> fn)
      throws QrcodeException {
    try {
      return fn.apply(create(content, width, height, margin));
    } catch (IOException e) {
      throw new QrcodeException(e);
    }
  }

  @Override
  public File createToFile(String content, int width, int height, int margin)
      throws QrcodeException {
    return create(
        content,
        width,
        height,
        margin,
        bytes -> FileUtils.createTmpFile(new ByteArrayInputStream(bytes), "png"));
  }

  @Override
  public MultipartFile createToMultiPartFile(String content, int width, int height, int margin)
      throws QrcodeException {
    return create(
        content,
        width,
        height,
        margin,
        bytes -> FileUtils.inputStreamToMultipartFile(new ByteArrayInputStream(bytes), "png"));
  }

  @Override
  public QrCode createQrCode(String content, int width, int height, int margin)
      throws QrcodeException {
    return create(
        content,
        width,
        height,
        margin,
        bytes -> {
          MultipartFile multipartFile =
              FileUtils.inputStreamToMultipartFile(new ByteArrayInputStream(bytes), "png");
          String path = uploadPicture(multipartFile);
          String base64OfImgUrl = getBase64OfImgUrl(path);
          return new QrCode(content, path, base64OfImgUrl);
        });
  }

  @Override
  public <T> T createQrCode(
      String content, int width, int height, int margin, QrcodeConvertFunction<QrCode, T> fn)
      throws QrcodeException {
    try {
      return fn.apply(createQrCode(content, width, height, margin));
    } catch (IOException e) {
      throw new QrcodeException(e);
    }
  }

  private String uploadPicture(MultipartFile multipartFile) {
    return "";
  }

  private String getBase64OfImgUrl(String url) {
    return "";
  }

  @Data
  private static class QrcodeCacheKey {
    private String content;
    private int width;
    private int height;
    private int margin;

    public static QrcodeCacheKey of(String content, int width, int height, int margin) {
      QrcodeCacheKey qrcodeCacheKey = new QrcodeCacheKey();
      qrcodeCacheKey.setContent(content);
      qrcodeCacheKey.setWidth(width);
      qrcodeCacheKey.setHeight(height);
      qrcodeCacheKey.setMargin(margin);
      return qrcodeCacheKey;
    }
  }
}
