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

package io.github.code13.libs.zxing;

import java.io.File;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

/**
 * QrCodeService.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/9/19 15:21
 */
public interface QrCodeService {

  /**
   * 创建二维码.
   *
   * @param content 二维码内容
   * @param width 二维码宽度
   * @param height 二维码高度
   * @param margin 外边距
   * @return byte[] 二维码对应的字节数组
   * @throws QrcodeException 生成二维码抛出的异常
   */
  byte[] create(String content, int width, int height, int margin) throws QrcodeException;

  /**
   * 创建二维码,并通过转换函数将其转换为其他类型.
   *
   * @param content 二维码内容
   * @param width 二维码宽度
   * @param height 二维码高度
   * @param margin 外边距
   * @param fn 这是一个转换函数，用于提供将字节数组转化为其他需要的类型
   * @return byte[] 二维码对应的字节数组
   * @throws QrcodeException 生成二维码抛出的异常
   */
  <T> T create(
      String content, int width, int height, int margin, QrcodeConvertFunction<byte[], T> fn)
      throws QrcodeException;

  /**
   * 创建二维码，并存储至临时文件中.
   *
   * @param content 二维码内容
   * @param width 二维码宽度
   * @param height 二维码高度
   * @param margin 外边距
   * @return File 二维码图片文件
   * @throws QrcodeException 生成二维码抛出的异常
   */
  File createToFile(String content, int width, int height, int margin) throws QrcodeException;

  /**
   * 创建二维码，并存储至 MultipartFile，通常用于上传文件.
   *
   * @param content 二维码内容
   * @param width 二维码宽度
   * @param height 二维码高度
   * @param margin 外边距
   * @return File 二维码图片文件
   * @throws QrcodeException 生成二维码抛出的异常
   */
  MultipartFile createToMultiPartFile(String content, int width, int height, int margin)
      throws QrcodeException;

  /**
   * 创建 二维码 将 内容、链接地址、base64字符串 封装在一起.
   *
   * @param content 二维码内容
   * @param width 二维码宽度
   * @param height 二维码高度
   * @param margin 外边距
   * @return QrCode
   * @throws QrcodeException 生成二维码抛出的异常
   */
  QrCode createQrCode(String content, int width, int height, int margin) throws QrcodeException;

  /**
   * 创建二维码,并通过转换函数将其转换为其他类型.
   *
   * @param content 二维码内容
   * @param width 二维码宽度
   * @param height 二维码高度
   * @param margin 外边距
   * @param fn 这是一个转换函数，用于提供将字节数组转化为其他需要的类型
   * @return byte[] 二维码对应的字节数组
   * @throws QrcodeException 生成二维码抛出的异常
   */
  <T> T createQrCode(
      String content, int width, int height, int margin, QrcodeConvertFunction<QrCode, T> fn)
      throws QrcodeException;

  /**
   * 转换函数.
   *
   * @param <T> T
   * @param <R> R
   */
  @FunctionalInterface
  interface QrcodeConvertFunction<T, R> {

    /**
     * apply.
     *
     * @param t param
     * @return result
     */
    R apply(T t) throws IOException;
  }
}
