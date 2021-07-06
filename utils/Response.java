package tz.go.tcra.lims.utils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Response<T> {

    private Integer code;
    private Boolean status;
    private String message;

    private T data;

//    private List<T> dataList = new ArrayList<>();

    public Response(Integer code, Boolean status, String message, T data) {
        this.code = code;
        this.status = status;
        this.message = message;
        this.data = data;
    }

//    public Response(Integer code, Boolean status, String message, List<T> dataList) {
//        this.code = code;
//        this.status = status;
//        this.message = message;
//        this.dataList = dataList;
//    }

//    public Response(Integer code, Boolean status, String message, T data, List<T> dataList) {
//        this.code = code;
//        this.status = status;
//        this.message = message;
//        this.data = data;
//        this.dataList = dataList;
//    }
}