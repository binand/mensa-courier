package in.radongames.smsreceiver;

import com.radongames.core.exceptions.RepositoryException;
import com.radongames.core.interfaces.CrudRepository;
import com.radongames.smslib.SmsContents;

import javax.inject.Inject;
import javax.inject.Singleton;

import lombok.NonNull;

@Singleton
public class SmsRepository implements CrudRepository<SmsContents, Long> {

    @Inject
    SmsMapper mMapper;

    @Inject
    SmsDao mDao;

    @Override
    public SmsContents save(@NonNull SmsContents sms) throws RepositoryException {
        return null;
    }

    @Override
    public Iterable<SmsContents> saveAll(@NonNull Iterable<SmsContents> it) throws RepositoryException {
        return null;
    }

    @Override
    public SmsContents findById(@NonNull Long id) {
        return null;
    }

    @Override
    public boolean existsById(@NonNull Long id) {
        return false;
    }

    @Override
    public Iterable<SmsContents> findAll() {
        return null;
    }

    @Override
    public Iterable<SmsContents> findAllById(@NonNull Iterable<Long> it) throws RepositoryException {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(@NonNull Long id) {

    }

    @Override
    public void delete(@NonNull SmsContents sms) {

    }

    @Override
    public void deleteAllById(@NonNull Iterable<Long> it) throws RepositoryException {

    }

    @Override
    public void deleteAll(@NonNull Iterable<SmsContents> it) throws RepositoryException {

    }

    @Override
    public void deleteAll() {

//        mDao.deleteAll();
    }
}
