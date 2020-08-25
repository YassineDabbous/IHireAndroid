package tn.core.model.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BaseResponse<T> {

    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("data")
    @Expose
    private T data;
    @SerializedName("errors")
    @Expose
    private List<String> errors = new ArrayList<>();
    @SerializedName("messages")
    @Expose
    private List<String> messages = new ArrayList<>();
    @SerializedName("validation")
    @Expose
    private Map<String, List<String>> validation = null;
    @SerializedName("time")
    @Expose
    private Integer time;


    public Map<String, List<String>> getValidation() {
        return validation; //Utilities.checkErrors(validation);
    }

    public void setValidation(Map<String, List<String>> validation) {
        this.validation = validation;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("code", code).append("data", data).append("errors", errors).append("messages", messages).append("time", time).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(time).append(errors).append(data).append(code).append(messages).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof BaseResponse) == false) {
            return false;
        }
        BaseResponse rhs = ((BaseResponse) other);
        return new EqualsBuilder().append(time, rhs.time).append(errors, rhs.errors).append(data, rhs.data).append(code, rhs.code).append(messages, rhs.messages).isEquals();
    }

}