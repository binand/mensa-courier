package in.radongames.smsreceiver;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewbinding.ViewBinding;

import com.radongames.android.olmur.adapter.OlmurListAdapter;
import com.radongames.android.olmur.adapter.OlmurViewHolder;
import com.radongames.smslib.SmsContents;

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

        return new SmsViewHolder(ItemMessageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
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

        public SmsViewHolder(ViewBinding binding) {

            super(binding.getRoot());
            mBinding = (ItemMessageBinding) binding;
        }

        @Override
        public void bindViewHolder(SmsContents sms) {

            mBinding.tvFrom.setText(sms.getDisplayOriginatingAddress());
            mBinding.tvTimestamp.setText(sms.getTimestamp());
            mBinding.tvMessage.setText(sms.getDisplayMessageBody());
        }
    }
}
