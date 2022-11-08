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

package io.github.code13.javastack.spring.framework.context.format;

import java.math.RoundingMode;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Locale;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.Parser;
import org.springframework.format.Printer;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.format.datetime.standard.DateTimeFormatterFactory;
import org.springframework.format.number.CurrencyStyleFormatter;
import org.springframework.format.number.NumberStyleFormatter;
import org.springframework.format.number.PercentStyleFormatter;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.util.NumberUtils;

/**
 * Formatter_Runner.
 *
 * @see org.springframework.format.Printer
 * @see org.springframework.format.Parser
 * @see org.springframework.format.Formatter
 * @see org.springframework.format.annotation.DateTimeFormat
 * @see org.springframework.format.datetime.DateFormatter;
 * @see org.springframework.format.datetime.standard.DateTimeFormatterFactory
 * @see org.springframework.format.number.AbstractNumberFormatter
 * @see org.springframework.format.number.NumberStyleFormatter
 * @see org.springframework.format.number.PercentStyleFormatter
 * @see org.springframework.format.number.CurrencyStyleFormatter
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/11/8 11:03
 */
public class Formatter_Runner {

  /*
   * 格式化动作只需关心到两个领域：
   *
   * 时间日期领域 数字领域（其中包括货币）
   */

  // ----- 时间日期领域 start -----

  @Test
  @DisplayName("DateFormatter")
  void test() {
    DateFormatter formatter = new DateFormatter();

    Date currDate = new Date();

    System.out.println("默认输出格式：" + formatter.print(currDate, Locale.CHINA));
    formatter.setIso(DateTimeFormat.ISO.DATE_TIME);
    System.out.println("指定ISO输出格式：" + formatter.print(currDate, Locale.CHINA));
    formatter.setPattern("yyyy-mm-dd HH:mm:ss");
    System.out.println("指定pattern输出格式：" + formatter.print(currDate, Locale.CHINA));
  }

  @Test
  @DisplayName("JSR 310类型")
  void test1() {
    // DateTimeFormatterFactory dateTimeFormatterFactory = new DateTimeFormatterFactory();
    // dateTimeFormatterFactory.setPattern("yyyy-MM-dd HH:mm:ss");

    // 执行格式化动作
    System.out.println(
        new DateTimeFormatterFactory("yyyy-MM-dd HH:mm:ss")
            .createDateTimeFormatter()
            .format(LocalDateTime.now()));
    System.out.println(
        new DateTimeFormatterFactory("yyyy-MM-dd")
            .createDateTimeFormatter()
            .format(LocalDate.now()));
    System.out.println(
        new DateTimeFormatterFactory("HH:mm:ss").createDateTimeFormatter().format(LocalTime.now()));
    System.out.println(
        new DateTimeFormatterFactory("yyyy-MM-dd HH:mm:ss")
            .createDateTimeFormatter()
            .format(ZonedDateTime.now()));

    // 2020-12-26 22:44:44
    // 2020-12-26
    // 22:44:44
    // 2020-12-26 22:44:44
  }

  // ----- 时间日期领域 end -----

  // ----- 数字格式化 start -----

  @Test
  @DisplayName("NumberStyleFormatter")
  void test2() throws ParseException {
    NumberStyleFormatter formatter = new NumberStyleFormatter();

    double myNum = 1220.0455;
    System.out.println(formatter.print(myNum, Locale.getDefault()));

    formatter.setPattern("#.##");
    System.out.println(formatter.print(myNum, Locale.getDefault()));

    // 转换
    // Number parsedResult = formatter.parse("1,220.045", Locale.getDefault()); //
    // java.text.ParseException: 1,220.045
    Number parsedResult = formatter.parse("1220.045", Locale.getDefault());
    System.out.println(parsedResult.getClass() + "-->" + parsedResult);

    // 1,220.045
    // 1220.05
    //
    // class java.math.BigDecimal-->1220.045
  }

  @Test
  @DisplayName("PercentStyleFormatter")
  void test3() throws ParseException {
    PercentStyleFormatter formatter = new PercentStyleFormatter();

    double myNum = 1220.0455;
    System.out.println(formatter.print(myNum, Locale.getDefault()));

    // 转换
    // Number parsedResult = formatter.parse("1,220.045", Locale.getDefault()); //
    // java.text.ParseException: 1,220.045
    Number parsedResult = formatter.parse("122,005%", Locale.getDefault());
    System.out.println(parsedResult.getClass() + "-->" + parsedResult);
  }

  @Test
  @DisplayName("CurrencyStyleFormatter")
  void test4() throws ParseException {
    CurrencyStyleFormatter formatter = new CurrencyStyleFormatter();

    double myNum = 1220.0455;
    System.out.println(formatter.print(myNum, Locale.getDefault()));

    System.out.println("--------------定制化--------------");
    // 指定货币种类（如果你知道的话）
    // formatter.setCurrency(Currency.getInstance(Locale.getDefault()));
    // 指定所需的分数位数。默认是2
    formatter.setFractionDigits(1);
    // 舍入模式。默认是RoundingMode#UNNECESSARY
    formatter.setRoundingMode(RoundingMode.CEILING);
    // 格式化数字的模版
    formatter.setPattern("#.#¤¤");

    System.out.println(formatter.print(myNum, Locale.getDefault()));

    // 转换
    // Number parsedResult = formatter.parse("￥1220.05", Locale.getDefault());
    Number parsedResult = formatter.parse("1220.1CNY", Locale.getDefault());
    System.out.println(parsedResult.getClass() + "-->" + parsedResult);
  }

  // ----- 数字格式化  end -----

  // ----- FormattingConversionService start -----

  private static class IntegerPrinter implements Printer<Integer> {

    @Override
    public String print(Integer object, Locale locale) {
      object += 10;
      return object.toString();
    }
  }

  record Person(Integer id, String name) {}

  @Test
  @DisplayName("示例一：使用Printer，无中间转换")
  void test5() {
    FormattingConversionService formattingConversionService = new FormattingConversionService();
    FormatterRegistry formatterRegistry = formattingConversionService;
    // 说明：这里不使用DefaultConversionService是为了避免默认注册的那些转换器对结果的“干扰”，不方便看效果
    // ConversionService conversionService = new DefaultConversionService();
    ConversionService conversionService = formattingConversionService;

    // 注册格式化器
    formatterRegistry.addPrinter(new IntegerPrinter());

    // 最终均使用ConversionService统一提供服务转换
    System.out.println(conversionService.canConvert(Integer.class, String.class));
    System.out.println(conversionService.canConvert(Person.class, String.class));

    System.out.println(conversionService.convert(1, String.class));
    // 报错：No converter found capable of converting from type [cn.yourbatman.bean.Person] to type
    // [java.lang.String]
    // System.out.println(conversionService.convert(new Person(1, "YourBatman"), String.class));
  }

  @Test
  @DisplayName("示例二：使用Printer，有中间转换")
  void test6() {
    FormattingConversionService formattingConversionService = new FormattingConversionService();
    FormatterRegistry formatterRegistry = formattingConversionService;
    // 说明：这里不使用DefaultConversionService是为了避免默认注册的那些转换器对结果的“干扰”，不方便看效果
    // ConversionService conversionService = new DefaultConversionService();
    ConversionService conversionService = formattingConversionService;

    // 注册格式化器
    formatterRegistry.addFormatterForFieldType(Person.class, new IntegerPrinter(), null);
    // 强调：此处绝不能使用lambda表达式代替，否则泛型类型丢失，结果将出错
    formatterRegistry.addConverter(
        new Converter<Person, Integer>() {
          @Override
          public Integer convert(Person source) {
            return source.id();
          }
        });

    // 最终均使用ConversionService统一提供服务转换
    System.out.println(conversionService.canConvert(Person.class, String.class));
    System.out.println(conversionService.convert(new Person(1, "WhoAMI"), String.class));
  }

  private static class IntegerParser implements Parser<Integer> {

    @Override
    public Integer parse(String text, Locale locale) throws ParseException {
      return NumberUtils.parseNumber(text, Integer.class);
    }
  }

  @Test
  @DisplayName("示例一：使用Parser，无中间转换")
  void test7() {
    FormattingConversionService formattingConversionService = new FormattingConversionService();
    FormatterRegistry formatterRegistry = formattingConversionService;
    ConversionService conversionService = formattingConversionService;

    // 注册格式化器
    formatterRegistry.addParser(new IntegerParser());

    System.out.println(conversionService.canConvert(String.class, Integer.class));
    System.out.println(conversionService.convert("1", Integer.class));
  }

  @Test
  @DisplayName("示例二：使用Parser，有中间转换")
  void test8() {
    FormattingConversionService formattingConversionService = new FormattingConversionService();
    FormatterRegistry formatterRegistry = formattingConversionService;
    ConversionService conversionService = formattingConversionService;

    // 注册格式化器
    formatterRegistry.addFormatterForFieldType(
        Person.class, new IntegerPrinter(), new IntegerParser());
    formatterRegistry.addConverter(
        new Converter<Integer, Person>() {
          @Override
          public Person convert(Integer source) {
            return new Person(source, "WhoAMI");
          }
        });

    System.out.println(conversionService.canConvert(String.class, Person.class));
    System.out.println(conversionService.convert("1", Person.class));
  }

  // ----- FormattingConversionService end -----
}
