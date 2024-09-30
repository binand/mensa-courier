package in.radongames.smsreceiver;

import androidx.lifecycle.LiveData;

import com.google.common.util.concurrent.ListenableFuture;
import com.radongames.android.data.ListenableFutureConverter;
import com.radongames.android.data.LiveDataConverter;
import com.radongames.android.data.ObservableCrudRepository;
import com.radongames.core.converters.ListConverter;
import com.radongames.core.exceptions.RepositoryException;
import com.radongames.smslib.SmsContents;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import lombok.NoArgsConstructor;
import lombok.NonNull;

@Singleton
@NoArgsConstructor(onConstructor_={@Inject})
public class SmsRepository implements ObservableCrudRepository<SmsContents, Long> {

    @Inject
    SmsMapper mMapper;

    @Inject
    SmsListMapper mListMapper;

    @Inject
    SmsDao mDao;

    @Inject
    ListenableFutureConverter<SmsEntity, SmsContents> mListenableFutureConverter;

    @Inject
    ListConverter<SmsEntity, SmsContents> mListConverter;

    @Inject
    ListenableFutureConverter<List<SmsEntity>, List<SmsContents>> mListenableFutureListConverter;

    @Inject
    LiveDataConverter<List<SmsEntity>, List<SmsContents>> mLiveDataListConverter;

    @Override
    public SmsContents save(@NonNull SmsContents message) throws RepositoryException {

        return mMapper.forward(mDao.save(mMapper.backward(message)));
    }

    @Override
    public List<SmsContents> saveAll(@NonNull List<SmsContents> messages) throws RepositoryException {

        return mListMapper.forward(mDao.saveAll(mListMapper.backward(messages)));
    }

    @Override
    public SmsContents findById(@NonNull Long id) {

        return mMapper.forward(mDao.findById(id));
    }

    @Override
    public boolean existsById(@NonNull Long id) {

        return mDao.existsById(id);
    }

    @Override
    public List<SmsContents> findAll() {

        return mListMapper.forward(mDao.findAll());
    }

    @Override
    public List<SmsContents> findAllById(@NonNull List<Long> ids) throws RepositoryException {

        return mListMapper.forward(mDao.findAllById(ids));
    }

    @Override
    public long count() {

        return mDao.count();
    }

    @Override
    public void deleteById(@NonNull Long id) {

        mDao.deleteById(id);
    }

    @Override
    public void delete(@NonNull SmsContents message) {

        mDao.delete(mMapper.backward(message));
    }

    @Override
    public void deleteAllById(@NonNull List<Long> ids) throws RepositoryException {

        mDao.deleteAllById(ids);
    }

    @Override
    public void deleteAll(@NonNull List<SmsContents> messages) throws RepositoryException {

        mDao.deleteAll(mListMapper.backward(messages));
    }

    @Override
    public void deleteAll() {

        mDao.deleteAll();
    }

    @Override
    public ListenableFuture<SmsContents> observableSave(@NonNull SmsContents message) {

        return mListenableFutureConverter.forward(mDao.observableSave(mMapper.backward(message)));
    }

    @Override
    public ListenableFuture<List<SmsContents>> observableSaveAll(@NonNull List<SmsContents> messages) {

        return mListenableFutureListConverter.forward(mDao.observableSaveAll(mListConverter.backward(messages)));
    }

    @Override
    public ListenableFuture<SmsContents> observableFindById(@NonNull Long id) {

        return mListenableFutureConverter.forward(mDao.observableFindById(id));
    }

    @Override
    public ListenableFuture<Boolean> observableExistsById(@NonNull Long id) {

        return mDao.observableExistsById(id);
    }

    @Override
    public LiveData<List<SmsContents>> observableFindAll() {

        return mLiveDataListConverter.forward(mDao.observableFindAll());
    }

    @Override
    public LiveData<List<SmsContents>> observableFindAllById(@NonNull List<Long> ids) {

        return mLiveDataListConverter.forward(mDao.observableFindAllById(ids));
    }

    @Override
    public LiveData<Long> observableCount() {

        return mDao.observableCount();
    }
}
