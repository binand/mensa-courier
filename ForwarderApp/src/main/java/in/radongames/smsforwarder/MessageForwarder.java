package in.radongames.smsforwarder;

import com.radongames.json.interfaces.JsonSerializable;

public interface MessageForwarder {

    void forward(JsonSerializable<?> msg);
}
