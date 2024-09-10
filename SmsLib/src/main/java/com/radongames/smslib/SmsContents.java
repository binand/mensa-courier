package com.radongames.smslib;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
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
public final class SmsContents implements JsonSerializable<SmsContents> {

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
    @SerializedName("timestamp")
    private long mTimestamp;

    @JsonExclude
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private static final Gson sGson = new GsonCreator().create();

    @Override
    public String toJson() {

        return sGson.toJson(this);
    }

    @Override
    public SmsContents applyJson(String s) {

        return sGson.fromJson(s, getClass());
    }
}
