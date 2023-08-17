package dev.siriuz.webfluxdemo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.util.Objects;

@Data
@ToString
@NoArgsConstructor
public class Response {
    private Date date = new Date();
    private int output;

    public Response(int output) {
        this.output = output;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Response)) return false;
        Response response = (Response) o;
        return output == response.output;
    }

    @Override
    public int hashCode() {
        return Objects.hash(output);
    }
}
