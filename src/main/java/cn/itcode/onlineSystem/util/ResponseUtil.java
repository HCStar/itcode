package cn.itcode.onlineSystem.util;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.apache.http.HttpStatus;

import java.io.Serializable;

@Data
public class ResponseUtil<T> implements Serializable {
        private boolean ret;
        private int code;
        private String msg;
        private Object data;

        public ResponseUtil() {
            super();
        }

        public static ResponseUtil error() {

            return new ResponseUtil(false, HttpStatus.SC_INTERNAL_SERVER_ERROR,"未知异常，请联系管理员", null);
        }

        public static ResponseUtil error(String msg) {

            return new ResponseUtil(false, HttpStatus.SC_INTERNAL_SERVER_ERROR, msg, null);
        }

         public static JSONObject error(JSONObject msg) {

//            return new ResponseUtil(false, HttpStatus.SC_INTERNAL_SERVER_ERROR, msg, null);
            return msg;
        }

        public static ResponseUtil error(int code, String msg) {

            return new ResponseUtil(false, HttpStatus.SC_INTERNAL_SERVER_ERROR, msg, null);
        }

        public static <T> ResponseUtil error(int code, String msg, T data) {
            return new ResponseUtil(false, code, msg, data);
        }

        public static ResponseUtil suc(String msg) {
            return new ResponseUtil(true, HttpStatus.SC_OK, msg, null);
        }
        public static ResponseUtil suc(Object data) {
            return new ResponseUtil(true, HttpStatus.SC_OK, null, data);
        }
        public static ResponseUtil suc() {
            return new ResponseUtil();
        }

        public static ResponseUtil suc(String msg, Object data) {
            return new ResponseUtil(true, HttpStatus.SC_OK, msg, data);
    }

        public ResponseUtil(Boolean ret, int code, String msg, T data){
            this.ret = ret;
            this.code = code;
            this.msg = msg;
            this.data = data;
        }
}
