package in.radongames.smsreceiver;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.radongames.core.interfaces.Bindable;
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
public class SmsDisplayAdapter extends RecyclerView.Adapter<SmsDisplayAdapter.SmsViewHolder> {

    List<SmsContents> mItems = new ArrayList<>();

    public void setMessages(List<SmsContents> messages) {

        log.debug("setMessages(): Received set of: " + messages.size() + " messages.");
        mItems.clear();
        mItems.addAll(messages);
        notifyItemRangeChanged(0, messages.size());
    }

    @NonNull
    @Override
    public SmsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new SmsViewHolder(ItemMessageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SmsViewHolder holder, int position) {

        holder.bind(mItems.get(position));
    }

    @Override
    public int getItemCount() {

        return mItems.size();
    }

    public static class SmsViewHolder extends RecyclerView.ViewHolder implements Bindable<SmsContents> {

        ItemMessageBinding mBinding;

        public SmsViewHolder(ViewBinding binding) {

            super(binding.getRoot());
            mBinding = (ItemMessageBinding) binding;
        }

        @Override
        public void bind(SmsContents sms) {

            mBinding.tvFrom.setText(sms.getDisplayOriginatingAddress());
            mBinding.tvTimestamp.setText(sms.getTimestamp() + "");
            mBinding.tvMessage.setText(sms.getDisplayMessageBody());
        }
    }
}
