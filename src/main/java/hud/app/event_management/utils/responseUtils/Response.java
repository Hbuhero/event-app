package hud.app.event_management.utils.responseUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Response<T> {
    private boolean error;
    private String message;
    private T data;
    private List<T> dataList;
    private Page<T> dataPage;
    private int code;

    public Response(boolean error, String message, int code){
        this.error = error;
        this.code = code;
        this.message = message;
    }

    public Response(boolean error, int code, T data) {
        this.error = error;
        this.code = code;
        this.data = data;
    }

    public Response(boolean error, String message, int code, T data){
        this.error = error;
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public Response(boolean error, int code, List<T> dataList){
        this.error = error;
        this.code = code;
        this.dataList = dataList;
    }

    @Override
    public String toString() {
        return "Response{" +
                "error=" + error +
                ", code=" + code +
                ", data=" + data +
                ", dataList=" + dataList +
                ", message='" + message + '\'' +
                '}';
    }
}
