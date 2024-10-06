package com.radongames.smslib;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.radongames.core.interfaces.Mergeable;
import com.radongames.json.annotations.JsonExclude;
import com.radongames.json.gson.creators.GsonCreator;
import com.radongames.json.interfaces.JsonSerializable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public final class SmsContents implements JsonSerializable<SmsContents>, Mergeable<SmsContents> {

    public static final Long INVALID_ID = -1L;

    @JsonExclude
    private Long mId = INVALID_ID;
    @SerializedName("orig_addr")
    private String mOriginatingAddress;
    @SerializedName("orig_daddr")
    private String mDisplayOriginatingAddress;
    @SerializedName("msg_body")
    private String mMessageBody;
    @SerializedName("msg_dbody")
    private String mDisplayMessageBody;
    @SerializedName("svc_addr")
    private String mServiceCentreAddress;
    @SerializedName("email_from")
    private String mEmailFrom;
    @SerializedName("email_body")
    private String mEmailBody;
    @SerializedName("pseudo_subj")
    private String mPseudoSubject;
    @SerializedName("ts_sent")
    private Long mSentAt;
    @SerializedName("ts_fwded")
    private Long mForwardedAt;
    @JsonExclude
    private Long mReceivedAt;

    private static final Gson sGson = new GsonCreator().create();

    @Override
    public String toJson() {

        return sGson.toJson(this);
    }

    @Override
    public SmsContents applyJson(String s) {

        return merge(sGson.fromJson(s, getClass()));
    }

    /*
     * Not intelligent merge. We simply overwrite this with that.
     */
    @Override
    public SmsContents merge(SmsContents that) {

        this.setOriginatingAddress(that.getOriginatingAddress());
        this.setDisplayOriginatingAddress(that.getDisplayOriginatingAddress());
        this.setMessageBody(that.getMessageBody());
        this.setDisplayMessageBody(that.getDisplayMessageBody());
        this.setServiceCentreAddress(that.getServiceCentreAddress());
        this.setEmailFrom(that.getEmailFrom());
        this.setEmailBody(that.getEmailBody());
        this.setPseudoSubject(that.getPseudoSubject());
        this.setSentAt(that.getSentAt());
        this.setForwardedAt(that.getForwardedAt());
        this.setReceivedAt(System.currentTimeMillis());

        return this;
    }
}
