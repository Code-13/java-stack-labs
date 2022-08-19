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

package io.github.code13.javastack.libs.itextpdf;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * ChangePDFRunner.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/8/16 14:44
 */
public class ChangePDFRunner {

  @Test
  @DisplayName("testChangePdf")
  void testChangePdf() {
    try {
      Map<String, String> params = new HashMap<>();
      params.put("year", "2022");
      params.put("nominee", "Code13");
      params.put("date", "2022-08-17");
      params.put("nominator", "Code13");

      String templatePath = "src/test/resources/change/Template.pdf";
      String newPDFPath = "src/test/resources/change/Template-update.pdf";
      //      String fontPath = "/Users/xxx/tmp/pdf/Alibaba-PuHuiTi-Regular.ttf";

      PdfDocument pdf = new PdfDocument(new PdfReader(templatePath), new PdfWriter(newPDFPath));

      /*PdfFont font = PdfFontFactory.createFont(this.getClass().getClassLoader().getResource("/").getPath()
      + "font/Alibaba-PuHuiTi-Regular.ttf");*/
      //      PdfFont font = PdfFontFactory.createFont(fontPath);

      if (params != null && !params.isEmpty()) {
        // 有参数才替换
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdf, true);
        Map<String, PdfFormField> fields = form.getFormFields();
        // 获取所有的表单域
        for (String param : params.keySet()) {
          PdfFormField formField = fields.get(param);
          // 获取某个表单域
          if (formField != null) {
            // 替换值
            formField.setValue(params.get(param));
            // 替换值
          }
        }
        // 锁定表单，不让修改
        form.flattenFields();
      }
      pdf.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
