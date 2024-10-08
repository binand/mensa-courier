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

    @Inject
    NameRetriever mNameRetriever;

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

        return new SmsViewHolder(ItemMessageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false), mDecoder, mConverter, mNameRetriever);
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
        NameRetriever mNameRetriever;

        public SmsViewHolder(ViewBinding binding, EncoderDecoder<Serializable> decoder, EpochStringConverter converter, NameRetriever retriever) {

            super(binding.getRoot());
            mBinding = (ItemMessageBinding) binding;
            mDecoder = decoder;
            mConverter = converter.withFormatter("yyyy-MM-dd HH:mm");
            mNameRetriever = retriever;
        }

        @Override
        public void bindViewHolder(SmsContents sms) {

            String name = mNameRetriever.retrieve(sms.getDisplayOriginatingAddress());
            mBinding.tvFrom.setText(name == null ? sms.getDisplayOriginatingAddress() : name);
            mBinding.tvSentTime.setText(mConverter.forward(sms.getSentAt()));
            mBinding.tvMessage.setText((CharSequence) mDecoder.decode(sms.getDisplayMessageBody()));
            mBinding.tvDelay1.setText(getDiff(sms.getForwardedAt(), sms.getSentAt()));
            mBinding.tvDelay2.setText(getDiff(sms.getReceivedAt(), sms.getForwardedAt()));
            Linkify.addLinks(mBinding.tvMessage, Linkify.ALL);
        }

        private String getDiff(Long m1, Long m2) {
            if (m1 == null || m2 == null) {

                return "N/A";
            }

            return String.valueOf((m1 - m2 + 500)/1000);
        }
    }
}
