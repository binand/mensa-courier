package in.radongames.smsreceiver;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.radongames.smslib.SmsContents;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SmsViewModel extends AndroidViewModel {

    @Inject
    SmsRepository mRepository;

    @Inject
    public SmsViewModel(@NonNull Application app) {

        super(app);
    }

    public LiveData<List<SmsContents>> getMessages() {

        return mRepository.observableFindAll();
    }

    public LiveData<Long> getCount() {

        return mRepository.observableCount();
    }
}
