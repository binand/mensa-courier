package in.radongames.smsreceiver;

import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewbinding.ViewBinding;

import com.radongames.android.olmur.adapter.OlmurListAdapter;
import com.radongames.android.olmur.adapter.OlmurViewHolder;
import com.radongames.core.converters.EpochStringConverter;
import com.radongames.core.interfaces.EncoderDecoder;
import com.radongames.smslib.SmsContents;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.scopes.ActivityScoped;
import in.radongames.smsreceiver.databinding.ItemMessageBinding;
import lombok.CustomLog;
import lombok.NoArgsConstructor;

@ActivityScoped
@NoArgsConstructor(onConstructor_={@Inject})
@CustomLog
public class SmsDisplayAdapter extends OlmurListAdapter<SmsContents, SmsDisplayAdapter.SmsViewHolder> {

    @Inject
    EncoderDecoder<Serializable> mDecoder;

    @Inject
    EpochStringConverter mConverter;

    public void setMessages(List<SmsContents> messages) {

        log.debug("setMessages(): Received set of: " + messages.size() + " messages.");
        adapterItems.clear();
        adapterItems.addAll(messages);
        notifyItemRangeChanged(0, messages.size());
    }

    public SmsContents removeMessage(int position) {

        SmsContents message = adapterItems.remove(position);
        if (message != null) {

            notifyItemRemoved(position);
        }

        return message;
    }

    @Override
    public SmsViewHolder createViewHolder(int i, ViewGroup parent) {

        return new SmsViewHolder(ItemMessageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false), mDecoder, mConverter);
    }

    @NonNull
    @Override
    public List<SmsContents> initItemsCollection() {

        return new ArrayList<>();
    }

    @Override
    public void onBindViewHolder(@NonNull SmsViewHolder holder, int position) {

        log.debug("Binding position: " + position + " to: " + adapterItems.get(position));
        holder.bindViewHolder(adapterItems.get(position));
    }

    @Override
    public int getItemCount() {

        return adapterItems.size();
    }

    public static class SmsViewHolder extends OlmurViewHolder<SmsContents> {

        ItemMessageBinding mBinding;
        EncoderDecoder<Serializable> mDecoder;
        EpochStringConverter mConverter;

        public SmsViewHolder(ViewBinding binding, EncoderDecoder<Serializable> decoder, EpochStringConverter converter) {

            super(binding.getRoot());
            mBinding = (ItemMessageBinding) binding;
            mDecoder = decoder;
            mConverter = converter.withFormatter("yyyy-MM-dd HH:mm");
        }

        @Override
        public void bindViewHolder(SmsContents sms) {

            mBinding.tvFrom.setText(sms.getDisplayOriginatingAddress());
            mBinding.tvSentTime.setText(mConverter.forward(sms.getSentAt()));
            mBinding.tvMessage.setText((CharSequence) mDecoder.decode(sms.getDisplayMessageBody()));
            Linkify.addLinks(mBinding.tvMessage, Linkify.ALL);
        }
    }
}
