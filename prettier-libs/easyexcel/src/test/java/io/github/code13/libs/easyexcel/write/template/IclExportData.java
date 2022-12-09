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

package io.github.code13.libs.easyexcel.write.template;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * IclExportData.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2022/3/31 5:33 PM
 */
@Data
public class IclExportData {
  @ExcelProperty(value = "ICL ID")
  private String iclCode;

  @ExcelProperty(value = "ICL Name")
  private String iclName;

  @ExcelProperty(value = "Description")
  private String description;

  @ExcelProperty(value = "Category")
  private String category;

  @ExcelProperty(value = "Information Owner")
  private String infoOwners;

  @ExcelProperty(value = "Record Creator")
  private String recordCreator;

  @ExcelProperty(value = "Status")
  private String status;

  @ExcelProperty(value = "ICL Security Class")
  private String securityClass;

  @ExcelProperty(value = "ICL Classification Result\n(Confidentiality)")
  private String iclConfidentiality;

  @ExcelProperty(value = "ICL Classification Result\n(Integrity)")
  private String iclIntegrity;

  @ExcelProperty(value = "ICL Classification Result\n(Availability)")
  private String iclAvailability;

  @ExcelProperty(value = "Submit Date")
  private String submitDate;

  @ExcelProperty(value = "Approval Date")
  private String approvalDate;

  @ExcelProperty(value = {"Process", "Process Name"})
  private String processName;

  @ExcelProperty(value = {"Process", "Process Owner"})
  private String processOwner;

  @ExcelProperty(value = {"Document", "Document Name"})
  private String documentName;

  @ExcelProperty(value = {"Data Management", "Dataset ID"})
  private String dataSetId;

  @ExcelProperty(value = {"Data Management", "Dataset Name"})
  private String datasetName;

  @ExcelProperty(value = {"Data Management", "Data Engineer"})
  private String dataEngineer;

  @ExcelProperty(value = {"Data Management", "Data Steward"})
  private String dataSteward;

  @ExcelProperty(value = {"Data Management", "Layer"})
  private String layer;

  @ExcelProperty(value = {"Data Management", "Data Governors"})
  private String dataGovernors;

  @ExcelProperty(value = "IOB ID")
  private String iobCode;

  @ExcelProperty(value = "IOB Name")
  private String iobName;

  @ExcelProperty(value = "Method of Classification")
  private String classificationMethod;

  @ExcelProperty(value = "IOB Security Class")
  private String iobSecurityClass;

  @ExcelProperty(value = "IOB Classification Result\n(Confidentiality)")
  private String iobConfidentiality;

  @ExcelProperty(value = "IOB Classification Result\n(Integrity)")
  private String iobIntegrity;

  @ExcelProperty(value = "IOB Classification Result\n(Availability)")
  private String iobAvailability;
}
