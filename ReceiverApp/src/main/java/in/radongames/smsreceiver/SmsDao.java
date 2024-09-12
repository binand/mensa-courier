package in.radongames.smsreceiver;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Query;
import androidx.room.Upsert;

import com.radongames.core.exceptions.RepositoryException;
import com.radongames.core.interfaces.CrudRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import lombok.NonNull;

@Dao
public abstract class SmsDao implements CrudRepository<SmsEntity, Long> {

    @Override
    public SmsEntity save(@NonNull SmsEntity smsEntity) throws RepositoryException {

        long id = saveInternal(smsEntity);
        /*
         * Documentation says the return value of UPSERT is:
         * The SQLite row id or -1 if the insertion failed and update is performed
         */
        return id == -1 ? smsEntity : findById(id);
    }

    @Override
    public List<SmsEntity> saveAll(@NonNull Iterable<SmsEntity> iterable) {

        return findAllById(saveAllInternal(convertIterableToList(iterable)));
    }

    @Override
    @Query("SELECT * FROM messages WHERE id = :id")
    public abstract SmsEntity findById(@NonNull Long id);

    @Override
    @Query("SELECT EXISTS(SELECT 1 FROM messages WHERE id = :id)")
    public abstract boolean existsById(@NonNull Long id);

    @Override
    @Query("SELECT * FROM messages")
    public abstract List<SmsEntity> findAll();

    @Override
    public List<SmsEntity> findAllById(@NonNull Iterable<Long> iterable) throws RepositoryException {

        return findAllByIdInternal(convertIterableToList(iterable));
    }

    @Override
    @Query("SELECT COUNT(id) FROM messages")
    public abstract long count();

    @Override
    @Query("DELETE FROM messages WHERE id = :id")
    public abstract void deleteById(@NonNull Long id);

    @Override
    @Delete
    public abstract void delete(@NonNull SmsEntity smsEntity);

    @Override
    /*
     * For some reason, DELETE doesn't seem to accept an iterable
     * to build the WHERE clause even though SELECT does (see findAllById()).
     *
     * When this issue is fixed, we can make this abstract with the below
     * @Query annotation enabled.
     *
     * @Query("DELETE FROM messages WHERE id IN (:iterable)")
     */
    public void deleteAllById(@NonNull Iterable<Long> iterable) throws RepositoryException {

        deleteAll(findAllById(iterable));
    }

    @Override
    @Delete
    public abstract void deleteAll(@NonNull Iterable<SmsEntity> iterable) throws RepositoryException;

    @Override
    @Query("DELETE FROM messages")
    public abstract void deleteAll();

    private <T> List<T> convertIterableToList(Iterable<T> iterable) {

        return StreamSupport.stream(iterable.spliterator(), false).collect(Collectors.toList());
    }

    /*
     * Internal methods.
     */
    @Upsert
    protected abstract long saveInternal(@NonNull SmsEntity smsEntity) throws RepositoryException;

    @Upsert
    protected abstract List<Long> saveAllInternal(@NonNull List<SmsEntity> iterable);

    @Query("SELECT * FROM messages WHERE id IN (:iterable)")
    protected abstract List<SmsEntity> findAllByIdInternal(@NonNull List<Long> iterable) throws RepositoryException;
}
