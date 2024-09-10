package com.radongames.smslib;

import com.google.gson.Gson;
import com.radongames.json.annotations.JsonExclude;
import com.radongames.json.gson.creators.GsonCreator;
import com.radongames.json.interfaces.JsonSerializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public final class SmsContents implements JsonSerializable<SmsContents> {

    private String mOriginatingAddress;
    private String mDisplayOriginatingAddress;
    private String mMessageBody;
    private String mDisplayMessageBody;
    private String mServiceCentreAddress;
    private String mEmailFrom;
    private String mEmailBody;
    private String mPseudoSubject;
    private long mTimestamp;

    @JsonExclude
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
