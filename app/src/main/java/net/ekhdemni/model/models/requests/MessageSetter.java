package net.ekhdemni.model.models.requests;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class MessageSetter {

    public MessageSetter(Integer cid, Integer to, String message) {
        this.cid = cid;
        this.to = to;
        this.message = message;
    }

    @SerializedName("cid")
    @Expose
    private Integer cid;
    @SerializedName("to")
    @Expose
    private Integer to;
    @SerializedName("message")
    @Expose
    private String message;

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public Integer getTo() {
        return to;
    }

    public void setTo(Integer to) {
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}