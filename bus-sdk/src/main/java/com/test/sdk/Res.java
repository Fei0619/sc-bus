package com.test.sdk;

/**
 * @author 费世程
 * @date 2020/6/4 17:24
 */
public class Res<T> {

  private static Integer ok = 200;
  private static Integer error = 400;
  private static Integer exception = 500;

  /**
   * 响应码
   */
  private Integer code;
  /**
   * 提示信息
   */
  private String message;
  /**
   * 请求是否成功
   */
  private Boolean success;
  /**
   * 响应数据
   */
  private T data;

  private Res(Integer code, String message, Boolean success) {
    this.code = code;
    this.message = message;
    this.success = success;
  }

  private Res(Integer code, String message, Boolean success, T data) {
    this.code = code;
    this.message = message;
    this.success = success;
    this.data = data;
  }


  public static Res success() {
    return new Res(ok, "success", true);
  }

  public static <T> Res<T> success(T data) {
    return new Res<>(ok, "success", true, data);
  }

  public static <T> Res<T> success(String message, T data) {
    return new Res<>(ok, message, true, data);
  }

  public static Res error() {
    return new Res(error, "error", false);
  }

  public static Res error(String message) {
    return new Res(error, message, false);
  }

  public static Res exception() {
    return new Res(exception, "exception", false);
  }

  private static Res exception(String message) {
    return new Res(exception, message, false);
  }

}
